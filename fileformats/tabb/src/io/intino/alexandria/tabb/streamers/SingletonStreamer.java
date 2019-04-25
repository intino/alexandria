package io.intino.alexandria.tabb.streamers;

import io.intino.alexandria.tabb.ColumnStream;
import io.intino.alexandria.tabb.ColumnStreamer;
import io.intino.alexandria.zet.ZetStream;

public class SingletonStreamer implements ColumnStreamer {

	private final String name;
	private final ZetStream zetStream;
	private ColumnStream reference;

	public SingletonStreamer(String name, ZetStream zetStream) {
		this.name = name;
		this.zetStream = zetStream;
	}

	public SingletonStreamer with(ColumnStream columnStream) {
		this.reference = columnStream;
		return this;
	}

	@Override
	public ColumnStream[] get() {
		return new ColumnStream[]{create()};
	}

	private ColumnStream create() {
		return new ColumnStream() {
			@Override
			public String name() {
				return name;
			}

			@Override
			public Type type() {
				return Type.Boolean;
			}

			@Override
			public Mode mode() {
				return null;
			}

			@Override
			public boolean hasNext() {
				return reference.hasNext();
			}

			@Override
			public void next() {
				while (zetStream.hasNext() && zetStream.current() < reference.key())
					zetStream.next();
			}

			@Override
			public Long key() {
				return reference.key();
			}

			@Override
			public Object value() {
				return reference.key() == zetStream.current();
			}
		};
	}
}
