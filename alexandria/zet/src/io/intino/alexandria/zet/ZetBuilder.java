package io.intino.alexandria.zet;

import io.intino.alexandria.logger.Logger;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ZetBuilder {
	private final File source;

	public ZetBuilder(File file) {
		this.source = file;
	}

	public void put(long... data) {
		try {
			Files.move(merge(data).toPath(), source.toPath(), REPLACE_EXISTING);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private File merge(long[] data) {
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

	private ZetStream mergeFileWith(long[] data) {
		return new ZetStream.Union(new SourceZetStream(source), new SourceZetStream(data));
	}

}