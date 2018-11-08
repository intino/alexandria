package io.intino.alexandria.zim;

import io.intino.alexandria.inl.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ZimWriter {

	private final ZipOutputStream os;

	public ZimWriter(File file) throws IOException {
		file.getParentFile().mkdirs();
		this.os = new ZipOutputStream(new FileOutputStream(file));
		this.os.putNextEntry(new ZipEntry("events.inl"));
	}

	public void write(String message) throws IOException {
		String data = message + "\n\n";
		os.write(data.getBytes());
	}

	public void write(Message message) throws IOException {
		write(message.toString());
	}


	public void write(ZimStream stream) throws IOException {
		while (stream.hasNext()) write(stream.next());
	}

	public void close() throws IOException {
		os.closeEntry();
		os.close();
	}

}
