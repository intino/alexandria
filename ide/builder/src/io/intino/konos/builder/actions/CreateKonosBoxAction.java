package io.intino.konos.builder.actions;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.PsiTestUtil;
import io.intino.konos.builder.KonosIcons;
import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.utils.GraphLoader;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.legio.graph.Artifact.Imports.Dependency;
import io.intino.plugin.IntinoException;
import io.intino.plugin.project.LegioConfiguration;
import io.intino.plugin.project.LibraryConflictResolver.Version;
import io.intino.plugin.project.LibraryConflictResolver.VersionRange;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.JavaResourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.intellij.notification.NotificationType.ERROR;
import static com.intellij.notification.NotificationType.INFORMATION;
import static io.intino.plugin.project.LibraryConflictResolver.VersionRange.isRange;
import static io.intino.tara.plugin.lang.psi.impl.TaraUtil.*;

public class CreateKonosBoxAction extends KonosAction {
	private static final Logger LOG = Logger.getInstance("CreateKonosBoxAction: ");
	private static final String BOX = "box";
	private static final String TEXT = "Create Konos Box";

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
		final KonosGraph konosGraph = new KonosGenerator(module).generate(getSrcRoot(module), getGenRoot(module), getResRoot(module));
		if (konosGraph != null) updateDependencies(module, requiredDependencies(module, konosGraph));
	}

	private void updateDependencies(Module module, Map<String, String> requiredLibraries) {
		final LegioConfiguration conf = (LegioConfiguration) TaraUtil.configurationOf(module);
		final List<Dependency> dependencies = conf.graph().artifact().imports().dependencyList();
		conf.addCompileDependencies(requiredLibraries.keySet().stream().
				filter(dep -> findDependency(dependencies, dep) == null || (conf.groupId() + ":" + conf.artifactId()).equals(dep)).
				map(dep -> dep + ":" + requiredLibraries.get(dep)).
				collect(Collectors.toList()));
		conf.updateCompileDependencies(requiredLibraries.keySet().stream().
				filter(dep -> {
					final Dependency dependency = findDependency(dependencies, dep);
					return dependency != null && isOlder(dependency, requiredLibraries.get(dep));
				}).
				map(dep -> dep + ":" + requiredLibraries.get(dep)).
				collect(Collectors.toList()));
		conf.reload();
	}

	private boolean isOlder(Dependency dependency, String version) {
		try {
			final String dependencyVersion = dependency.effectiveVersion();
			if (isRange(dependencyVersion)) return !VersionRange.isInRange(version, VersionRange.rangeValuesOf(dependencyVersion));
			return new Version(dependencyVersion).compareTo(new Version(version)) < 0;
		} catch (IntinoException e) {
			LOG.error(e);
			return false;
		}
	}

	private Dependency findDependency(List<Dependency> dependencies, String artifact) {
		return dependencies.stream().filter(dependency -> artifact.equals(dependency.groupId() + ":" + dependency.artifactId())).findFirst().orElse(null);
	}

	private Map<String, String> requiredDependencies(Module module, KonosGraph konosGraph) {
		Type typeOfHashMap = new TypeToken<Map<String, String>>() {
		}.getType();
		final InputStream resourceAsStream = this.getClass().getResourceAsStream("/versions.json");
		if (resourceAsStream == null) return Collections.emptyMap();
		Map<String, String> dependencies = new Gson().fromJson(new InputStreamReader(resourceAsStream), typeOfHashMap);
		if (!hasModel(module) && konosGraph.jMXServiceList().isEmpty()) remove(dependencies, "jmx");
		if (konosGraph.jMSServiceList().isEmpty()) remove(dependencies, "jms");
		if (konosGraph.taskList().isEmpty()) remove(dependencies, "scheduler");
		if (konosGraph.nessClientList().isEmpty()) remove(dependencies, "ness-accessor");
		if (konosGraph.uIServiceList().isEmpty()) remove(dependencies, "ui");
		if (konosGraph.rESTServiceList().isEmpty()) {
			remove(dependencies, "rest");
			remove(dependencies, "ui");
		}
		if (konosGraph.slackBotServiceList().isEmpty()) remove(dependencies, "slack");
		return dependencies;
	}

	private void remove(Map<String, String> dependencies, String type) {
		String toRemove = null;
		for (String dep : dependencies.keySet()) if (dep.contains(type)) toRemove = dep;
		if (toRemove != null) dependencies.remove(toRemove);
	}

	private boolean hasModel(Module module) {
		return module != null && configurationOf(module) != null && hasModel(configurationOf(module));
	}

	private boolean hasModel(Configuration configuration) {
		return !configuration.languages().isEmpty();
	}

	private boolean noProject(AnActionEvent e, Project project) {
		if (project == null) {
			LOG.error("actionPerformed: no project for " + e);
			return true;
		}
		return false;
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
			KonosGraph graph = new GraphLoader().loadGraph(module);
			if (graph == null) {
				notifyError("Models have errors");
				return null;
			}
			if (!render(packageName, gen, src, res, graph)) return null;
			refreshDirectories(gen, src, res);
			notifySuccess();
			return graph;
		}

		private void refreshDirectories(File gen, File src, File res) {
			refreshDirectory(gen);
			refreshDirectory(src);
			refreshDirectory(res);
		}

		private boolean render(String packageName, File gen, File src, File res, KonosGraph graph) {
			try {
				new FullRenderer(module, graph, src, gen, res, packageName).execute();
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

}
