package io.intino.pandora.plugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import io.intino.pandora.plugin.PandoraIcons;
import tara.intellij.lang.psi.impl.TaraUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class PandoraActionGroup extends DefaultActionGroup {

	public PandoraActionGroup() {
		super("Pandora", true);
		this.getTemplatePresentation().setText("Pandora");
		this.getTemplatePresentation().setVisible(true);
		this.getTemplatePresentation().setEnabled(true);
		this.getTemplatePresentation().setIcon(PandoraIcons.ICON_16);
	}

	@Override
	public void update(AnActionEvent e) {
		final Project project = e.getData(CommonDataKeys.PROJECT);
		boolean enabled = !collectModulesWithConfiguration(project).isEmpty();
		e.getPresentation().setVisible(enabled);
		e.getPresentation().setEnabled(enabled);
		e.getPresentation().setIcon(PandoraIcons.ICON_16);
	}

	private List<Module> collectModulesWithConfiguration(Project project) {
		List<Module> taraModules = new ArrayList<>();
		for (Module module : ModuleManager.getInstance(project).getModules())
			if (TaraUtil.configurationOf(module) != null)
				taraModules.add(module);
		return taraModules;
	}
}
