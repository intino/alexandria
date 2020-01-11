package io.intino.alexandria.event;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.MessageWriter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.zip.GZIPOutputStream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@SuppressWarnings({"WeakerAccess"})
public class EventWriter {
	private final File source;

	public EventWriter(File file) {
		this.source = file;
		file.getParentFile().mkdirs();
	}

	public void put(Event... messages) {
		put(new EventReader(messages));
	}

	public void put(List<Event> messages) {
		put(new EventReader(messages));
	}

	public void put(Stream<Event> stream) {
		put(new EventReader(stream));
	}

	public void put(EventStream eventStream) {
		try {
			Files.move(merge(eventStream).toPath(), source.toPath(), REPLACE_EXISTING);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private File merge(EventStream data) {
		File file = tempFile();
		try (MessageWriter writer = new MessageWriter(zipStream(file))) {
			EventStream stream = mergeFileWith(data);
			while (stream.hasNext()) writer.write(stream.next().toMessage());
		} catch (IOException e) {
			Logger.error(e);
		}
		return file;
	}

	private GZIPOutputStream zipStream(File file) throws IOException {
		return new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
	}


	private File tempFile() {
		try {
			return File.createTempFile("eventwriter#", ".zim");
		} catch (IOException e) {
			Logger.error(e);
			return new File("eventwriter#" + UUID.randomUUID().toString() + ".zim");
		}
	}

	private EventStream mergeFileWith(EventStream data) {
		return new EventStream.Merge(new EventReader(source), data);
	}

}