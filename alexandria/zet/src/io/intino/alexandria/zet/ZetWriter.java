package io.intino.alexandria.zet;

import java.io.*;

@SuppressWarnings("unused")
public class ZetWriter {

	private final DataOutputStream stream;

	public ZetWriter(File file) throws IOException {
		stream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
	}

	public void write(long... ids) throws IOException {
		for (long id : ids) stream.writeLong(id);
	}

	public void write(ZetStream stream) throws IOException {
		while (stream.hasNext()) this.stream.writeLong(stream.next());
	}

	public void close() throws IOException {
		stream.close();
	}

}
