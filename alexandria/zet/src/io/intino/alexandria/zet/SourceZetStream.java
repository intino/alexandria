package io.intino.alexandria.zet;

import io.intino.alexandria.zet.ZetStream;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class SourceZetStream implements ZetStream {
	private final Iterator<Long> iterator;
	private long current = - 1;

	public SourceZetStream(File file) {
		this(iteratorOf(inputStream(file)));
	}

	public SourceZetStream(long... ids) {
		this(Arrays.stream(ids).boxed().iterator());
	}

	public SourceZetStream(List<Long> ids) {
		this(ids.iterator());
	}

	public SourceZetStream(Iterator<Long> iterator) {
		this.iterator = iterator;
	}

	public SourceZetStream(Stream<Long> stream) {
		this.iterator = stream.iterator();
	}

	private static Iterator<Long> iteratorOf(DataInputStream stream) {
		return new Iterator<Long>() {
			private long current = -1;
			private long next = -1;

			@Override
			public Long next() {
				if (current == next) hasNext();
				current = next;
				return current;
			}

			@Override
			public boolean hasNext() {
				if (current != next) return true;
				try {
					next = stream.readLong();
					return true;
				} catch (IOException e) {
					next = -1;
					close();
					return false;
				}
			}

			private void close() {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}

	private static DataInputStream inputStream(File file) {
		try {
			return new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			return new DataInputStream(new ByteArrayInputStream(new byte[0]));
		}
	}

	@Override
	public long current() {
		return current;
	}

	@Override
	public long next() {
		return current = iterator.next();
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}
}
