package io.intino.pandora.plugin.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications.Bus;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import io.intino.pandora.plugin.codegeneration.accessor.rest.RESTAccessorRenderer;
import io.intino.pandora.plugin.rest.RESTService;
import org.apache.maven.shared.invoker.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.model.MavenId;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;
import tara.StashBuilder;
import tara.compiler.shared.Configuration;
import tara.dsl.Pandora;
import tara.intellij.actions.utils.FileSystemUtils;
import tara.intellij.lang.psi.impl.TaraUtil;
import tara.io.Stash;
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
import static java.io.File.separator;
import static org.jetbrains.idea.maven.utils.MavenUtil.resolveMavenHomeDirectory;

class AccessorsPublisher {
	private static final String PANDORA = "pandora";
	private static final Logger LOG = Logger.getInstance("Export Accessor: export");
	private final Module module;
	private File root;

	AccessorsPublisher(Module module) {
		this.module = module;
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
			final MavenProject project = MavenProjectsManager.getInstance(module.getProject()).findProject(module);
			if (project == null || apps.isEmpty()) return;
			mvn(project);
			for (String app : apps) notifySuccess(project.getMavenId(), app);
		} catch (IOException | MavenInvocationException e) {
			notifyError(e.getMessage());
			LOG.error(e.getMessage());
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
		final Configuration configuration = TaraUtil.configurationOf(module);
		String generationPackage = configuration != null ? configuration.workingPackage() : "pandora";
		final Stash[] stashes = PandoraUtils.findPandoraFiles(module).stream().map(p -> new StashBuilder(new File(p.getVirtualFile().getPath()), new Pandora(), module.getName()).build()).toArray(Stash[]::new);
		final Graph graph = GraphLoader.loadGraph(stashes).graph();
		if (graph == null) return Collections.emptyList();
		for (RESTService service : graph.find(RESTService.class)) {
			File sourcesDestiny = new File(new File(root, service.name() + File.separator + "src"), generationPackage);
			sourcesDestiny.mkdirs();
			new RESTAccessorRenderer(service, sourcesDestiny, generationPackage.replace(separator, ".")).execute();
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
		Bus.notify(new Notification("Pandora", "Accessor cannot be published. ", message, ERROR), module.getProject());
	}
}
