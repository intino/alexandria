package io.intino.pandora.plugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.module.Module;
import io.intino.pandora.plugin.PandoraIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tara.intellij.lang.psi.impl.TaraUtil;

import javax.swing.*;
import java.io.File;

public abstract class PandoraAction extends io.intino.pandora.plugin.actions.Action {

	public PandoraAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
		super(text, description, icon);
	}

	@SuppressWarnings("Duplicates")
	@Override
	public void update(AnActionEvent e) {
		e.getPresentation().setIcon(PandoraIcons.ICON_16);
		final Module module = e.getData(LangDataKeys.MODULE);
		boolean enabled = module != null && legioFile(module).exists();
		final File file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
		if (!file.exists()) return;
		String version = file.getParentFile().getName();
		final String interfaceVersion = TaraUtil.configurationOf(module).interfaceVersion();
		e.getPresentation().setVisible(enabled & version.equals(interfaceVersion));
		e.getPresentation().setEnabled(enabled & version.equals(interfaceVersion));
	}

	@NotNull
	private File legioFile(Module module) {
		File moduleRoot = new File(module.getModuleFilePath()).getParentFile();
		return new File(moduleRoot, "configuration.legio");
	}
}
