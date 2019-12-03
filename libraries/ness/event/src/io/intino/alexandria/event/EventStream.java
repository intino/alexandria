package io.intino.alexandria.event;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;

public interface EventStream {

	@SuppressWarnings("unused")
	Event current();

	Event next();

	boolean hasNext();

	default void forEachRemaining(Consumer<Event> action) {
		Objects.requireNonNull(action);
		while (hasNext())
			action.accept(next());
	}

	@SuppressWarnings("unused")
	class Merge implements EventStream {
		private Event currentEvent;
		private EventStream[] inputs;
		private Event[] current;

		public Merge(EventStream... inputs) {
			this.inputs = inputs;
			this.current = stream(inputs).map(this::next).toArray(Event[]::new);
		}

		public static Merge of(EventStream... inputs) {
			return new Merge(inputs);
		}

		@Override
		public Event current() {
			return currentEvent;
		}

		@Override
		public Event next() {
			return currentEvent = next(minIndex());
		}

		private Event next(int index) {
			Event message = current[index];
			current[index] = next(inputs[index]);
			return message;
		}

		@Override
		public boolean hasNext() {
			return !stream(current).allMatch(Objects::isNull);
		}

		private Event next(EventStream input) {
			return input.hasNext() ? input.next() : null;
		}

		private int minIndex() {
			return range(0, current.length).boxed().min(this::comparingTimestamp).orElse(-1);
		}

		private int comparingTimestamp(int a, int b) {
			return Long.compare(tsOf(a), tsOf(b));
		}

		private long tsOf(int i) {
			return current[i] != null ? current[i].ts().toEpochMilli() : Long.MAX_VALUE;
		}

	}

	public class Sequence implements EventStream {
		private final Iterator<EventStream> iterator;
		private Event currentEvent;
		private EventStream current;

		public Sequence(EventStream... inputs) {
			this.iterator = streamOf(inputs).iterator();
			this.current = this.iterator.next();
		}

		public static Sequence of(EventStream... inputs) {
			return new Sequence(inputs);
		}

		private Stream<EventStream> streamOf(EventStream[] inputs) {
			return stream(isEmpty(inputs) ? inputs : emptyInput());
		}

		private boolean isEmpty(EventStream[] inputs) {
			return inputs.length > 0;
		}

		private EventStream[] emptyInput() {
			return new EventStream[]{new Empty()};
		}

		@Override
		public Event current() {
			return currentEvent;
		}

		@Override
		public Event next() {
			return currentEvent = current.next();
		}

		@Override
		public boolean hasNext() {
			return current.hasNext() || restHasNext();
		}

		private boolean restHasNext() {
			while (iterator.hasNext()) {
				current = iterator.next();
				if (current.hasNext()) return true;
			}
			return false;
		}

	}

	class Empty implements EventStream {

		@Override
		public Event current() {
			return null;
		}

		@Override
		public Event next() {
			return null;
		}

		@Override
		public boolean hasNext() {
			return false;
		}

	}

}
