package io.intino.alexandria.zim;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageWriter;

import java.io.*;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.intino.alexandria.zim.Zim.ZIM_EXTENSION;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ZimBuilder {
	private final File source;

	public ZimBuilder(File file) {
		this.source = file;
		file.getParentFile().mkdirs();
	}

	public void put(Message... messages) {
		put(ZimStream.of(messages));
	}

	public void put(List<Message> messages) {
		put(ZimStream.of(messages));
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
		try (MessageWriter writer = new MessageWriter(open(file))) {
			try(Stream<Message> stream = mergeFileWith(data)) {
				Iterator<Message> iterator = stream.iterator();
				while(iterator.hasNext()) writer.write(iterator.next());
			}
		} catch (IOException e) {
			Logger.error(e);
		}
		return file;
	}

	private OutputStream open(File file) throws IOException {
		return Zim.compressing(new BufferedOutputStream(new FileOutputStream(file)));
	}

	private File tempFile() {
		try {
			return File.createTempFile("builder#", ZIM_EXTENSION);
		} catch (IOException e) {
			Logger.error(e);
			return new File("builder#" + UUID.randomUUID() + ZIM_EXTENSION);
		}
	}

	private Stream<Message> mergeFileWith(Stream<Message> data) {
		try {
			return Stream.of(ZimStream.of(source), data).flatMap(Function.identity());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}