package io.intino.alexandria.event.tuple;

import io.intino.alexandria.event.EventReader;

import java.io.*;

public class TupleEventReader implements EventReader<TupleEvent> {
	private static final String SEP = "\t";

	private final BufferedReader reader;
	private String currentLine;

	public TupleEventReader(File file) throws IOException {
		this.reader = new BufferedReader(new FileReader(file));
		advance();
	}

	@Override
	public void close() throws Exception {
		reader.close();
	}

	@Override
	public boolean hasNext() {
		return currentLine != null;
	}

	@Override
	public TupleEvent next() {
		String next = currentLine;
		advance();
		return new TupleEvent(next.split(SEP, -1));
	}

	private void advance() {
		try {
			String line;
			while((line = reader.readLine()) != null)
				if(!line.isEmpty()) break;
			currentLine = line;
		} catch (IOException e) {
			throw new RuntimeException(e); // TODO
		}
	}
}
