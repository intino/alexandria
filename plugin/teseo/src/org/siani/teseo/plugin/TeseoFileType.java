package org.siani.teseo.plugin;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TeseoFileType implements FileType {
	public static final TeseoFileType INSTANCE = new TeseoFileType();

	private TeseoFileType() {
	}

	@NotNull
	public String getName() {
		return "Teseo";
	}

	@NotNull
	public String getDescription() {
		return "Teseo file";
	}

	@NotNull
	public String getDefaultExtension() {
		return "teseo";
	}


	@Nullable
	@Override
	public javax.swing.Icon getIcon() {
		return TeseoIcons.ICON_16;
	}

	@Override
	public boolean isBinary() {
		return true;
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Nullable
	@Override
	public String getCharset(@NotNull VirtualFile file, @NotNull byte[] content) {
		return null;
	}

}