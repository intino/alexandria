package io.intino.pandora.plugin.file;

import io.intino.pandora.plugin.PandoraIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tara.intellij.lang.file.TaraFileType;

public class PandoraFileType extends TaraFileType {
	private static TaraFileType INSTANCE;


	private PandoraFileType() {
		super();
	}

	public static PandoraFileType instance() {
		return INSTANCE != null ? (PandoraFileType) INSTANCE : (PandoraFileType) (INSTANCE = new PandoraFileType());
	}

	@NotNull
	public String getName() {
		return "Pandora";
	}

	@NotNull
	public String getDescription() {
		return "Pandora file";
	}

	@NotNull
	public String getDefaultExtension() {
		return "pandora";
	}

	@Nullable
	@Override
	public javax.swing.Icon getIcon() {
		return PandoraIcons.ICON_16;
	}


}