package io.intino.konos.builder.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.LibraryOrderEntry;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.PsiTestUtil;
import io.intino.konos.builder.KonosIcons;
import io.intino.konos.builder.Manifest;
import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.cache.CacheReader;
import io.intino.konos.builder.codegeneration.cache.CacheWriter;
import io.intino.konos.builder.utils.GraphLoader;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.legio.graph.Artifact.Imports.Dependency;
import io.intino.plugin.IntinoException;
import io.intino.plugin.dependencyresolution.DependencyCatalog;
import io.intino.plugin.project.IntinoDirectory;
import io.intino.plugin.project.LegioConfiguration;
import io.intino.plugin.project.LibraryConflictResolver.Version;
import io.intino.plugin.project.LibraryConflictResolver.VersionRange;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.io.Stash;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.JavaResourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.intellij.notification.NotificationType.ERROR;
import static com.intellij.notification.NotificationType.INFORMATION;
import static io.intino.plugin.project.LibraryConflictResolver.VersionRange.isRange;
import static io.intino.tara.plugin.lang.psi.impl.TaraUtil.*;
import static java.util.stream.Collectors.toList;

public class CreateKonosBoxAction extends KonosAction {
	private static final Logger LOG = Logger.getInstance("CreateKonosBoxAction: ");
	private static final String BOX = "box";
	private static final String TEXT = "Create Konos Box";

	private static final String ElementCache = ".cache";

	public CreateKonosBoxAction() {
		super(TEXT, "Creates Konos Box", KonosIcons.GENERATE_16);
		this.setShortcutSet(CustomShortcutSet.fromString("control alt S"));
	}

	@Override
	public void update(AnActionEvent e) {
		e.getPresentation().setIcon(KonosIcons.GENERATE_16);
		super.update(e);
		final File file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
		if (!file.exists()) return;
		final Module module = e.getData(LangDataKeys.MODULE);
		if (module != null)
			e.getPresentation().setText(TEXT + " for " + module.getName() + " (" + file.getParentFile().getName() + ")");
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void actionPerformed(AnActionEvent e) {
		Project project = e.getData(PlatformDataKeys.PROJECT);
		final Module module = LangDataKeys.MODULE.getData(e.getDataContext());
		if (noProject(e, project) || module == null) return;
		project.save();
		FileDocumentManager.getInstance().saveAllDocuments();
		this.withTask(new Task.Backgroundable(project, module.getName() + ": Generating box...", false, PerformInBackgroundOption.ALWAYS_BACKGROUND) {
			@Override
			public void run(@NotNull ProgressIndicator progressIndicator) {
				final KonosGraph konosGraph = new KonosGenerator(module).generate(getSrcRoot(module), getGenRoot(module), getResRoot(module));
				if (konosGraph != null)
					ApplicationManager.getApplication().invokeLater(() -> updateDependencies(module, requiredDependencies(konosGraph)));
			}
		});
	}

	private void updateDependencies(Module module, Map<String, String> requiredLibraries) {
		final LegioConfiguration conf = (LegioConfiguration) TaraUtil.configurationOf(module);
		if (conf.groupId().equals("io.intino.alexandria")) return;
		final List<Dependency> toAdd = conf.graph().artifact().imports().dependencyList();
		List<OrderEntry> libraries = Arrays.stream(ModuleRootManager.getInstance(module).getModifiableModel().getOrderEntries()).filter(o -> o instanceof LibraryOrderEntry).collect(toList());
		List<String> collect = requiredLibraries.keySet().stream().
				filter(dep -> !isInModule(libraries, dep + ":" + requiredLibraries.get(dep)) && findDependency(toAdd, dep) == null || (conf.groupId() + ":" + conf.artifactId()).equals(dep)).
				map(dep -> dep + ":" + requiredLibraries.get(dep)).
				collect(toList());
		collect.forEach(d -> conf.addDependency(DependencyCatalog.DependencyScope.COMPILE, d));
		conf.updateCompileDependencies(requiredLibraries.keySet().stream().
				filter(dep -> {
					final Dependency dependency = findDependency(toAdd, dep);
					return dependency != null && isOlder(dependency, requiredLibraries.get(dep));
				}).
				map(dep -> dep + ":" + requiredLibraries.get(dep)).
				collect(toList()));
		conf.reload();
	}

	private boolean isInModule(List<OrderEntry> libraries, String dep) {
		return libraries.stream().anyMatch(library -> ((LibraryOrderEntry) library).getLibraryName().endsWith(dep));
	}

	private boolean isOlder(Dependency dependency, String version) {
		try {
			final String dependencyVersion = dependency.effectiveVersion().isEmpty() ? dependency.version() : dependency.effectiveVersion();
			if (isRange(dependencyVersion))
				return !VersionRange.isInRange(version, VersionRange.rangeValuesOf(dependencyVersion));
			if (dependencyVersion.isEmpty()) return false;
			if (version.isEmpty()) return true;
			return new Version(dependencyVersion).compareTo(new Version(version)) < 0;
		} catch (IntinoException e) {
			LOG.error(e);
			return false;
		}
	}

	private Dependency findDependency(List<Dependency> dependencies, String artifact) {
		return dependencies.stream().filter(dependency -> artifact.equals(dependency.groupId() + ":" + dependency.artifactId())).findFirst().orElse(null);
	}

	private Map<String, String> requiredDependencies(KonosGraph graph) {
		Map<String, String> dependencies = Manifest.load().dependencies;
		if (graph.jMXServiceList().isEmpty()) remove(dependencies, "jmx");
		if (graph.jMSServiceList().isEmpty()) remove(dependencies, "jms");
		if (graph.taskList().isEmpty()) remove(dependencies, "scheduler");
		if (graph.datalake() == null) remove(dependencies, "datalake");
		else if (!graph.datalake().isSshMirrored()) remove(dependencies, "sshj");
		if (graph.messageHub() == null) remove(dependencies, "message-hub");
		if (!graph.messageHub().isJmsBus()) remove(dependencies, "message-hub-jms");
		if (graph.uIServiceList().isEmpty()) remove(dependencies, "ui");
		if (graph.rESTServiceList().isEmpty()) remove(dependencies, "rest");
		if (graph.slackBotServiceList().isEmpty()) remove(dependencies, "slack");
		return dependencies;
	}

	private void remove(Map<String, String> dependencies, String type) {
		String toRemove = null;
		for (String dep : dependencies.keySet()) if (dep.contains(type)) toRemove = dep;
		if (toRemove != null) dependencies.remove(toRemove);
	}

	private boolean noProject(AnActionEvent e, Project project) {
		if (project == null) {
			LOG.error("actionPerformed: no project for " + e);
			return true;
		}
		return false;
	}

	private VirtualFile createDirectory(Module module, String name) {
		final Application a = ApplicationManager.getApplication();
		if (!a.isWriteAccessAllowed()) return a.runWriteAction((Computable<VirtualFile>) () -> create(module, name));
		return create(module, name);
	}

	@Nullable
	private VirtualFile create(Module module, String name) {
		try {
			final VirtualFile[] contentRoots = ModuleRootManager.getInstance(module).getContentRoots();
			return VfsUtil.createDirectoryIfMissing(contentRoots[0], name);
		} catch (IOException e) {
			return null;
		}
	}

	private void withTask(Task.Backgroundable runnable) {
		ProgressManager.getInstance().runProcessWithProgressAsynchronously(runnable, new BackgroundableProcessIndicator(runnable));
	}

	private VirtualFile getGenRoot(Module module) {
		for (VirtualFile file : getSourceRoots(module))
			if (file.isDirectory() && "gen".equals(file.getName())) return file;
		final VirtualFile genDirectory = createDirectory(module, "gen");
		if (genDirectory == null) return null;
		PsiTestUtil.addSourceRoot(module, genDirectory, JavaSourceRootType.SOURCE);
		return genDirectory;
	}

	private VirtualFile getResRoot(Module module) {
		for (VirtualFile file : getSourceRoots(module))
			if (file.isDirectory() && "res".equals(file.getName())) return file;
		final VirtualFile resDirectory = createDirectory(module, "res");
		PsiTestUtil.addSourceRoot(module, resDirectory, JavaResourceRootType.RESOURCE);
		return resDirectory;
	}

	private io.intino.konos.builder.codegeneration.cache.ElementCache loadCache(File folder, KonosGraph graph, Stash stash) {
		return new CacheReader(folder).load(graph, stash);
	}

	private void saveCache(io.intino.konos.builder.codegeneration.cache.ElementCache cache, File folder) {
		new CacheWriter(folder).save(cache);
	}

	private class KonosGenerator {

		private final Module module;

		KonosGenerator(Module module) {
			this.module = module;
		}

		KonosGraph generate(VirtualFile srcDirectory, VirtualFile genDirectory, VirtualFile resDirectory) {
			if (genDirectory == null) {
				notifyError("gen source root not found.");
				return null;
			}
			final Configuration configuration = configurationOf(module);
			String generationPackage = configuration == null ? BOX : configuration.workingPackage() + (configuration.boxPackage().isEmpty() ? "" : "." + configuration.boxPackage());
			File gen = new File(genDirectory.getPath(), generationPackage.replace(".", File.separator));
			gen.mkdirs();
			File src = new File(srcDirectory.getPath(), generationPackage.replace(".", File.separator));
			src.mkdirs();
			return generate(generationPackage, gen, src, new File(resDirectory.getPath()));
		}

		private KonosGraph generate(String packageName, File gen, File src, File res) {
			GraphLoader graphLoader = new GraphLoader();
			KonosGraph graph = graphLoader.loadGraph(module);
			if (graph == null) {
				notifyError("Models have errors");
				return null;
			}
			if (!render(packageName, gen, src, res, graph, graphLoader.konosStash())) return null;
			refreshDirectories(gen, src, res);
			notifySuccess();
			return graph;
		}

		private void refreshDirectories(File gen, File src, File res) {
			refreshDirectory(gen);
			refreshDirectory(src);
			refreshDirectory(res);
		}

		private boolean render(String packageName, File gen, File src, File res, KonosGraph graph, Stash stash) {
			try {
				File folder = new File(IntinoDirectory.of(module.getProject()) + "/box/" + module.getName());
				folder.mkdirs();
				io.intino.konos.builder.codegeneration.cache.ElementCache cache = loadCache(folder, graph, stash);
				new FullRenderer(graph, new Settings(module, src, gen, res, packageName, cache)).execute();
				saveCache(cache, folder);
			} catch (Exception e) {
				Logger.getInstance(this.getClass()).error(e.getMessage(), e);
				notifyError(e.getMessage() == null ? e.toString() : e.getMessage());
				return false;
			}
			return true;
		}

		private void notifySuccess() {
			final VirtualFile genRoot = getGenRoot(module);
			if (genRoot != null)
				Notifications.Bus.notify(
						new Notification("Boxing", "Services for " + module.getName(), "Generated", INFORMATION), module.getProject());
		}

		private void notifyError(String message) {
			Notifications.Bus.notify(
					new Notification("Boxing", "Services cannot be generated", message, ERROR), module.getProject());
		}

		private void refreshDirectory(File dir) {
			VirtualFile vDir = VfsUtil.findFileByIoFile(dir, true);
			if (vDir == null || !vDir.isValid()) return;
			VfsUtil.markDirtyAndRefresh(true, true, true, vDir);
			vDir.refresh(true, true);
		}

	}

}
