package io.intino.alexandria.message;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MessageWriter implements AutoCloseable {
	private final OutputStream os;

	public MessageWriter(OutputStream os) {
		this.os = new BufferedOutputStream(os);
	}

	public void write(Message message) throws IOException {
		write(message.toString());
		write("\n");
	}

	public void flush() throws IOException {
		os.flush();
	}

	private void write(String str) throws IOException {
		os.write(str.getBytes());
	}

	public void close() throws IOException {
		os.close();
	}


}
