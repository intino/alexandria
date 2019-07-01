package io.intino.alexandria.zim;

import io.intino.alexandria.inl.Event;
import io.intino.alexandria.inl.Message;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;

public interface ZimStream {

	@SuppressWarnings("unused")
	Message current();

	Message next();

	boolean hasNext();

	default void forEachRemaining(Consumer<Message> action) {
		Objects.requireNonNull(action);
		while (hasNext())
			action.accept(next());
	}

	@SuppressWarnings("unused")
	class Merge implements ZimStream {
		private Message currentMessage;
		private ZimStream[] inputs;
		private Event[] current;

		public Merge(ZimStream... inputs) {
			this.inputs = inputs;
			this.current = stream(inputs).map(this::next).toArray(Event[]::new);
		}

		public static Merge of(ZimStream... inputs) {
			return new Merge(inputs);
		}

		@Override
		public Message current() {
			return currentMessage;
		}

		@Override
		public Message next() {
			return currentMessage = next(minIndex());
		}

		private Message next(int index) {
			Message message = current[index];
			current[index] = next(inputs[index]);
			return message;
		}

		@Override
		public boolean hasNext() {
			return !stream(current).allMatch(Objects::isNull);
		}

		private Event next(ZimStream input) {
			return input.hasNext() ? input.next().asEvent() : null;
		}

		private int minIndex() {
			return range(0, current.length).boxed().min(this::comparingTimestamp).orElse(-1);
		}

		private int comparingTimestamp(int a, int b) {
			return Long.compare(tsOf(a), tsOf(b));
		}

		private long tsOf(int i) {
			return current[i] != null ? current[i].instant().toEpochMilli() : Long.MAX_VALUE;
		}

	}

	class Sequence implements ZimStream {
		private final Iterator<ZimStream> iterator;
		private Message currentMessage;
		private ZimStream current;

		private Sequence(ZimStream... inputs) {
			this.iterator = streamOf(inputs).iterator();
			this.current = this.iterator.next();
		}

		public static Sequence of(ZimStream... inputs) {
			return new Sequence(inputs);
		}

		private Stream<ZimStream> streamOf(ZimStream[] inputs) {
			return stream(isEmpty(inputs) ? inputs : emptyInput());
		}

		private boolean isEmpty(ZimStream[] inputs) {
			return inputs.length > 0;
		}

		private ZimStream[] emptyInput() {
			return new ZimStream[]{new Empty()};
		}

		@Override
		public Message current() {
			return currentMessage;
		}

		@Override
		public Message next() {
			return currentMessage = current.next();
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

	class Empty implements ZimStream {

		@Override
		public Message current() {
			return null;
		}

		@Override
		public Message next() {
			return null;
		}

		@Override
		public boolean hasNext() {
			return false;
		}

	}

}
