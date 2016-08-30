package org.siani.teseo.plugin.file;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import org.jetbrains.annotations.NotNull;
import org.siani.teseo.plugin.TeseoFileType;

public class TeseoFileTypeFactory extends com.intellij.openapi.fileTypes.FileTypeFactory {
	@Override
	public void createFileTypes(@NotNull FileTypeConsumer consumer) {
		consumer.consume(TeseoFileType.INSTANCE, TeseoFileType.INSTANCE.getDefaultExtension());
	}
}
