package io.intino.konos.builder.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import io.intino.konos.builder.KonosIcons;
import org.jetbrains.annotations.Nullable;
import io.intino.tara.plugin.project.TaraModuleType;

import javax.swing.*;

abstract class Action extends AnAction {


	public Action(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
		super(text, description, icon);
	}

	@Override
	public void update(AnActionEvent e) {
		final boolean enabled = TaraModuleType.isTara(LangDataKeys.MODULE.getData(e.getDataContext()));
		e.getPresentation().setVisible(enabled);
		e.getPresentation().setEnabled(enabled);
		e.getPresentation().setIcon(KonosIcons.ICON_16);
	}
}
