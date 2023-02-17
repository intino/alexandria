package io.intino.alexandria.zim;

import io.intino.alexandria.message.Message;

import java.util.Comparator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.*;
import java.util.stream.*;

import static java.util.Spliterator.*;

abstract class AbstractZimStream implements Stream<Message> {

	protected Stream<Message> asJavaStream() {
		return StreamSupport.stream(spliterator(), false);
	}

	@Override
	public Stream<Message> filter(Predicate<? super Message> predicate) {
		return asJavaStream().filter(predicate);
	}

	@Override
	public <R> Stream<R> map(Function<? super Message, ? extends R> mapper) {
		return asJavaStream().map(mapper);
	}

	@Override
	public IntStream mapToInt(ToIntFunction<? super Message> mapper) {
		return asJavaStream().mapToInt(mapper);
	}

	@Override
	public LongStream mapToLong(ToLongFunction<? super Message> mapper) {
		return asJavaStream().mapToLong(mapper);
	}

	@Override
	public DoubleStream mapToDouble(ToDoubleFunction<? super Message> mapper) {
		return asJavaStream().mapToDouble(mapper);
	}

	@Override
	public <R> Stream<R> flatMap(Function<? super Message, ? extends Stream<? extends R>> mapper) {
		return asJavaStream().flatMap(mapper);
	}

	@Override
	public IntStream flatMapToInt(Function<? super Message, ? extends IntStream> mapper) {
		return asJavaStream().flatMapToInt(mapper);
	}

	@Override
	public LongStream flatMapToLong(Function<? super Message, ? extends LongStream> mapper) {
		return asJavaStream().flatMapToLong(mapper);
	}

	@Override
	public DoubleStream flatMapToDouble(Function<? super Message, ? extends DoubleStream> mapper) {
		return asJavaStream().flatMapToDouble(mapper);
	}

	@Override
	public Stream<Message> distinct() {
		return asJavaStream().distinct();
	}

	@Override
	public Stream<Message> sorted() {
		return asJavaStream().sorted();
	}

	@Override
	public Stream<Message> sorted(Comparator<? super Message> comparator) {
		return asJavaStream().sorted(comparator);
	}

	@Override
	public Stream<Message> peek(Consumer<? super Message> action) {
		return asJavaStream().peek(action);
	}

	@Override
	public Stream<Message> limit(long maxSize) {
		return asJavaStream().limit(maxSize);
	}

	@Override
	public Stream<Message> skip(long n) {
		return asJavaStream().skip(n);
	}

	@Override
	public void forEachOrdered(Consumer<? super Message> action) {
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
	public Message reduce(Message identity, BinaryOperator<Message> accumulator) {
		return asJavaStream().reduce(identity, accumulator);
	}

	@Override
	public Optional<Message> reduce(BinaryOperator<Message> accumulator) {
		return asJavaStream().reduce(accumulator);
	}

	@Override
	public <U> U reduce(U identity, BiFunction<U, ? super Message, U> accumulator, BinaryOperator<U> combiner) {
		return asJavaStream().reduce(identity, accumulator, combiner);
	}

	@Override
	public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super Message> accumulator, BiConsumer<R, R> combiner) {
		return asJavaStream().collect(supplier, accumulator, combiner);
	}

	@Override
	public <R, A> R collect(Collector<? super Message, A, R> collector) {
		return asJavaStream().collect(collector);
	}

	@Override
	public Optional<Message> min(Comparator<? super Message> comparator) {
		return asJavaStream().min(comparator);
	}

	@Override
	public Optional<Message> max(Comparator<? super Message> comparator) {
		return asJavaStream().max(comparator);
	}

	@Override
	public long count() {
		return asJavaStream().count();
	}

	@Override
	public boolean anyMatch(Predicate<? super Message> predicate) {
		return asJavaStream().anyMatch(predicate);
	}

	@Override
	public boolean allMatch(Predicate<? super Message> predicate) {
		return asJavaStream().allMatch(predicate);
	}

	@Override
	public boolean noneMatch(Predicate<? super Message> predicate) {
		return asJavaStream().noneMatch(predicate);
	}

	@Override
	public Optional<Message> findFirst() {
		return asJavaStream().findFirst();
	}

	@Override
	public Optional<Message> findAny() {
		return asJavaStream().findAny();
	}

	@Override
	public Spliterator<Message> spliterator() {
		return Spliterators.spliteratorUnknownSize(iterator(), SORTED | ORDERED | NONNULL | IMMUTABLE);
	}

	@Override
	public boolean isParallel() {
		return false;
	}

	@Override
	public Stream<Message> sequential() {
		return asJavaStream().sequential();
	}

	@Override
	public Stream<Message> parallel() {
		return asJavaStream().parallel();
	}

	@Override
	public Stream<Message> unordered() {
		return asJavaStream().unordered();
	}
}
