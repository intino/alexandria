package io.intino.konos.builder.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.module.Module;
import io.intino.konos.builder.KonosIcons;
import io.intino.tara.compiler.shared.Configuration;
import io.intino.tara.plugin.lang.psi.impl.TaraUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

abstract class KonosAction extends Action {

	public KonosAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
		super(text, description, icon);
	}

	@SuppressWarnings("Duplicates")
	@Override
	public void update(AnActionEvent e) {
		e.getPresentation().setIcon(KonosIcons.ICON_16);
		final Module module = e.getData(LangDataKeys.MODULE);
		boolean enabled = module != null && legioFile(module).exists();
		final File file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
		if (!file.exists()) return;
		String version = file.getParentFile().getName();
		final Configuration configuration = TaraUtil.configurationOf(module);
		if (configuration == null) return;
		final String interfaceVersion = configuration.interfaceVersion();
		e.getPresentation().setVisible(enabled & version.equals(interfaceVersion));
		e.getPresentation().setEnabled(enabled & version.equals(interfaceVersion));
	}

	@NotNull
	private File legioFile(Module module) {
		File moduleRoot = new File(module.getModuleFilePath()).getParentFile();
		return new File(moduleRoot, "configuration.legio");
	}
}
