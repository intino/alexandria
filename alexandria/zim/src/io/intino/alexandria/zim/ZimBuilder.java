package io.intino.alexandria.zim;

import io.intino.alexandria.inl.Message;
import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ZimBuilder {
	private final File source;

	public ZimBuilder(File file) {
		this.source = file;
		file.getParentFile().mkdirs();
	}

	public void put(Message... messages) {
		put(new ZimReader(messages));
	}

	public void put(List<Message> messages) {
		put(new ZimReader(messages));
	}

	public void put(Stream<Message> stream) {
		put(new ZimReader(stream));
	}

	public void put(ZimStream zimStream) {
		try {
			Files.move(merge(zimStream).toPath(), source.toPath(), REPLACE_EXISTING);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private File merge(ZimStream data) {
		File file = tempFile();
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
			ZimStream stream = mergeFileWith(data);
			while (stream.hasNext()) writer.write(stream.next() + "\n\n");
		} catch (IOException e) {
			Logger.error(e);
		}
		return file;
	}

	private File tempFile() {
		try {
			return File.createTempFile("merge", "zim");
		} catch (IOException e) {
			Logger.error(e);
			return new File("merge");
		}
	}

	private ZimStream mergeFileWith(ZimStream data) {
		return new ZimStream.Merge(new ZimReader(source), data);
	}

}