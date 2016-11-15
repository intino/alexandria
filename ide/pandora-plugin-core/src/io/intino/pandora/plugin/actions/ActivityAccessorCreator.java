package io.intino.pandora.plugin.actions;

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
import io.intino.pandora.plugin.codegeneration.accessor.ui.ActivityAccessorRenderer;
import tara.magritte.Graph;

import java.io.File;

class ActivityAccessorCreator {

	private final Project project;
	private final Module appModule;
	private final Activity activity;

	ActivityAccessorCreator(Module module, Graph graph) {
		this.project = module.getProject();
		appModule = module;
		this.activity = graph.find(Activity.class).get(0);
	}

	public void execute() {
		final Module webModule = getOrCreateModule();
		ActivityAccessorRenderer renderer = new ActivityAccessorRenderer(appModule, webModule, activity);
		renderer.execute();
	}

	private Module getOrCreateModule() {
		return ApplicationManager.getApplication().runWriteAction((Computable<Module>) () -> {
			final ModuleManager manager = ModuleManager.getInstance(project);
			Module[] modules = manager.getModules();
			for (Module m : modules) if (m.getName().equals(activity.name() + "-activity")) return m;
			Module webModule = manager.newModule(project.getBasePath() + File.separator + activity.name().toLowerCase() + "-activity" + File.separator + activity.name().toLowerCase() + "-activity" + ModuleFileType.DOT_DEFAULT_EXTENSION, WebModuleType.WEB_MODULE);
			final ModifiableRootModel model = ModuleRootManager.getInstance(webModule).getModifiableModel();
			model.addContentEntry(new File(webModule.getModuleFilePath()).getParent());
			model.commit();
			return webModule;
		});
	}

}
