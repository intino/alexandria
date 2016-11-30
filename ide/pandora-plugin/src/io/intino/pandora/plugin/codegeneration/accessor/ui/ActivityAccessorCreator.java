package io.intino.pandora.plugin.codegeneration.accessor.ui;

import com.intellij.ide.highlighter.ModuleFileType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.WebModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Computable;
import io.intino.pandora.plugin.Activity;
import tara.magritte.Graph;

import java.io.File;
import java.util.List;

public class ActivityAccessorCreator {

	private final Project project;
	private final Module appModule;
	private final List<Activity> activities;

	public ActivityAccessorCreator(Module module, Graph graph) {
		this.project = module == null ? null : module.getProject();
		this.appModule = module;
		activities = graph.find(Activity.class);
	}

	public void execute() {
		for (Activity activity : activities) {
			Module webModule = getOrCreateModule(activity);
			ActivityAccessorRenderer renderer = new ActivityAccessorRenderer(appModule, webModule, activity);
			renderer.execute();
		}
	}

	private Module getOrCreateModule(Activity activity) {
		return ApplicationManager.getApplication().runWriteAction((Computable<Module>) () -> {
			final ModuleManager manager = ModuleManager.getInstance(project);
			Module[] modules = manager.getModules();
			for (Module m : modules) if (m.getName().equals(activity.name() + "-activity")) return m;
			Module webModule = manager.newModule(project.getBasePath() + File.separator + activity.name().toLowerCase() + "-activity" + File.separator + activity.name().toLowerCase() + "-activity" + ModuleFileType.DOT_DEFAULT_EXTENSION, WebModuleType.WEB_MODULE);
			final ModifiableRootModel model = ModuleRootManager.getInstance(webModule).getModifiableModel();
			final File parent = new File(webModule.getModuleFilePath()).getParentFile();
			parent.mkdirs();
			model.addContentEntry(parent.getAbsolutePath());
			model.commit();
			return webModule;
		});
	}

}
