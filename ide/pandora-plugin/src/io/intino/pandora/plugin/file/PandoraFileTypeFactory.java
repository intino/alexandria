package io.intino.pandora.plugin.file;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import org.jetbrains.annotations.NotNull;

public class PandoraFileTypeFactory extends com.intellij.openapi.fileTypes.FileTypeFactory {
	@Override
	public void createFileTypes(@NotNull FileTypeConsumer consumer) {
		consumer.consume(PandoraFileType.instance(), PandoraFileType.instance().getDefaultExtension());
	}
}
