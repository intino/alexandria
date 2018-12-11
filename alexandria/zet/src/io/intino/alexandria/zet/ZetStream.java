package io.intino.alexandria.zet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

public interface ZetStream {
	long current();

	long next();

	boolean hasNext();

	@SuppressWarnings({"WeakerAccess", "unused"})
	class Difference implements ZetStream {

		private final List<ZetStream> streams;
		private final ZetStream stream;
		private long next;
		private long current = -1;

		public Difference(List<ZetStream> streams) {
			this.stream = streams.size() > 0 ? streams.get(0) : null;
			this.streams = new ArrayList<>(streams.subList(1, streams.size()));
			this.streams.forEach(ZetStream::next);
			this.next = stream != null ? nextValue() : -1;
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
			current = next;
			next = nextValue();
			return current;
		}

		private long nextValue() {
			while (stream.hasNext()) {
				long value = stream.next();
				boolean ignore = false;
				for (ZetStream zetStream : streams) {
					while (zetStream.current() != -1 && zetStream.current() < value) zetStream.next();
					if (zetStream.current() == value) {
						ignore = true;
						break;
					}
				}
				if (!ignore) return value;
			}
			return -1;
		}

		@Override
		public boolean hasNext() {
			return this.next != -1;
		}
	}

	@SuppressWarnings({"WeakerAccess", "unused"})
	class SymmetricDifference implements ZetStream {
		private final List<ZetStream> streams;
		private long next;
		private long current;

		public SymmetricDifference(List<ZetStream> streams) {
			this.streams = streams;
			this.streams.forEach(ZetStream::next);
			this.next = nextValue();
			this.current = -1;
		}

		public SymmetricDifference(ZetStream... streams) {
			this(asList(streams));
		}

		@Override
		public long current() {
			return current;
		}

		@Override
		public long next() {
			this.current = this.next;
			this.next = nextValue();
			return this.current;
		}

		private long nextValue() {
			boolean duplicated;
			int index;
			long min;
			do {
				index = -1;
				min = Long.MAX_VALUE;
				duplicated = false;
				for (int i = 0; i < streams.size(); i++) {
					ZetStream stream = streams.get(i);
					if (stream.current() == -1 || stream.current() > min) continue;
					if (stream.current() < min) {
						min = stream.current();
						index = i;
					} else {
						duplicated = true;
						advance(index);
						advance(i);
						for (int j = i + 1; j < streams.size(); j++)
							if (streams.get(j).current() == min) advance(j);
						break;
					}
				}
				if (index < 0) return -1;
			} while (duplicated);
			advance(index);
			return min;
		}

		private void advance(int i) {
			if (streams.get(i).hasNext()) streams.get(i).next();
			else streams.set(i, Empty.instance);
		}

		@Override
		public boolean hasNext() {
			return next != -1;
		}
	}

	@SuppressWarnings({"WeakerAccess", "unused"})
	class Intersection implements ZetStream {

		private final List<ZetStream> streams;
		private long next;
		private long current;

		public Intersection(List<ZetStream> streams) {
			this.streams = streams;
			this.next = nextValue(advancing());
			this.current = 0;
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
			this.current = this.next;
			this.next = nextValue(advancing());
			return this.current;
		}

		private long advancing() {
			long max = Long.MIN_VALUE;
			for (ZetStream stream : streams) {
				if (!stream.hasNext()) return Long.MAX_VALUE;
				max = Math.max(max, stream.next());
			}
			return max;
		}


		private long nextValue(long max) {
			if (max == Long.MAX_VALUE) return -1;
			for (int i = 0; i < streams.size(); i++) {
				ZetStream stream = streams.get(i);
				while (stream.current() < max) {
					if (stream.current() == -1 || !stream.hasNext()) return -1;
					stream.next();
				}
				if (stream.current() == max) continue;
				max = stream.current();
				i = -1;
			}
			return max;
		}


		@Override
		public boolean hasNext() {
			return next != -1;
		}
	}

	@SuppressWarnings("WeakerAccess")
	class Union implements ZetStream {
		private final List<ZetStream> streams;
		private final int minFrequency;
		private final int maxFrequency;
		private final boolean consecutive;
		private long current;
		private long next;

		public Union(List<ZetStream> streams) {
			this(streams, 1, Integer.MAX_VALUE, false);
		}

		public Union(ZetStream... streams) {
			this(asList(streams));
		}

		public Union(List<ZetStream> streams, int minFrequency, int maxFrequency, boolean consecutive) {
			this.streams = streams;
			this.minFrequency = minFrequency;
			this.maxFrequency = maxFrequency;
			this.consecutive = consecutive;
			this.streams.forEach(ZetStream::next);
			this.next = nextValue();
			this.current = 0;
		}

		@Override
		public long current() {
			return current;
		}

		@Override
		public long next() {
			this.current = this.next;
			this.next = nextValue();
			return this.current;
		}

		@Override
		public boolean hasNext() {
			return next != -1;
		}

		private long nextValue() {
			long min;
			do {
				min = Long.MAX_VALUE;
				int count = 0;
				for (ZetStream zetStream : streams) {
					if (zetStream.current() == -1) continue;
					if (zetStream.current() < min) {
						min = zetStream.current();
						count = 1;
					} else if (zetStream.current() == min) count++;
					else if (consecutive) count = 0;
				}
				if (min == Long.MAX_VALUE) return -1;
				advanceStreamsWith(min);
				if ((count >= minFrequency) && (count <= maxFrequency)) break;
			} while (true);
			return min;
		}

		private void advanceStreamsWith(long value) {
			int i = -1;
			for (ZetStream stream : streams) {
				i++;
				if (stream.current() != value) continue;
				if (stream.hasNext())
					stream.next();
				else
					streams.set(i, Empty.instance);
			}
		}
	}


	class Merge implements ZetStream {
		private final List<ZetStream> streams;
		private long current;
		private long next;

		public Merge(List<ZetStream> streams) {
			this.streams = streams;
			this.current = -1;
			this.streams.forEach(ZetStream::next);
			this.next = nextValue();
		}

		public Merge(ZetStream... streams) {
			this(Arrays.asList(streams));
		}

		public long current() {
			return this.current;
		}

		public long next() {
			this.current = next;
			this.next = nextValue();
			return this.current;
		}

		private long nextValue() {
			int index = -1;
			long min = Long.MAX_VALUE;
			for (int i = 0; i < streams.size(); i++) {
				ZetStream stream = streams.get(i);
				if (stream.current() == -1 || stream.current() > min) continue;
				min = stream.current();
				index = i;
			}
			if (index < 0) return -1;
			streams.get(index).next();
			return min;
		}

		public boolean hasNext() {
			return next != -1;
		}
	}

	class Empty implements ZetStream {
		public static final ZetStream instance = new Empty();

		@Override
		public long current() {
			return -1;
		}

		@Override
		public long next() {
			return -1;
		}

		@Override
		public boolean hasNext() {
			return false;
		}
	}


}
