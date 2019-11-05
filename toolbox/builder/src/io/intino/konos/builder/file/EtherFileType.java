package io.intino.konos.builder.file;

import io.intino.konos.builder.KonosIcons;
import io.intino.tara.plugin.lang.file.TaraFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EtherFileType extends TaraFileType {
	private static TaraFileType INSTANCE;


	private EtherFileType() {
		super();
	}

	public static EtherFileType instance() {
		return INSTANCE != null ? (EtherFileType) INSTANCE : (EtherFileType) (INSTANCE = new EtherFileType());
	}

	@NotNull
	public String getName() {
		return "Ether";
	}

	@NotNull
	public String getDescription() {
		return "Ether file";
	}

	@NotNull
	public String getDefaultExtension() {
		return "ether";
	}

	@Nullable
	@Override
	public javax.swing.Icon getIcon() {
		return KonosIcons.ICON_16;
	}


}