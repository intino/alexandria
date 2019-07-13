package io.intino.alexandria.zim;

import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageWriter;

import java.io.*;

public class ZimExtractor {
	private ZimReader reader;
	private MessageWriter writer;

	private ZimExtractor(InputStream stream) {
		this.reader = new ZimReader(stream);
	}

	public static ZimExtractor of(File file) throws FileNotFoundException {
		return ZimExtractor.of(new BufferedInputStream(new FileInputStream(file)));
	}

	public static ZimExtractor of(InputStream stream) {
		return new ZimExtractor(stream);
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
			for (String attribute : message.attributes()) export(folder, message, attribute);
			writer.write(message);
		}
		writer.close();
	}

	private void export(File folder, Message message, String attribute) throws IOException {
		String data = message.get(attribute).data();
		if (!data.contains("@")) return;
		export(folder, message, data.split("\n"));
		message.set(attribute, data);
	}

	private void export(File folder, Message message, String[] lines) throws IOException {
		for (String line : lines) export(folder, message, new Attachment(line));
	}

	private void export(File folder, Message message, Attachment attachment) throws IOException {
		byte[] data = message.attachment(attachment.uuid);
		if (data.length > 0) export(fileOf(folder, attachment), data);
	}

	private void export(File file, byte[] data) throws IOException {
		OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
		os.write(data);
		os.close();
	}

	private MessageWriter messageWriterOf(File folder) throws FileNotFoundException {
		return new MessageWriter(new FileOutputStream(new File(folder, "zim.inl")));
	}

	private File fileOf(File folder, Attachment attachment) {
		return new File(folder, attachment.filename());
	}

	private static class Attachment {
		final String name;
		final String uuid;

		Attachment(String line) {
			String[] split = line.split("@");
			this.name = split[0];
			this.uuid = split.length > 1 ? split[1] : "";
		}

		String filename() {
			return uuid + extensionOf(name);
		}

		private String extensionOf(String name) {
			return name.contains(".") ? name.substring(name.lastIndexOf(".")) : "";
		}
	}
}
