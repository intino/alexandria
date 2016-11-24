package io.intino.pandora.plugin.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.notification.Notifications.Bus;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import io.intino.pandora.plugin.codegeneration.accessor.rest.RESTAccessorRenderer;
import io.intino.pandora.plugin.rest.RESTService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.intellij.notification.NotificationType.ERROR;
import static com.intellij.notification.NotificationType.INFORMATION;
import static org.jetbrains.idea.maven.utils.MavenUtil.resolveMavenHomeDirectory;

class AccessorsPublisher {
	private static final Logger LOG = Logger.getInstance("Publishing Accessor:");
	private static final String PANDORA = "pandora";
	private static final String REST_ACCESSOR_JAVA = "-rest-accessor-java";
	private final Module module;
	private final Graph graph;
	private final String generationPackage;
	private File root;

	AccessorsPublisher(Module module, Graph graph, String generationPackage) {
		this.module = module;
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
		try {
			final List<String> apps = createSources();
			final Configuration configuration = TaraUtil.configurationOf(module);
			if (configuration == null) return;
			else if (apps.isEmpty()) {
				notifyNothingDone();
				return;
			}
			mvn(configuration);
			for (String app : apps) notifySuccess(configuration, app);
		} catch (IOException | MavenInvocationException e) {
			notifyError(e.getMessage());
			LOG.error(e.getMessage());
		}
	}

	private void mvn(Configuration conf) throws MavenInvocationException, IOException {
		final File[] files = root.listFiles(File::isDirectory);
		for (File file : files != null ? files : new File[0]) {
			final File pom = createPom(file, conf.groupId(), file.getName() + REST_ACCESSOR_JAVA, conf.modelVersion());
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
		final Template template = AccessorPomTemplate.create();
		final File pomFile = new File(root, "pom.xml");
		Files.write(pomFile.toPath(), template.format(frame).getBytes());
		return pomFile;
	}

	private void notifySuccess(Configuration conf, String app) {
		final NotificationGroup balloon = NotificationGroup.toolWindowGroup("Tara Language", "Balloon");
		balloon.createNotification(app + " Accessor generated and uploaded", message(), INFORMATION, (n, e) -> {
			StringSelection selection = new StringSelection(newDependency(conf, app));
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);
		}).setImportant(true).notify(module.getProject());
	}

	private void notifyNothingDone() {
		Notifications.Bus.notify(
				new Notification("Pandora", "None rest services are found in module", module.getName(), INFORMATION), module.getProject());
	}

	@NotNull
	private String message() {
		return "<a href=\"#\">Copy maven dependency</a>";
	}

	@NotNull
	private String newDependency(Configuration conf, String app) {
		return "<dependency>\n" +
				"    <groupId>" + conf.groupId() + "</groupId>\n" +
				"    <artifactId>" + app + REST_ACCESSOR_JAVA + "</artifactId>\n" +
				"    <version>" + conf.modelVersion() + "</version>\n" +
				"</dependency>";
	}

	private void notifyError(String message) {
		Bus.notify(new Notification("Pandora", "Accessor cannot be published. ", message, ERROR), module.getProject());
	}
}
