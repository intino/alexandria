package io.intino.alexandria.datalake;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.measurement.MeasurementEvent;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.event.triplet.TripletEvent;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Datalake {
	String EventStoreFolder = "events";
	String TripletStoreFolder = "triplets";
	String MeasurementStoreFolder = "measurements";

	Store<MessageEvent> messageStore();

	Store<TripletEvent> tripletStore();

	Store<MeasurementEvent> measurementStore();

	interface Store<T extends Event> {
		Stream<Tank<T>> tanks();

		Tank<T> tank(String name);

		interface Tank<T extends Event> {
			String name();

			public Source<T> source(String name);

			default Stream<T> content() {
				//TODO CH merge and sequence
				return EventStream.Sequence.of(tubs().map(Tub::events).toArray(EventStream<T>[]::new));
			}

			default Stream<T> content(BiPredicate<Source<T>, Timetag> filter) {
				//TODO CH merge and sequence
				return EventStream.Sequence.of(tubs().filter(t -> filter.test(t.timetag())).map(Tub::events).toArray(EventStream<T>[]::new));
			}
		}

		interface Source<T extends Event> {
			default Scale scale() {
				return first().timetag().scale();
			}

			Stream<Store.Tub<T>> tubs();

			Store.Tub<T> first();

			Store.Tub<T> last();

			Store.Tub<T> on(Timetag tag);

			default Stream<Tub> tubs(Timetag from, Timetag to) {
				return StreamSupport.stream(from.iterateTo(to).spliterator(), false).map(this::on);
			}
		}

		interface Tub<T extends Event> {
			Timetag timetag();

			Stream<T> events();

			default Scale scale() {
				return timetag().scale();
			}

			default Stream<T> events(Predicate<T> filter) {
				return events().filter(filter);
			}

		}
	}
}