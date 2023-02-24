package io.intino.alexandria.ztp;

import io.intino.alexandria.resourcecleaner.DisposableResource;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

public class ZtpWriter implements AutoCloseable {

	private final BufferedWriter writer;
	private final DisposableResource resource;

	public ZtpWriter(File file) throws IOException {
		this(Ztp.compressing(new BufferedOutputStream(new FileOutputStream(file))));
	}

	public ZtpWriter(OutputStream out) {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		this.writer = writer;
		this.resource = DisposableResource.whenDestroyed(this).thenClose(writer);
	}

	public void write(Tuple... tuples) throws IOException {
		write(Arrays.stream(tuples));
	}

	public void write(Collection<Tuple> tuples) throws IOException {
		write(tuples.stream());
	}

	public void write(Stream<Tuple> tuples) throws IOException {
		Iterator<Tuple> iterator = tuples.iterator();
		while(iterator.hasNext()) write(iterator.next());
	}

	public void write(String str) throws IOException {
		if(str.isEmpty()) writer.newLine();
		else write(new Tuple(str));
	}

	public void write(Tuple tuple) throws IOException {
		writer.write(tuple.toString());
		writer.newLine();
	}

	public void flush() throws IOException {
		writer.flush();
	}

	@Override
	public void close() {
		resource.close();
	}
}
