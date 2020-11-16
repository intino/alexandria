package io.intino.alexandria.led;

import io.intino.alexandria.led.allocators.TransactionAllocator;
import io.intino.alexandria.led.allocators.TransactionFactory;
import io.intino.alexandria.led.allocators.stack.StackAllocators;
import io.intino.alexandria.led.allocators.stack.StackListAllocator;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.leds.IteratorLedStream;
import io.intino.alexandria.led.util.iterators.IteratorUtils;
import io.intino.alexandria.led.util.iterators.MergedIterator;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.intino.alexandria.led.Transaction.idOf;
import static io.intino.alexandria.led.Transaction.sizeOf;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public interface LedStream<T extends Transaction> extends Iterator<T>, AutoCloseable {

	static <T extends Transaction> LedStream<T> empty() {
		return IteratorLedStream.fromStream(0, Stream.empty());
	}

	static<T extends Transaction> LedStream<T> fromLed(Led<T> led) {
		return led.toLedStream();
	}

	static <T extends Transaction> LedStream<T> fromStream(int transactionSize, Stream<T> stream) {
		return IteratorLedStream.fromStream(transactionSize, stream);
	}

	static <T extends Transaction> LedStream<T> of(int transactionSize, T... transactions) {
		return fromStream(transactionSize, Arrays.stream(transactions));
	}

	static <T extends Transaction> LedStream<T> singleton(int transactionSize, T transaction) {
		return fromStream(transactionSize, Stream.of(transaction));
	}

	static <T extends Transaction> LedStream<T> merged(Stream<LedStream<T>> ledStreams) {
		return merged(ledStreams.iterator());
	}

	static <T extends Transaction> LedStream<T> merged(Iterator<LedStream<T>> iterator) {
		if(!iterator.hasNext()) {
			return empty();
		}
		return iterator.next().merge(IteratorUtils.streamOf(iterator));
	}

	static <T extends Transaction> Builder<T> builder(Class<T> transactionClass) {
		return new LedStreamBuilder<>(transactionClass);
	}

	static <T extends Transaction> Builder<T> builder(Class<T> transactionClass, int numElementsPerBlock) {
		return new LedStreamBuilder<>(transactionClass, numElementsPerBlock);
	}

	static <T extends Transaction> Builder<T> builder(Class<T> transactionClass, TransactionFactory<T> factory) {
		return new LedStreamBuilder<>(transactionClass, factory);
	}

	static <T extends Transaction> Builder<T> builder(Class<T> transactionClass,
													  TransactionFactory<T> factory, int numElementsPerBlock) {
		return new LedStreamBuilder<>(transactionClass, factory, numElementsPerBlock);
	}

	int transactionSize();

	@Override
	boolean hasNext();

	@Override
	T next();

	default Spliterator<T> spliterator() {
		return Spliterators.spliteratorUnknownSize(this, Spliterator.SORTED);
	}

	default Stream<T> asJavaStream() {
		return StreamSupport.stream(spliterator(), false);
	}

	default LedStream<T> filter(Predicate<T> condition) {
		return new LedStream.Filter<>(this, condition);
	}

	default LedStream<T> peek(Consumer<T> consumer) {
		return new LedStream.Peek<>(this, consumer);
	}

	default <R extends Transaction> LedStream<R> map(TransactionAllocator<R> allocator, BiConsumer<T, R> mapper) {
		return new LedStream.Map<>(this, allocator, mapper);
	}

	default <R extends Transaction> LedStream<R> map(int rSize, TransactionFactory<R> factory, BiConsumer<T, R> mapper) {
		return new LedStream.Map<>(this, rSize, factory, mapper);
	}

	default <R extends Transaction> LedStream<R> map(Class<R> newType, BiConsumer<T, R> mapper) {
		return new LedStream.Map<>(this, sizeOf(newType), Transaction.factoryOf(newType), mapper);
	}

	default <R> Stream<R> mapToObj(Function<T, R> mapper) {
		return asJavaStream().map(mapper);
	}

	default LedStream<T> merge(LedStream<T> other) {
		return new LedStream.Merge<>(this, other);
	}

	default LedStream<T> merge(Stream<LedStream<T>> others) {
		return new LedStream.Merge<>(this, others);
	}

	default <O extends Transaction> LedStream<T> removeAll(LedStream<O> other) {
		return new LedStream.RemoveAll<>(this, other);
	}

	default LedStream<T> removeAll(Iterable<Long> other) {
		return new LedStream.RemoveAll<>(this, other.iterator());
	}

	default LedStream<T> removeAll(Iterator<Long> other) {
		return new LedStream.RemoveAll<>(this, other);
	}

	default <O extends Transaction> LedStream<T> retainAll(LedStream<O> other) {
		return new LedStream.RetainAll<>(this, other);
	}

	default LedStream<T> retainAll(Iterable<Long> other) {
		return new LedStream.RetainAll<>(this, other.iterator());
	}

	default LedStream<T> retainAll(Iterator<Long> other) {
		return new LedStream.RetainAll<>(this, other);
	}

	default Optional<T> findFirst() {
		return hasNext() ? Optional.ofNullable(next()) : Optional.empty();
	}

	default Optional<T> findLast() {
		T last = null;
		while(hasNext()) {
			last = next();
		}
		return Optional.ofNullable(last);
	}

	default boolean allMatch(Predicate<T> condition) {
		while(hasNext()) {
			if(!condition.test(next())) {
				return false;
			}
		}
		return true;
	}

	default boolean anyMatch(Predicate<T> condition) {
		while(hasNext()) {
			if(condition.test(next())) {
				return true;
			}
		}
		return false;
	}

	default boolean noneMatch(Predicate<T> condition) {
		while(hasNext()) {
			if(condition.test(next())) {
				return false;
			}
		}
		return true;
	}

	default void forEach(Consumer<T> consumer) {
		while(hasNext()) {
			consumer.accept(next());
		}
	}

	default void serialize(File file) {
		LedWriter ledWriter = new LedWriter(file);
		ledWriter.write(this);
	}

	default LedStream<T> onClose(Runnable onClose) {
		return this;
	}

	abstract class LedStreamOperation<T extends Transaction, R extends Transaction> implements LedStream<R> {

		protected final LedStream<T> source;
		private Runnable onClose;
		private boolean closed;

		public LedStreamOperation(LedStream<T> source) {
			this.source = requireNonNull(source);
		}

		@Override
		public int transactionSize() {
			return source.transactionSize();
		}

		@Override
		public LedStream<R> onClose(Runnable onClose) {
			this.onClose = onClose;
			return this;
		}

		@Override
		public void close() throws Exception {
			if(closed) {
				return;
			}
			if(onClose != null) {
				onClose.run();
			}
			source.close();
			closed = true;
		}
	}

	class Filter<T extends Transaction> extends LedStream.LedStreamOperation<T, T> {

		private final Predicate<T> condition;
		private T current;

		public Filter(LedStream<T> source, Predicate<T> condition) {
			super(source);
			this.condition = requireNonNull(condition);
		}

		@Override
		public boolean hasNext() {
			if(current == null) {
				advanceToNextElement();
			}
			return current != null;
		}

		private void advanceToNextElement() {
			while(source.hasNext()) {
				final T next = source.next();
				if(condition.test(next)) {
					current = next;
					break;
				}
			}
		}

		@Override
		public T next() {
			if(!hasNext())
				throw new NoSuchElementException();
			final T next = current;
			current = null;
			return next;
		}
	}

	class Peek<T extends Transaction> extends LedStream.LedStreamOperation<T, T> {

		private final Consumer<T> consumer;

		public Peek(LedStream<T> source, Consumer<T> consumer) {
			super(source);
			this.consumer = requireNonNull(consumer);
		}

		@Override
		public boolean hasNext() {
			return source.hasNext();
		}

		@Override
		public T next() {
			final T next = source.next();
			consumer.accept(next);
			return next;
		}
	}

	class RemoveAll<T extends Transaction> extends LedStream.LedStreamOperation<T, T> {

		private final Iterator<Long> other;
		private T sourceCurrent;
		private Long otherCurrentId;

		public RemoveAll(LedStream<T> source, LedStream<?> other) {
			this(source, other.mapToObj(Transaction::idOf).iterator());
		}

		public RemoveAll(LedStream<T> source, Iterator<Long> idIterator) {
			super(source);
			this.other = requireNonNull(idIterator);
		}

		@Override
		public boolean hasNext() {
			if(sourceCurrent == null) {
				advanceToNextElement();
			}
			return sourceCurrent != null;
		}

		private void advanceToNextElement() {
			if(!source.hasNext()) {
				return;
			}
			if(!other.hasNext()) {
				sourceCurrent = source.next();
				otherCurrentId = null;
				return;
			}

			T sourceElement = source.next();
			if(otherCurrentId == null) {
				otherCurrentId = other.next();
			}

			if(idOf(sourceElement) < otherCurrentId) {
				sourceCurrent = sourceElement;
				return;
			}
			while(idOf(sourceElement) > otherCurrentId) {
				if(other.hasNext()) {
					otherCurrentId = other.next();
				} else {
					sourceCurrent = sourceElement;
					otherCurrentId = null;
					return;
				}
			}

			do {
				long id = idOf(sourceElement);
				final int comparison = Long.compare(id, otherCurrentId);
				if(comparison == 0) {
					if(source.hasNext()) {
						sourceElement = source.next();
					} else {
						sourceCurrent = null;
						return;
					}
				} else if(comparison > 0) {
					if(other.hasNext()) {
						otherCurrentId = other.next();
					} else {
						otherCurrentId = null;
						break;
					}
				}

			} while(idOf(sourceElement) >= otherCurrentId);

			sourceCurrent = sourceElement;
		}

		@Override
		public T next() {
			if(!hasNext())
				throw new NoSuchElementException();
			final T next = sourceCurrent;
			sourceCurrent = null;
			return next;
		}
	}

	class Merge<T extends Transaction> extends LedStream.LedStreamOperation<T, T> {

		private final MergedIterator<T> mergedIterator;

		public Merge(LedStream<T> source, LedStream<T> other) {
			super(source);
			mergedIterator = new MergedIterator<>(Stream.of(source, requireNonNull(other)), Comparator.comparingLong(Transaction::idOf));
		}

		public Merge(LedStream<T> source, Stream<LedStream<T>> others) {
			super(source);
			mergedIterator = new MergedIterator<>(Stream.concat(Stream.of(source), others), Comparator.comparingLong(Transaction::idOf));
		}

		@Override
		public boolean hasNext() {
			return mergedIterator.hasNext();
		}

		@Override
		public T next() {
			return mergedIterator.next();
		}
	}

	class RetainAll<T extends Transaction> extends LedStream.LedStreamOperation<T, T> {

		private final Iterator<Long> other;
		private T current;

		public RetainAll(LedStream<T> source, LedStream<?> other) {
			this(source, other.mapToObj(Transaction::idOf).iterator());
		}

		public RetainAll(LedStream<T> source, Iterator<Long> idIterator) {
			super(source);
			this.other = requireNonNull(idIterator);
		}

		@Override
		public boolean hasNext() {
			if(current == null) {
				advanceToNextElement();
			}
			return current != null;
		}

		private void advanceToNextElement() {
			if(!source.hasNext() || !other.hasNext()) {
				return;
			}

			T sourceElement = source.next();
			long otherElementId = other.next();

			while(idOf(sourceElement) < otherElementId) {
				if(!source.hasNext()) {
					return;
				}
				sourceElement = source.next();
			}
			while(idOf(sourceElement) > otherElementId) {
				if(!other.hasNext()) {
					return;
				}
				otherElementId = other.next();
			}
			if(idOf(sourceElement) == otherElementId) {
				current = sourceElement;
			}
		}

		@Override
		public T next() {
			if(!hasNext())
				throw new NoSuchElementException();
			final T next = current;
			current = null;
			return next;
		}
	}

	class Map<T extends Transaction, R extends Transaction> extends LedStream.LedStreamOperation<T, R> {

		private static final int DEFAULT_ELEMENTS_PER_STACK = 1024;


		private final TransactionAllocator<R> allocator;
		private final BiConsumer<T, R> mapper;

		public Map(LedStream<T> source, TransactionAllocator<R> allocator, BiConsumer<T, R> mapper) {
			super(source);
			this.allocator = requireNonNull(allocator);
			this.mapper = requireNonNull(mapper);
		}

		public Map(LedStream<T> source, int rSize, TransactionFactory<R> factory, BiConsumer<T, R> mapper) {
			this(source, getDefaultAllocator(rSize, factory), mapper);
		}

		@Override
		public int transactionSize() {
			return allocator.transactionSize();
		}

		@Override
		public boolean hasNext() {
			return source.hasNext();
		}

		@Override
		public R next() {
			final R newElement = allocator.calloc();
			mapper.accept(source.next(), newElement);
			return newElement;
		}

		private static <R extends Transaction> TransactionAllocator<R> getDefaultAllocator(int rSize, TransactionFactory<R> factory) {
			return new StackListAllocator<>(DEFAULT_ELEMENTS_PER_STACK, rSize, factory, StackAllocators::newManaged);
		}
	}

	interface Builder<T extends Transaction> {

		Class<T> transactionClass();

		int transactionSize();

		Builder<T> create(Consumer<T> initializer);

		LedStream<T> build();
	}
}