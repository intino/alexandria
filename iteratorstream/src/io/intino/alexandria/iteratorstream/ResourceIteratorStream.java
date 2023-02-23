package io.intino.alexandria.iteratorstream;

import io.intino.alexandria.resourcecleaner.DisposableResource;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static io.intino.alexandria.resourcecleaner.DisposableResource.whenDestroyed;
import static java.util.Objects.requireNonNull;
import static java.util.Spliterator.*;

public class ResourceIteratorStream<T> implements Stream<T>, Iterator<T>, Iterable<T>, AutoCloseable {

	protected final Iterator<T> iterator;
	protected final DisposableResource resource;

	public ResourceIteratorStream(Iterator<T> iterator) {
		this.iterator = requireNonNull(iterator);
		this.resource = createAutoDisposableResource(iterator);
	}

	protected DisposableResource createAutoDisposableResource(Iterator<T> iterator) {
		return whenDestroyed(this).thenClose(iterator);
	}

	protected Stream<T> asJavaStream() {
		return StreamSupport.stream(spliterator(), false);
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public T next() {
		return iterator.next();
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		try {
			while(hasNext()) action.accept(next());
		} finally {
			close();
		}
	}

	@Override
	public Stream<T> onClose(Runnable closeHandler) {
		if(closeHandler != null) resource.addCloseHandler(closeHandler);
		return this;
	}

	@Override
	public void close() {
		resource.close();
	}

	@Override
	public Stream<T> filter(Predicate<? super T> predicate) {
		return asJavaStream().filter(predicate);
	}

	@Override
	public <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
		return asJavaStream().map(mapper);
	}

	@Override
	public IntStream mapToInt(ToIntFunction<? super T> mapper) {
		return asJavaStream().mapToInt(mapper);
	}

	@Override
	public LongStream mapToLong(ToLongFunction<? super T> mapper) {
		return asJavaStream().mapToLong(mapper);
	}

	@Override
	public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
		return asJavaStream().mapToDouble(mapper);
	}

	@Override
	public <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
		return asJavaStream().flatMap(mapper);
	}

	@Override
	public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
		return asJavaStream().flatMapToInt(mapper);
	}

	@Override
	public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
		return asJavaStream().flatMapToLong(mapper);
	}

	@Override
	public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
		return asJavaStream().flatMapToDouble(mapper);
	}

	@Override
	public Stream<T> distinct() {
		return asJavaStream().distinct();
	}

	@Override
	public Stream<T> sorted() {
		return asJavaStream().sorted();
	}

	@Override
	public Stream<T> sorted(Comparator<? super T> comparator) {
		return asJavaStream().sorted(comparator);
	}

	@Override
	public Stream<T> peek(Consumer<? super T> action) {
		return asJavaStream().peek(action);
	}

	@Override
	public Stream<T> limit(long maxSize) {
		return asJavaStream().limit(maxSize);
	}

	@Override
	public Stream<T> skip(long n) {
		return asJavaStream().skip(n);
	}

	@Override
	public void forEachOrdered(Consumer<? super T> action) {
		sorted().forEach(action);
	}

	@Override
	public Object[] toArray() {
		return asJavaStream().toArray();
	}

	@Override
	public <A> A[] toArray(IntFunction<A[]> generator) {
		return asJavaStream().toArray(generator);
	}

	@Override
	public T reduce(T identity, BinaryOperator<T> accumulator) {
		return asJavaStream().reduce(identity, accumulator);
	}

	@Override
	public Optional<T> reduce(BinaryOperator<T> accumulator) {
		return asJavaStream().reduce(accumulator);
	}

	@Override
	public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
		return asJavaStream().reduce(identity, accumulator, combiner);
	}

	@Override
	public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
		return asJavaStream().collect(supplier, accumulator, combiner);
	}

	@Override
	public <R, A> R collect(Collector<? super T, A, R> collector) {
		return asJavaStream().collect(collector);
	}

	@Override
	public Optional<T> min(Comparator<? super T> comparator) {
		return asJavaStream().min(comparator);
	}

	@Override
	public Optional<T> max(Comparator<? super T> comparator) {
		return asJavaStream().max(comparator);
	}

	@Override
	public long count() {
		return asJavaStream().count();
	}

	@Override
	public boolean anyMatch(Predicate<? super T> predicate) {
		return asJavaStream().anyMatch(predicate);
	}

	@Override
	public boolean allMatch(Predicate<? super T> predicate) {
		return asJavaStream().allMatch(predicate);
	}

	@Override
	public boolean noneMatch(Predicate<? super T> predicate) {
		return asJavaStream().noneMatch(predicate);
	}

	@Override
	public Optional<T> findFirst() {
		return asJavaStream().findFirst();
	}

	@Override
	public Optional<T> findAny() {
		return asJavaStream().findAny();
	}

	@Override
	public Spliterator<T> spliterator() {
		return Spliterators.spliteratorUnknownSize(iterator(), SORTED | ORDERED | NONNULL | IMMUTABLE);
	}

	@Override
	public boolean isParallel() {
		return false;
	}

	@Override
	public Stream<T> sequential() {
		return asJavaStream().sequential();
	}

	@Override
	public Stream<T> parallel() {
		return asJavaStream().parallel();
	}

	@Override
	public Stream<T> unordered() {
		return asJavaStream().unordered();
	}
}
