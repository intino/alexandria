package io.intino.alexandria.zim;

import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageWriter;

import java.io.*;

public class ZimExtractor {
	private final ZimReader reader;
	private MessageWriter writer;

	private ZimExtractor(InputStream stream) {
		this.reader = new ZimReader(stream);
	}

	public void to(File folder) throws IOException {
		this.writer = messageWriterOf(folder);
		export(createIfNotExists(folder));
	}

	private File createIfNotExists(File folder) {
		folder.mkdirs();
		return folder;
	}

	private void export(File folder) throws IOException {
		while (reader.hasNext()) {
			Message message = reader.next();
			writer.write(message);
		}
		writer.close();
	}

	private MessageWriter messageWriterOf(File folder) throws FileNotFoundException {
		return new MessageWriter(new FileOutputStream(new File(folder, "zim.inl")));
	}

	public static ZimExtractor of(File file) throws FileNotFoundException {
		return ZimExtractor.of(new BufferedInputStream(new FileInputStream(file)));
	}

	public static ZimExtractor of(InputStream stream) {
		return new ZimExtractor(stream);
	}
}
