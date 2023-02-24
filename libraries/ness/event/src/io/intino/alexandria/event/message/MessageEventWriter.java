package io.intino.alexandria.event.message;

import io.intino.alexandria.zim.Zim;
import io.intino.alexandria.zim.ZimWriter;

import java.io.*;

public class MessageEventWriter implements io.intino.alexandria.event.EventWriter<MessageEvent> {

	private final ZimWriter writer;

	public MessageEventWriter(File file) throws IOException {
		this(file, true);
	}

	public MessageEventWriter(File file, boolean append) throws IOException {
		this(IO.open(file, append));
	}

	public MessageEventWriter(OutputStream destination) throws IOException {
		this.writer = new ZimWriter(Zim.compressing(destination));
	}

	@Override
	public void write(MessageEvent event) throws IOException {
		writer.write(event.toMessage());
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}
}