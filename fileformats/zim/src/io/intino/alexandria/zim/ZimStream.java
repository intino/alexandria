package io.intino.alexandria.zim;

import io.intino.alexandria.message.Message;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

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

	class Sequence implements ZimStream {
		private final Iterator<ZimStream> iterator;
		private Message currentMessage;
		private ZimStream current;

		Sequence(ZimStream... inputs) {
			this.iterator = streamOf(inputs).iterator();
			this.current = this.iterator.next();
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

		public static Sequence of(ZimStream... inputs) {
			return new Sequence(inputs);
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
