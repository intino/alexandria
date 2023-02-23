package io.intino.alexandria.ztp;

import io.intino.alexandria.resourcecleaner.DisposableResource;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

public class TupleWriter implements AutoCloseable {

	private final BufferedWriter writer;
	private final DisposableResource resource;

	public TupleWriter(OutputStream out) {
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

	@Override
	public void close() {
		resource.close();
	}
}
