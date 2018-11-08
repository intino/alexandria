package io.intino.alexandria.zim;

import io.intino.alexandria.inl.Message;
import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@SuppressWarnings("unused")
public class ZimBuilder {
	private final File source;

	public ZimBuilder(File file) {
		this.source = file;
		file.getParentFile().mkdirs();
	}

	public void put(Message... data) {
		try {
			Files.move(merge(data).toPath(), source.toPath(), REPLACE_EXISTING);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private File merge(Message[] data) {
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

	private ZimStream mergeFileWith(Message[] data) {
		return new ZimStream.Merge(new ZimReader(source), new ZimReader(data));
	}

}