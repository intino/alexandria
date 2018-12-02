package io.intino.alexandria.zet;

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

	@SuppressWarnings({"WeakerAccess", "unused"})
	class SymmetricDifference implements ZetStream {

		private final List<ZetStream> streams;
		private long current = -1;
		private long next = -1;

		public SymmetricDifference(List<ZetStream> streams) {
			this.streams = streams;
			streams.stream().filter(s -> s.current() == -1 && s.hasNext()).forEach(ZetStream::next);
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
			return streams.stream().filter(s -> s.current() == value).count() == 1;
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
			for (ZetStream stream : streams)
				max = Math.max(max, stream.hasNext() ? stream.next() : -1);
			return max;
		}


		private long nextValue(long max) {
			for (int i = 0; i < streams.size(); i++) {
				ZetStream stream = streams.get(i);
				while (stream.current() < max) {
					if (!stream.hasNext()) return -1;
					stream.next();
				}
				if (stream.current() == max) continue;
				max = stream.current();
				i = 0;
			}
			return max;
		}


		@Override
		public boolean hasNext() {
			return next != -1;
		}


	}

	@SuppressWarnings("WeakerAccess")
			//FIXME
	class Union implements ZetStream {

		private final List<ZetStream> streams;
		private final int minFrequency;
		private final int maxFrequency;
		private final boolean consecutives;
		private long current = -1;
		private long next = -1;

		public Union(List<ZetStream> streams) {
			this.streams = streams;
			this.minFrequency = 0;
			this.maxFrequency = Integer.MAX_VALUE;
			this.consecutives = false;
			streams.stream().filter(s -> s.current() == -1 && s.hasNext()).forEach(ZetStream::next);
		}

		public Union(ZetStream... streams) {
			this(asList(streams));
		}

		public Union(List<ZetStream> streams, int minFrequency, int maxFrequency, boolean consecutives) {
			this.streams = streams;
			this.minFrequency = minFrequency;
			this.maxFrequency = maxFrequency;
			this.consecutives = consecutives;
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

		private long getNextValue() {
			long value = Long.MAX_VALUE;
			for (ZetStream zetStream : streams) {
				if (zetStream.current() > value || zetStream.current() == -1) continue;
				value = zetStream.current();
			}
			return value;
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
			int consecutives = 0;
			int maxConsecutives = 0;
			for (ZetStream stream : streams) {
				if (stream.current() != value) {
					maxConsecutives = consecutives;
					consecutives = 0;
				} else {
					freq++;
					consecutives++;
				}
			}
			maxConsecutives = Math.max(maxConsecutives, consecutives);
			return this.consecutives ?
					maxConsecutives >= maxFrequency :
					freq >= minFrequency && freq <= maxFrequency;
		}
	}


	class Join implements ZetStream {
		private final List<ZetStream> streams;
		private long current;

		public Join(List<ZetStream> streams) {
			this.streams = streams;
			this.current = -1;
			this.streams.stream().forEach(ZetStream::next);
		}

		public Join(ZetStream... streams) {
			this(Arrays.asList(streams));
		}

		public long current() {
			return this.current;
		}

		public long next() {
			return this.current = getNextValue();
		}

		private long getNextValue() {
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
			for (ZetStream stream : streams)
				if (stream.hasNext()) return true;
			return false;
		}

	}
}
