package io.intino.alexandria.zim;

import io.intino.alexandria.inl.InlReader;
import io.intino.alexandria.inl.Message;
import io.intino.alexandria.logger.Logger;

import java.io.*;
import java.util.zip.ZipInputStream;

public class ZimReader implements ZimStream {
	public static final String ZimExtension = ".zim";
	private InlReader source;
	private Message current;
	private Message next;

	public ZimReader(File file) {
		this(streamOf(file));
	}

	public ZimReader(String text) {
		this(new ByteArrayInputStream(text.getBytes()));
	}

	public ZimReader(InputStream is) {
		try {
			this.source = new InlReader(is);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private static InputStream streamOf(File file) {
		try {
			return isZim(file) ? zipStreamOf(file) : new FileInputStream(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static boolean isZim(File file) {
		return file.getName().endsWith(ZimExtension);
	}

	private static InputStream zipStreamOf(File file) throws IOException {
		ZipInputStream zipStream = new ZipInputStream(new FileInputStream(file));
		zipStream.getNextEntry();
		return zipStream;
	}

	@Override
	public Message next() {
		if (current == next) hasNext();
		current = next;
		return current;
	}

	private Message nextMessageFromSource() {
		try {
			return source.next();
		} catch (IOException e) {
			Logger.error(e);
			return null;
		}
	}

	@Override
	public boolean hasNext() {
		if (current != next) return true;
		next = nextMessageFromSource();
		return next != null;
	}

	@Override
	public void close() {
		try {
			source.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}

}
