package io.intino.pandora.plugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.module.Module;
import io.intino.pandora.plugin.PandoraIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

public abstract class PandoraAction extends io.intino.pandora.plugin.actions.Action {

	public PandoraAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
		super(text, description, icon);
	}

	@Override
	public void update(AnActionEvent e) {
		final Module module = e.getData(LangDataKeys.MODULE);
		boolean enabled = module != null && hasLegioFile(module);
		e.getPresentation().setVisible(enabled);
		e.getPresentation().setEnabled(enabled);
		e.getPresentation().setIcon(PandoraIcons.ICON_16);
	}

	private boolean hasLegioFile(Module module) {
		File file = legioFile(module);
		return file.exists();
	}

	@NotNull
	private File legioFile(Module module) {
		File moduleRoot = new File(module.getModuleFilePath()).getParentFile();
		return new File(moduleRoot, "configuration.legio");
	}
}
