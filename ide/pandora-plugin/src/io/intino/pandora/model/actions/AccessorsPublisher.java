package io.intino.pandora.model.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.notification.Notifications.Bus;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import io.intino.pandora.model.codegeneration.accessor.jms.JMSAccessorRenderer;
import io.intino.pandora.model.codegeneration.accessor.jmx.JMXAccessorRenderer;
import io.intino.pandora.model.codegeneration.accessor.rest.RESTAccessorRenderer;
import io.intino.pandora.model.jms.JMSService;
import io.intino.pandora.model.jmx.JMXService;
import io.intino.pandora.model.rest.RESTService;
import org.apache.maven.shared.invoker.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.compiler.shared.Configuration;
import tara.intellij.actions.utils.FileSystemUtils;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.magritte.Graph;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.List;

import static com.intellij.notification.NotificationType.ERROR;
import static com.intellij.notification.NotificationType.INFORMATION;
import static org.jetbrains.idea.maven.utils.MavenUtil.resolveMavenHomeDirectory;

class AccessorsPublisher {
	private static final Logger LOG = Logger.getInstance("Publishing Accessor:");
	private static final String PANDORA = "pandora";
	private static final String ACCESSOR = "-accessor";
	private final Module module;
	private final Graph graph;
	private final String generationPackage;
	private File root;
	private Configuration configuration;

	AccessorsPublisher(Module module, Graph graph, String generationPackage) {
		this.module = module;
		configuration = TaraUtil.configurationOf(module);
		this.graph = graph;
		this.generationPackage = generationPackage;
		try {
			this.root = Files.createTempDirectory(PANDORA).toFile();
			FileSystemUtils.removeDir(this.root);
		} catch (IOException e) {
			root = null;
		}
	}

	void publish() {
		if (configuration == null) return;
		try {
			final List<String> apps = createSources();
			if (apps.isEmpty()) {
				notify("None rest services are found in module", INFORMATION);
				return;
			} else if (configuration.distributionReleaseRepository() == null) {
				notify("There isn't distribution repository defined", ERROR);
				return;
			}
			mvn(configuration);
			notifySuccess(configuration, apps);
		} catch (IOException | MavenInvocationException e) {
			notifyError(e.getMessage());
			LOG.error(e.getMessage());
		}
	}

	private void mvn(Configuration conf) throws MavenInvocationException, IOException {
		final File[] files = root.listFiles(File::isDirectory);
		for (File file : files != null ? files : new File[0]) {
			final File pom = createPom(file, conf.groupId(), file.getName() + ACCESSOR, conf.modelVersion());
			final InvocationResult result = invoke(pom);
			if (result != null && result.getExitCode() != 0) {
				if (result.getExecutionException() != null)
					throw new IOException("Failed to publish accessor.", result.getExecutionException());
				else throw new IOException("Failed to publish accessor. Exit code: " + result.getExitCode());
			} else if (result == null) throw new IOException("Failed to publish accessor. Maven HOME not found");
		}
	}

	private InvocationResult invoke(File pom) throws MavenInvocationException, IOException {
		final String ijMavenHome = MavenProjectsManager.getInstance(module.getProject()).getGeneralSettings().getMavenHome();
		InvocationRequest request = new DefaultInvocationRequest().setPomFile(pom).setGoals(Arrays.asList("clean", "install", "deploy"));
		final File mavenHome = resolveMavenHomeDirectory(ijMavenHome);
		if (mavenHome == null) return null;
		LOG.info("Maven HOME: " + mavenHome.getAbsolutePath());
		Invoker invoker = new DefaultInvoker().setMavenHome(mavenHome);
		log(invoker);
		config(request, mavenHome);
		return invoker.execute(request);
	}

	private void log(Invoker invoker) throws IOException {
		invoker.setErrorHandler(LOG::error);
		invoker.setOutputHandler(s -> {
			LOG.info(s);
			System.out.println(s);
		});
	}

	private void config(InvocationRequest request, File mavenHome) {
		final File mvn = new File(mavenHome, "bin" + File.separator + "mvn");
		mvn.setExecutable(true);
		final Sdk sdk = ModuleRootManager.getInstance(module).getSdk();
		if (sdk != null && sdk.getHomePath() != null) request.setJavaHome(new File(sdk.getHomePath()));
	}

	private List<String> createSources() throws IOException {
		List<String> apps = new ArrayList<>();
		if (graph == null) return Collections.emptyList();
		apps.addAll(rest());
		apps.addAll(jms());
		apps.addAll(jmx());
		return apps;
	}

	private List<String> jmx() {
		List<String> apps = new ArrayList<>();
		for (JMXService service : graph.find(JMXService.class)) {
			File sourcesDestiny = new File(new File(root, service.name() + File.separator + "src"), generationPackage.replace(".", File.separator));
			sourcesDestiny.mkdirs();
			new JMXAccessorRenderer(service, sourcesDestiny, generationPackage).execute();
			apps.add(service.name());
		}
		return apps;
	}

	private List<String> jms() {
		List<String> apps = new ArrayList<>();
		for (JMSService service : graph.find(JMSService.class)) {
			File sourcesDestiny = new File(new File(root, service.name() + File.separator + "src"), generationPackage.replace(".", File.separator));
			sourcesDestiny.mkdirs();
			new JMSAccessorRenderer(service, sourcesDestiny, generationPackage).execute();
			apps.add(service.name());
		}
		return apps;
	}

	private List<String> rest() {
		List<String> apps = new ArrayList<>();
		for (RESTService service : graph.find(RESTService.class)) {
			File sourcesDestiny = new File(new File(root, service.name() + File.separator + "src"), generationPackage.replace(".", File.separator));
			sourcesDestiny.mkdirs();
			new RESTAccessorRenderer(service, sourcesDestiny, generationPackage).execute();
			apps.add(service.name());
		}
		return apps;
	}


	private File createPom(File root, String group, String artifact, String version) throws IOException {
		final Frame frame = new Frame().addTypes("pom").addSlot("group", group).addSlot("artifact", artifact).addSlot("version", version);
		configuration.releaseRepositories().forEach((u, i) -> frame.addSlot("repository", createRepositoryFrame(u, i, "release")));
		SimpleEntry<String, String> distroRepo = configuration.distributionReleaseRepository();
		frame.addSlot("repository", createRepositoryFrame(distroRepo.getKey(), distroRepo.getValue(), "distribution"));
		final Template template = AccessorPomTemplate.create();
		final File pomFile = new File(root, "pom.xml");
		Files.write(pomFile.toPath(), template.format(frame).getBytes());
		return pomFile;
	}

	private Frame createRepositoryFrame(String url, String id, String type) {
		return new Frame().addTypes("repository", "release", type).
				addSlot("name", id).
				addSlot("url", url).
				addSlot("type", new Random().nextInt() % 10);
	}

	private void notifySuccess(Configuration conf, List<String> apps) {
		final NotificationGroup balloon = NotificationGroup.toolWindowGroup("Tara Language", "Balloon");
		for (String app : apps) {
			balloon.createNotification("Accessors generated and uploaded", message(), INFORMATION, (n, e) -> {
				StringSelection selection = new StringSelection(newDependency(conf, app));
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
			}).setImportant(true).notify(module.getProject());
		}
	}

	private void notify(String message, NotificationType type) {
		Notifications.Bus.notify(
				new Notification("Pandora", message, module.getName(), type), module.getProject());
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
				"    <version>" + conf.modelVersion() + "</version>\n" +
				"</dependency>";
	}

	private void notifyError(String message) {
		Bus.notify(new Notification("Pandora", "Accessor cannot be published. ", message, ERROR), module.getProject());
	}
}
