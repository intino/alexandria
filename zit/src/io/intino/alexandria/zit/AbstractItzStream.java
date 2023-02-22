package io.intino.alexandria.zit;

import io.intino.alexandria.zit.model.Data;

import java.util.Comparator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.*;
import java.util.stream.*;

import static java.util.Spliterator.*;

abstract class AbstractItzStream implements Stream<Data> {

	protected Stream<Data> asJavaStream() {
		return StreamSupport.stream(spliterator(), false);
	}

	@Override
	public Stream<Data> filter(Predicate<? super Data> predicate) {
		return asJavaStream().filter(predicate);
	}

	@Override
	public <R> Stream<R> map(Function<? super Data, ? extends R> mapper) {
		return asJavaStream().map(mapper);
	}

	@Override
	public IntStream mapToInt(ToIntFunction<? super Data> mapper) {
		return asJavaStream().mapToInt(mapper);
	}

	@Override
	public LongStream mapToLong(ToLongFunction<? super Data> mapper) {
		return asJavaStream().mapToLong(mapper);
	}

	@Override
	public DoubleStream mapToDouble(ToDoubleFunction<? super Data> mapper) {
		return asJavaStream().mapToDouble(mapper);
	}

	@Override
	public <R> Stream<R> flatMap(Function<? super Data, ? extends Stream<? extends R>> mapper) {
		return asJavaStream().flatMap(mapper);
	}

	@Override
	public IntStream flatMapToInt(Function<? super Data, ? extends IntStream> mapper) {
		return asJavaStream().flatMapToInt(mapper);
	}

	@Override
	public LongStream flatMapToLong(Function<? super Data, ? extends LongStream> mapper) {
		return asJavaStream().flatMapToLong(mapper);
	}

	@Override
	public DoubleStream flatMapToDouble(Function<? super Data, ? extends DoubleStream> mapper) {
		return asJavaStream().flatMapToDouble(mapper);
	}

	@Override
	public Stream<Data> distinct() {
		return asJavaStream().distinct();
	}

	@Override
	public Stream<Data> sorted() {
		return asJavaStream().sorted();
	}

	@Override
	public Stream<Data> sorted(Comparator<? super Data> comparator) {
		return asJavaStream().sorted(comparator);
	}

	@Override
	public Stream<Data> peek(Consumer<? super Data> action) {
		return asJavaStream().peek(action);
	}

	@Override
	public Stream<Data> limit(long maxSize) {
		return asJavaStream().limit(maxSize);
	}

	@Override
	public Stream<Data> skip(long n) {
		return asJavaStream().skip(n);
	}

	@Override
	public void forEachOrdered(Consumer<? super Data> action) {
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
	public Data reduce(Data identity, BinaryOperator<Data> accumulator) {
		return asJavaStream().reduce(identity, accumulator);
	}

	@Override
	public Optional<Data> reduce(BinaryOperator<Data> accumulator) {
		return asJavaStream().reduce(accumulator);
	}

	@Override
	public <U> U reduce(U identity, BiFunction<U, ? super Data, U> accumulator, BinaryOperator<U> combiner) {
		return asJavaStream().reduce(identity, accumulator, combiner);
	}

	@Override
	public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super Data> accumulator, BiConsumer<R, R> combiner) {
		return asJavaStream().collect(supplier, accumulator, combiner);
	}

	@Override
	public <R, A> R collect(Collector<? super Data, A, R> collector) {
		return asJavaStream().collect(collector);
	}

	@Override
	public Optional<Data> min(Comparator<? super Data> comparator) {
		return asJavaStream().min(comparator);
	}

	@Override
	public Optional<Data> max(Comparator<? super Data> comparator) {
		return asJavaStream().max(comparator);
	}

	@Override
	public long count() {
		return asJavaStream().count();
	}

	@Override
	public boolean anyMatch(Predicate<? super Data> predicate) {
		return asJavaStream().anyMatch(predicate);
	}

	@Override
	public boolean allMatch(Predicate<? super Data> predicate) {
		return asJavaStream().allMatch(predicate);
	}

	@Override
	public boolean noneMatch(Predicate<? super Data> predicate) {
		return asJavaStream().noneMatch(predicate);
	}

	@Override
	public Optional<Data> findFirst() {
		return asJavaStream().findFirst();
	}

	@Override
	public Optional<Data> findAny() {
		return asJavaStream().findAny();
	}

	@Override
	public Spliterator<Data> spliterator() {
		return Spliterators.spliteratorUnknownSize(iterator(), SORTED | ORDERED | NONNULL | IMMUTABLE);
	}

	@Override
	public boolean isParallel() {
		return false;
	}

	@Override
	public Stream<Data> sequential() {
		return asJavaStream().sequential();
	}

	@Override
	public Stream<Data> parallel() {
		return asJavaStream().parallel();
	}

	@Override
	public Stream<Data> unordered() {
		return asJavaStream().unordered();
	}
}
