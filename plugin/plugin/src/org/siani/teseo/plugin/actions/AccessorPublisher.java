package org.siani.teseo.plugin.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications.Bus;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import org.apache.maven.shared.invoker.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.model.MavenId;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.intellij.actions.utils.FileSystemUtils;
import tara.io.StashDeserializer;
import tara.magritte.Graph;
import teseo.Application;
import teseo.TeseoApplication;
import teseo.codegeneration.accessor.JavaAccessorRenderer;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.intellij.notification.NotificationType.ERROR;
import static java.io.File.separator;
import static org.jetbrains.idea.maven.utils.MavenUtil.resolveMavenHomeDirectory;

class AccessorPublisher {
	private static final String TESEO = "teseo";
	private static final Logger LOG = Logger.getInstance("Export Accessor: export");
	private final Module module;
	private File root;

	AccessorPublisher(Module module) {
		this.module = module;
		try {
			this.root = Files.createTempDirectory(TESEO).toFile();
			FileSystemUtils.removeDir(this.root);
		} catch (IOException e) {
			root = null;
		}
	}

	void publish() {
		try {
			final List<String> apps = createSources();
			final MavenProject project = MavenProjectsManager.getInstance(module.getProject()).findProject(module);
			if (project == null) return;
			mvn(project);
			for (String app : apps) notifySuccess(project.getMavenId(), app);
		} catch (IOException | MavenInvocationException e) {
			notifyError(e.getMessage());
		}
	}

	private void mvn(MavenProject project) throws MavenInvocationException, IOException {
		final File[] files = root.listFiles(File::isDirectory);
		for (File file : files != null ? files : new File[0]) {
			final File pom = createPom(file, project.getMavenId().getGroupId(), file.getName() + "-accessor-java", project.getMavenId().getVersion());
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
		InvocationRequest request = new DefaultInvocationRequest().setPomFile(pom).setGoals(Arrays.asList("install", "deploy"));
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
		invoker.setOutputHandler(System.out::println);
	}

	private void config(InvocationRequest request, File mavenHome) {
		final File mvn = new File(mavenHome, "bin" + File.separator + "mvn");
		mvn.setExecutable(true);
		final Sdk sdk = ModuleRootManager.getInstance(module).getSdk();
		if (sdk != null && sdk.getHomePath() != null) request.setJavaHome(new File(sdk.getHomePath()));
	}

	private List<String> createSources() throws IOException {
		List<String> apps = new ArrayList<>();
		final String outLanguage = TeseoUtils.findOutLanguage(module);
		String packageName = TESEO + separator + (outLanguage == null || outLanguage.isEmpty() ? "api" : outLanguage.toLowerCase());
		for (Application app : Graph.load().loadStashes(StashDeserializer.stashFrom(new File(TeseoUtils.findTeseo(module)))).wrap(TeseoApplication.class).find(Application.class)) {
			File sourcesDestiny = new File(new File(root, app.name() + File.separator + "src"), packageName);
			sourcesDestiny.mkdirs();
			new JavaAccessorRenderer(app).execute(sourcesDestiny, packageName.replace(separator, "."));
			apps.add(app.name());
		}
		return apps;
	}

	private File createPom(File root, String group, String artifact, String version) throws IOException {
		final Frame frame = new Frame().addTypes("pom").addSlot("group", group).addSlot("artifact", artifact).addSlot("version", version);
		final Template template = PomTemplate.create();
		final File pomFile = new File(root, "pom.xml");
		Files.write(pomFile.toPath(), template.format(frame).getBytes());
		return pomFile;
	}

	private void notifySuccess(MavenId id, String app) {
		final NotificationGroup balloon = NotificationGroup.toolWindowGroup("Tara Language", "Balloon");
		balloon.createNotification(app + " Accessor generated and uploaded", message(), NotificationType.INFORMATION, (n, e) -> {
			StringSelection selection = new StringSelection(newDependency(id, app));
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);
		}).setImportant(true).notify(module.getProject());
	}

	@NotNull
	private String message() {
		return "<a href=\"#\">Copy maven dependency</a>";
	}

	@NotNull
	private String newDependency(MavenId id, String app) {
		return "<dependency>\n" +
				"    <groupId>" + id.getGroupId() + "</groupId>\n" +
				"    <artifactId>" + app + "-" + "accessor-java</artifactId>\n" +
				"    <version>" + id.getVersion() + "</version>\n" +
				"</dependency>";
	}

	private void notifyError(String message) {
		Bus.notify(new Notification("Teseo", "Accessor cannot be published. " + message, " ", ERROR), module.getProject());
	}
}
