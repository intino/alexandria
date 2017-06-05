package io.intino.konos.builder.codegeneration.accessor.ui;

import com.intellij.ide.highlighter.ModuleFileType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.WebModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import io.intino.konos.model.Activity;
import io.intino.tara.magritte.Graph;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import static io.intino.tara.plugin.project.configuration.ConfigurationManager.newExternalProvider;
import static io.intino.tara.plugin.project.configuration.ConfigurationManager.register;

public class ActivityAccessorCreator {

	private final Project project;
	private final Module javaModule;
	private final List<Activity> activities;

	public ActivityAccessorCreator(Module module, Graph graph) {
		this.project = module == null ? null : module.getProject();
		this.javaModule = module;
		activities = graph.find(Activity.class);
	}

	public void execute() {
		if (javaModule == null) return;
		for (Activity activity : activities) {
			Module webModule = getOrCreateModule(activity);
			final ActivityAccessorRenderer renderer = new ActivityAccessorRenderer(javaModule, webModule, activity);
			renderer.execute();
			final boolean created = renderer.createConfigurationFile();
			if (created) register(webModule, newExternalProvider(webModule));
		}
	}

	private Module getOrCreateModule(Activity activity) {
		return ApplicationManager.getApplication().runWriteAction((Computable<Module>) () -> {
			final ModuleManager manager = ModuleManager.getInstance(project);
			Module[] modules = manager.getModules();
			for (Module m : modules)
				if (m.getName().equals(toSnakeCase(activity.name()) + "-activity")) {
					addModuleDependency(m);
					return m;
				}
			Module webModule = manager.newModule(modulePath(activity), WebModuleType.WEB_MODULE);
			final ModifiableRootModel model = ModuleRootManager.getInstance(webModule).getModifiableModel();
			final File parent = new File(webModule.getModuleFilePath()).getParentFile();
			parent.mkdirs();
			final VirtualFile vFile = VfsUtil.findFileByIoFile(parent, true);
			final ContentEntry contentEntry = vFile != null ? model.addContentEntry(vFile) : model.addContentEntry(parent.getAbsolutePath());
			addExcludeFiles(parent, contentEntry);
			model.commit();
			return webModule;
		});
	}

	private void addExcludeFiles(File parent, ContentEntry contentEntry)  {
		final File lib = new File(parent, "lib");
		lib.mkdirs();
		contentEntry.addExcludeFolder(VfsUtil.findFileByIoFile(lib, true));
		final File dist = new File(parent, "dist");
		dist.mkdirs();
		contentEntry.addExcludeFolder(VfsUtil.findFileByIoFile(dist, true));

	}

	private String toSnakeCase(String name) {
		String regex = "([a-z])([A-Z]+)";
		String replacement = "$1-$2";
		return name.replaceAll(regex, replacement).toLowerCase();
	}

	@NotNull
	private String modulePath(Activity activity) {
		return project.getBasePath() + File.separator + toSnakeCase(activity.name()) + "-activity" + File.separator + toSnakeCase(activity.name()) + "-activity" + ModuleFileType.DOT_DEFAULT_EXTENSION;
	}

	private void addModuleDependency(Module webModule) {
		LibraryTable table = javaModule != null ? LibraryTablesRegistrar.getInstance().getLibraryTable(javaModule.getProject()) : null;
		if (table == null) return;
		final ModuleRootManager manager = ModuleRootManager.getInstance(javaModule);
		final ModifiableRootModel modifiableModel = manager.getModifiableModel();
		if (manager.isDependsOn(webModule)) return;
		modifiableModel.addModuleOrderEntry(webModule);
		modifiableModel.commit();
	}

}
