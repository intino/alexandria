package io.intino.alexandria.tabb;

import io.intino.alexandria.mapp.MappReader;
import io.intino.alexandria.mapp.MappStream;

import java.nio.ByteBuffer;
import java.util.List;

public class MappColumnStream implements ColumnStream {

	private final MappReader reader;
	private final Type type;
	private final List<String> labels;
	private boolean inited;
	private MappStream.Item current;

	public MappColumnStream(MappReader reader, Type type) {
		this.reader = reader;
		this.type = type;
		this.labels = reader.labels();
		this.inited = false;
	}

	@Override
	public String name() {
		return reader.name();
	}

	@Override
	public Type type() {
		return type;
	}

	@Override
	public Mode mode() {
		return new Mode(labels.stream().map(l -> l.replace("\n", "|")).toArray(String[]::new));
	}

	@Override
	public boolean hasNext() {
		return reader.hasNext();
	}

	@Override
	public void next() {
		current = reader.next();
		inited = true;
	}

	@Override
	public Long key() {
		if (!inited) next();
		return current.key();
	}

	@Override
	public byte[] value() {
		if (!inited) next();
		return ByteBuffer.allocate(4).putInt(labels.indexOf(current.value())).array();
	}
}
