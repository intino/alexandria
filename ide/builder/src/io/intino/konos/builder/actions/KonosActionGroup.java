package io.intino.konos.builder.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import io.intino.konos.builder.KonosIcons;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class KonosActionGroup extends DefaultActionGroup {

	public KonosActionGroup() {
		super("Konos", true);
		this.getTemplatePresentation().setText("Konos");
		this.getTemplatePresentation().setVisible(true);
		this.getTemplatePresentation().setEnabled(true);
		this.getTemplatePresentation().setIcon(KonosIcons.ICON_16);
	}

	@Override
	public void update(AnActionEvent e) {
		final Project project = e.getData(CommonDataKeys.PROJECT);
		boolean enabled = !collectModulesWithConfiguration(project).isEmpty();
		e.getPresentation().setVisible(enabled);
		e.getPresentation().setEnabled(enabled);
		e.getPresentation().setIcon(KonosIcons.ICON_16);
	}

	private List<Module> collectModulesWithConfiguration(Project project) {
		List<Module> taraModules = new ArrayList<>();
		for (Module module : ModuleManager.getInstance(project).getModules())
			if (TaraUtil.configurationOf(module) != null)
				taraModules.add(module);
		return taraModules;
	}
}
