package io.intino.alexandria.event;

import io.intino.alexandria.event.util.EventFormats;
import io.intino.alexandria.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public abstract class AbstractEventWriter<T extends Event> implements EventWriter<T> {
	protected final File file;

	public AbstractEventWriter(File file) {
		this.file = file;
	}

	@Override
	public void put(Stream<T> eventStream) throws IOException {
		File temp = null;
		try {
			file.getParentFile().mkdirs();
			temp = merge(eventStream);
			Files.move(temp.toPath(), file.toPath(), REPLACE_EXISTING);
		} finally {
			if (temp != null) temp.delete();
		}
	}

	protected abstract File merge(Stream<T> data) throws IOException;

	protected Stream<T> mergeFileWith(Stream<T> data) throws IOException {
		return EventStream.merge(Stream.of(EventStream.of(file), data));
	}

	protected File tempFile() {
		String prefix = getClass().getSimpleName().toLowerCase() + "#";
		try {
			return File.createTempFile(prefix, EventFormats.formatOf(file).extension());
		} catch (IOException e) {
			Logger.error(e);
			return new File(prefix + UUID.randomUUID() + EventFormats.formatOf(file).extension());
		}
	}
}