package io.intino.alexandria.datalake;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.event.triplet.TripletEvent;

import java.util.function.Predicate;
import java.util.stream.Stream;

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

			Scale scale();

			Stream<Store.Tub<T>> tubs();

			Store.Tub<T> first();

			Store.Tub<T> last();

			Store.Tub<T> on(Timetag tag);

			default Stream<T> content() {
				return EventStream.Sequence.of(tubs().map(Tub::events).toArray(EventStream<T>[]::new));
			}

			default Stream<T> content(Predicate<Timetag> filter) {
				return EventStream.Sequence.of(tubs().filter(t -> filter.test(t.timetag())).map(Tub::events).toArray(EventStream<T>[]::new));
			}
		}

		interface Tub<T extends Event> {
			Timetag timetag();

			Stream<T> events();
		}
	}
}