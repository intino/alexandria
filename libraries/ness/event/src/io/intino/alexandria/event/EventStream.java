package io.intino.alexandria.event;

import io.intino.alexandria.event.message.MessageEvent;

import java.time.Instant;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;

public interface EventStream {

	@SuppressWarnings("unused")
	MessageEvent current();

	MessageEvent next();

	boolean hasNext();

	default void forEachRemaining(Consumer<MessageEvent> action) {
		Objects.requireNonNull(action);
		while (hasNext())
			action.accept(next());
	}

	@SuppressWarnings("unused")
	class Merge implements EventStream {
		private MessageEvent currentEvent;
		private final EventStream[] inputs;
		private final MessageEvent[] current;

		public Merge(EventStream... inputs) {
			this.inputs = inputs;
			this.current = stream(inputs).map(this::next).toArray(MessageEvent[]::new);
		}

		public static Merge of(EventStream... inputs) {
			return new Merge(inputs);
		}

		@Override
		public MessageEvent current() {
			return currentEvent;
		}

		@Override
		public MessageEvent next() {
			return currentEvent = next(minIndex());
		}

		private MessageEvent next(int index) {
			MessageEvent message = current[index];
			current[index] = next(inputs[index]);
			return message;
		}

		@Override
		public boolean hasNext() {
			return !stream(current).allMatch(Objects::isNull);
		}

		private MessageEvent next(EventStream input) {
			return input.hasNext() ? input.next() : null;
		}

		private int minIndex() {
			return range(0, current.length).boxed().min(this::comparingTimestamp).orElse(-1);
		}

		private int comparingTimestamp(int a, int b) {
			return tsOf(current[a]).compareTo(tsOf(current[b]));
		}

		private Instant tsOf(MessageEvent event) {
			return event != null ? event.ts() : Instant.MAX;
		}
	}

	class Sequence implements EventStream {
		private final Iterator<EventStream> iterator;
		private MessageEvent currentEvent;
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
		public MessageEvent current() {
			return currentEvent;
		}

		@Override
		public MessageEvent next() {
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
		public MessageEvent current() {
			return null;
		}

		@Override
		public MessageEvent next() {
			return null;
		}

		@Override
		public boolean hasNext() {
			return false;
		}
	}
}
