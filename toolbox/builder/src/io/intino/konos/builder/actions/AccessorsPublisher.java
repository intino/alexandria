package io.intino.konos.builder.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.notification.Notifications.Bus;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.accessor.jmx.JMXAccessorRenderer;
import io.intino.konos.builder.codegeneration.accessor.messaging.MessagingAccessorRenderer;
import io.intino.konos.builder.codegeneration.accessor.rest.RESTAccessorRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.Service;
import io.intino.plugin.toolwindows.output.IntinoTopics;
import io.intino.plugin.toolwindows.output.MavenListener;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.actions.utils.FileSystemUtils;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.apache.maven.shared.invoker.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.project.MavenProjectsManager;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.intellij.notification.NotificationType.ERROR;
import static com.intellij.notification.NotificationType.INFORMATION;
import static org.jetbrains.idea.maven.utils.MavenUtil.resolveMavenHomeDirectory;

class AccessorsPublisher {
	private static final Logger LOG = Logger.getInstance("Publishing Accessor:");
	private static final String KONOS = "konos";
	private static final String ACCESSOR = "-accessor";
	private final Settings settings;
	private final KonosGraph graph;
	private StringBuilder log = new StringBuilder();
	private File root;
	private Configuration configuration;

	AccessorsPublisher(Settings settings, KonosGraph graph) {
		this.settings = settings;
		this.configuration = TaraUtil.configurationOf(settings.module());
		this.graph = graph;
		try {
			this.root = Files.createTempDirectory(KONOS).toFile();
			FileSystemUtils.removeDir(this.root);
		} catch (IOException e) {
			root = null;
		}
	}

	void install() {
		List<Service> services = collectServices();
		if (!createSources(services)) return;
		try {
			mvn(services, configuration, "");
		} catch (IOException | MavenInvocationException e) {
			notifyError(e.getMessage());
			LOG.error(e.getMessage());
		}
	}

	void publish() {
		List<Service> services = collectServices();
		if (!createSources(services)) return;
		try {
			mvn(services, configuration, "deploy");
		} catch (IOException | MavenInvocationException e) {
			notifyError(e.getMessage());
			LOG.error(e.getMessage());
		}
	}

	private boolean createSources(List<Service> services) {
		if (configuration == null) return false;
		if (graph == null) {
			notify("Box has errors. Check problems view for more information", INFORMATION);
			return false;
		}
		if (services.isEmpty()) {
			notify("No services found in module", INFORMATION);
			return false;
		}
		if (services.size() > 1)
			services = new SelectServicesDialog(WindowManager.getInstance().suggestParentWindow(module().getProject()), services).showAndGet();
		rest(services);
		jms(services);
		jmx(services);
		if (configuration.distributionReleaseRepository() == null) {
			notify("There isn't distribution repository defined", ERROR);
			return false;
		}
		return true;
	}

	private List<Service> collectServices() {
		return graph.serviceList().stream().filter(s -> !s.isUI() && !s.isSlackBot() && !s.isJMX()).collect(Collectors.toList());
	}

	private void mvn(List<Service> services, Configuration conf, String goal) throws MavenInvocationException, IOException {
		final File[] files = root.listFiles(File::isDirectory);
		for (File file : files != null ? files : new File[0]) {
			final Service service = services.stream().filter(s -> s.name$().equals(file.getName())).findFirst().orElse(null);
			final File pom = createPom(file, service, conf.groupId(), file.getName() + ACCESSOR, conf.version());
			final InvocationResult result = invoke(pom, goal);
			if (result != null && result.getExitCode() != 0) {
				if (result.getExecutionException() != null)
					throw new IOException("Failed to publish accessor.", result.getExecutionException());
				else throw new IOException("Failed to publish accessor. Exit code: " + result.getExitCode());
			} else if (result == null) throw new IOException("Failed to publish accessor. Maven HOME not found");
			notifySuccess(configuration, file.getName());
		}
	}

	private InvocationResult invoke(File pom, String goal) throws MavenInvocationException {
		List<String> goals = new ArrayList<>();
		final String ijMavenHome = MavenProjectsManager.getInstance(module().getProject()).getGeneralSettings().getMavenHome();
		goals.add("clean");
		goals.add("install");
		if (!goal.isEmpty()) goals.add(goal);
		InvocationRequest request = new DefaultInvocationRequest().setPomFile(pom).setGoals(goals);
		final File mavenHome = resolveMavenHomeDirectory(ijMavenHome);
		if (mavenHome == null) return null;
		LOG.info("Maven HOME: " + mavenHome.getAbsolutePath());
		Invoker invoker = new DefaultInvoker().setMavenHome(mavenHome);
		log(invoker);
		config(request, mavenHome);
		return invoker.execute(request);
	}

	private void log(Invoker invoker) {
		invoker.setErrorHandler(LOG::error);
		invoker.setOutputHandler(this::publish);
	}

	private void publish(String line) {
		Module module = module();
		if (module.getProject().isDisposed()) return;
		final MessageBus messageBus = module.getProject().getMessageBus();
		final MavenListener mavenListener = messageBus.syncPublisher(IntinoTopics.BUILD_CONSOLE);
		mavenListener.println(line);
		final MessageBusConnection connect = messageBus.connect();
		connect.deliverImmediately();
		connect.disconnect();
	}

	private void config(InvocationRequest request, File mavenHome) {
		final File mvn = new File(mavenHome, "bin" + File.separator + "mvn");
		mvn.setExecutable(true);
		final Sdk sdk = ModuleRootManager.getInstance(module()).getSdk();
		if (sdk != null && sdk.getHomePath() != null) request.setJavaHome(new File(sdk.getHomePath()));
	}


	private void rest(List<Service> services) {
		services.stream().filter(Service::isREST).forEach(s -> {
			File sourcesDestiny = new File(new File(root, s.name$() + File.separator + "src"), packageName().replace(".", File.separator));
			sourcesDestiny.mkdirs();
			new RESTAccessorRenderer(settings, s.asREST(), sourcesDestiny).execute();
		});
	}

	private void jms(List<Service> services) {
		services.stream().filter(Service::isMessaging).forEach(s -> {
			File sourcesDestiny = new File(new File(root, s.asMessaging().name$() + File.separator + "src"), packageName().replace(".", File.separator));
			sourcesDestiny.mkdirs();
			new MessagingAccessorRenderer(settings, s.asMessaging(), graph.workflow(), sourcesDestiny).execute();
		});
	}

	private void jmx(List<Service> services) {
		services.stream().filter(Service::isJMX).forEach(s -> {
			File sourcesDestiny = new File(new File(root, s.name$() + File.separator + "src"), packageName().replace(".", File.separator));
			sourcesDestiny.mkdirs();
			new JMXAccessorRenderer(settings, s.asJMX(), sourcesDestiny).execute();
		});
	}

	private File createPom(File root, Service service, String group, String artifact, String version) {
		final FrameBuilder builder = new FrameBuilder("pom").add("group", group).add("artifact", artifact).add("version", version);
		configuration.releaseRepositories().forEach((u, i) -> builder.add("repository", createRepositoryFrame(u, i, "release")));
		SimpleEntry<String, String> distroRepo = configuration.distributionReleaseRepository();
		builder.add("repository", createRepositoryFrame(distroRepo.getKey(), distroRepo.getValue(), "distribution"));
		builder.add("dependency", new FrameBuilder(serviceTypes(service)).add("value", "").toFrame());
		final File pomFile = new File(root, "pom.xml");
		Commons.write(pomFile.toPath(), new AccessorPomTemplate().render(builder.toFrame()));
		return pomFile;
	}

	@NotNull
	private String[] serviceTypes(Service service) {
		return service.core$().conceptList().stream().map(s -> (s.id().contains("$") ? s.id().split("\\$")[1] : s.id()).toLowerCase()).toArray(String[]::new);
	}

	private Frame createRepositoryFrame(String url, String id, String type) {
		return new FrameBuilder("repository", "release", type).
				add("name", id).
				add("random", UUID.randomUUID().toString()).
				add("url", url).toFrame();
	}

	private void notifySuccess(Configuration conf, String app) {
		final NotificationGroup balloon = NotificationGroup.toolWindowGroup("Tara Language", "Balloon");
		balloon.createNotification("Accessors generated and uploaded", message(), INFORMATION, (n, e) -> {
			StringSelection selection = new StringSelection(newDependency(conf, app));
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);
		}).setImportant(true).notify(module().getProject());
	}

	private void notify(String message, NotificationType type) {
		Notifications.Bus.notify(
				new Notification("Konos", message, module().getName(), type), module().getProject());
	}

	@NotNull
	private String message() {
		return "<a href=\"#\">Copy maven dependency</a>";
	}

	@NotNull
	private String newDependency(Configuration conf, String app) {
		return "<dependency>\n" +
				"    <groupId>" + conf.groupId().toLowerCase() + "</groupId>\n" +
				"    <artifactId>" + app.toLowerCase() + ACCESSOR + "</artifactId>\n" +
				"    <version>" + conf.version() + "</version>\n" +
				"</dependency>";
	}

	private void notifyError(String message) {
		final String result = log.toString();
		Bus.notify(new Notification("Konos", "Accessor cannot be published. ", message + "\n" + result, ERROR), module().getProject());
	}

	private Module module() {
		return settings.module();
	}

	private String packageName() {
		return settings.packageName();
	}
}
