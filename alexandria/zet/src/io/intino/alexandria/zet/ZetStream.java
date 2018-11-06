package io.intino.alexandria.zet;

import java.util.List;

import static java.util.Arrays.asList;

public interface ZetStream {

	long current();

	long next();

	boolean hasNext();

	class Difference implements ZetStream {

		private final List<ZetStream> streams;
		private long current = -1;
		private long next = -1;

		public Difference(List<ZetStream> streams) {
			this.streams = streams;
			streams.stream().filter(s -> s.current() == -1 && s.hasNext()).forEach(ZetStream::next);
		}

		public Difference(ZetStream... streams) {
			this(asList(streams));
		}

		@Override
		public long current() {
			return current;
		}

		@Override
		public long next() {
			if (current == next) hasNext();
			current = next;
			return current;
		}

		private long getNextValue() {
			long value = Long.MAX_VALUE;
			for (ZetStream zetStream : streams) {
				if (zetStream.current() > value || zetStream.current() == -1) continue;
				value = zetStream.current();
			}
			return value;
		}

		@Override
		public boolean hasNext() {
			if (current != next) return true;
			advanceStreamsWith(current);
			long value = getNextValue();
			if (value == Long.MAX_VALUE) {
				next = -1;
				return false;
			}
			while (!isValid(value)) {
				advanceStreamsWith(value);
				value = getNextValue();
				if (value == Long.MAX_VALUE) {
					next = -1;
					return false;
				}
			}
			next = value;
			return true;
		}

		private void advanceStreamsWith(long value) {
			for (ZetStream stream : streams) if (stream.current() == value) stream.next();
		}

		private boolean isValid(long value) {
			if (streams.get(0).current() != value) return false;
			for (int i = 1; i < streams.size(); i++) if (streams.get(i).current() == value) return false;
			return true;
		}

	}

	class Intersection implements ZetStream {

		private final List<ZetStream> streams;
		private long current = -1;
		private long next = -1;

		public Intersection(List<ZetStream> streams) {
			this.streams = streams;
			streams.stream().filter(s -> s.current() == -1 && s.hasNext()).forEach(ZetStream::next);
		}

		public Intersection(ZetStream... streams) {
			this(asList(streams));
		}

		@Override
		public long current() {
			return current;
		}

		@Override
		public long next() {
			if (current == next) hasNext();
			current = next;
			return current;
		}

		private long getNextValue() {
			long value = Long.MAX_VALUE;
			for (ZetStream zetStream : streams) {
				if (zetStream.current() > value || zetStream.current() == -1) continue;
				value = zetStream.current();
			}
			return value;
		}

		@Override
		public boolean hasNext() {
			if (current != next) return true;
			advanceStreamsWith(current);
			long value = getNextValue();
			if (value == Long.MAX_VALUE) {
				next = -1;
				return false;
			}
			while (!isValid(value)) {
				advanceStreamsWith(value);
				value = getNextValue();
				if (value == Long.MAX_VALUE) {
					next = -1;
					return false;
				}
			}
			next = value;
			return true;
		}

		private void advanceStreamsWith(long value) {
			for (ZetStream stream : streams) if (stream.current() == value) stream.next();
		}

		private boolean isValid(long value) {
			int freq = 0;
			for (ZetStream stream : streams) if (stream.current() == value) freq++;
			return freq == streams.size();
		}

	}

	class Union implements ZetStream {

		private final List<ZetStream> streams;
		private final int minFrequency;
		private final int maxFrequency;
		private final int recencyIndex;
		private long current = -1;
		private long next = -1;

		public Union(List<ZetStream> streams) {
			this.streams = streams;
			this.minFrequency = 0;
			this.maxFrequency = Integer.MAX_VALUE;
			this.recencyIndex = 0;
			streams.stream().filter(s -> s.current() == -1 && s.hasNext()).forEach(ZetStream::next);
		}

		public Union(ZetStream... streams) {
			this(asList(streams));
		}

		public Union(List<ZetStream> streams, int minFrequency, int maxFrequency, int recencyIndex) {
			this.streams = streams;
			this.minFrequency = minFrequency;
			this.maxFrequency = maxFrequency;
			this.recencyIndex = recencyIndex;
		}

		@Override
		public long current() {
			return current;
		}

		@Override
		public long next() {
			if (current == next) hasNext();
			current = next;
			return current;
		}

		private long getNextValue() {
			long value = Long.MAX_VALUE;
			for (ZetStream zetStream : streams) {
				if (zetStream.current() > value || zetStream.current() == -1) continue;
				value = zetStream.current();
			}
			return value;
		}

		@Override
		public boolean hasNext() {
			if (current != next) return true;
			advanceStreamsWith(current);
			long value = getNextValue();
			if (value == Long.MAX_VALUE) {
				next = -1;
				return false;
			}
			while (!isValid(value)) {
				advanceStreamsWith(value);
				value = getNextValue();
				if (value == Long.MAX_VALUE) {
					next = -1;
					return false;
				}
			}
			next = value;
			return true;
		}

		private void advanceStreamsWith(long value) {
			for (ZetStream stream : streams)
				while (stream.current() == value) {
					stream.next();
					if (stream.current() == -1) break;
				}
		}

		private boolean isValid(long value) {
			int freq = 0;
			int lastIndex = 0;
			for (int i = 0; i < streams.size(); i++) {
				if (streams.get(i).current() != value) continue;
				freq++;
				lastIndex = i;
			}
			return freq >= minFrequency && freq <= maxFrequency && lastIndex >= recencyIndex;
		}

	}
}
