package io.intino.alexandria.zit;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageWriter;
import org.xerial.snappy.SnappyOutputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ZitBuilder {

	private final File source;

	public ZitBuilder(File file) {
		this.source = file;
		file.getParentFile().mkdirs();
	}

	public void put(double[] messages) {
		put(ZitStream.of(messages));
	}

	public void put(List<Message> messages) {
		put(ZitStream.of(messages));
	}

	public void put(Stream<Message> zimStream) {
		try {
			Files.move(merge(zimStream).toPath(), source.toPath(), REPLACE_EXISTING);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private File merge(Stream<Message> data) {
		File file = tempFile();
		try (MessageWriter writer = new MessageWriter(zipStream(file))) {
			try(Stream<Message> stream = mergeFileWith(data)) {
				Iterator<Message> iterator = stream.iterator();
				while(iterator.hasNext()) writer.write(iterator.next());
			}
		} catch (IOException e) {
			Logger.error(e);
		}
		return file;
	}

	private SnappyOutputStream zipStream(File file) throws IOException {
		return new SnappyOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
	}

	private File tempFile() {
		try {
			return File.createTempFile("builder#", ".zim");
		} catch (IOException e) {
			Logger.error(e);
			return new File("builder#" + UUID.randomUUID().toString() + ".zim");
		}
	}

	private Stream<Message> mergeFileWith(Stream<Message> data) {
		try {
			return Stream.of(ZitStream.of(source), data).flatMap(Function.identity());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}