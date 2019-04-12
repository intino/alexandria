package io.intino.alexandria.tabb;

import io.intino.alexandria.mapp.MappReader;
import io.intino.alexandria.mapp.MappStream;

import java.nio.ByteBuffer;
import java.util.List;

public class MappColumnStream implements ColumnStream {

	private final MappReader reader;
	private final Type type;
	private final List<String> labels;
	private MappStream.Item current;

	public MappColumnStream(MappReader reader, Type type) {
		this.reader = reader;
		this.type = type;
		this.labels = reader.labels();
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
		return new Mode(labels.toArray(new String[0]));
	}

	@Override
	public boolean hasNext() {
		return reader.hasNext();
	}

	@Override
	public void next() {
		current = reader.next();
	}

	@Override
	public Long key() {
		return current.key();
	}

	@Override
	public byte[] value() {
		return ByteBuffer.allocate(4).putInt(labels.indexOf(current.value())).array();
	}
}
