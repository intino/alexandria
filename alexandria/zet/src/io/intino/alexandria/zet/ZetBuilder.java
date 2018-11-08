package io.intino.alexandria.zet;

import io.intino.alexandria.logger.Logger;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ZetBuilder {
	private final File source;

	public ZetBuilder(File file) {
		this.source = file;
		file.getParentFile().mkdirs();
	}

	public void put(long... data) {
		put(new ZetReader(data));
	}

	public void put(List<Long> messages) {
		put(new ZetReader(messages));
	}

	public void put(Stream<Long> stream) {
		put(new ZetReader(stream));
	}

	public void put(ZetReader stream) {
		try {
			Files.move(merge(stream).toPath(), source.toPath(), REPLACE_EXISTING);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private File merge(ZetStream data) {
		File file = tempFile();
		try (DataOutputStream os = new DataOutputStream(new FileOutputStream(file))) {
			ZetStream stream = mergeFileWith(data);
			while (stream.hasNext()) os.writeLong(stream.next());
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

	private ZetStream mergeFileWith(ZetStream stream) {
		return new ZetStream.Union(new ZetReader(source), stream);
	}

}