package io.intino.konos.builder.file;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import org.jetbrains.annotations.NotNull;

public class KonosFileTypeFactory extends com.intellij.openapi.fileTypes.FileTypeFactory {
	@Override
	public void createFileTypes(@NotNull FileTypeConsumer consumer) {
		consumer.consume(KonosFileType.instance(), KonosFileType.instance().getDefaultExtension());
	}
}
