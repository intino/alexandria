package org.siani.teseo.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.module.Module;
import org.siani.teseo.plugin.TeseoIcons;
import org.siani.teseo.plugin.project.facet.TeseoFacet;

abstract class Action extends AnAction {

	@Override
	public void update(AnActionEvent e) {
		final Module module = LangDataKeys.MODULE.getData(e.getDataContext());
		final boolean enabled = TeseoFacet.isOfType(module);
		e.getPresentation().setVisible(enabled);
		e.getPresentation().setEnabled(enabled);
		e.getPresentation().setIcon(TeseoIcons.ICON_16);
	}
}
