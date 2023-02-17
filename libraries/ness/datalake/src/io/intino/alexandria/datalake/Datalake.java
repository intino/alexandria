package io.intino.alexandria.datalake;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.measurement.MeasurementEvent;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.event.tuple.TupleEvent;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.intino.alexandria.event.EventStream.merge;
import static io.intino.alexandria.event.EventStream.sequence;

public interface Datalake {
	String EventStoreFolder = "events";
	String TupleStoreFolder = "tuples";
	String MeasurementStoreFolder = "measurements";

	Store<MessageEvent> messageStore();

	Store<TupleEvent> tripletStore();

	Store<MeasurementEvent> measurementStore();

	interface Store<T extends Event> {
		Stream<Tank<T>> tanks();

		Tank<T> tank(String name);

		interface Tank<T extends Event> {
			String name();

			Source<T> source(String name);

			Stream<Source<T>> sources();

			default Stream<T> content() {
				return merge(sources().map(s -> sequence(s.tubs().map(Tub::events))));
			}

			default Stream<T> content(BiPredicate<Source<T>, Timetag> filter) {
				return merge(sources().map(s -> sequence(s.tubs().filter(t -> filter.test(s, t.timetag())).map(Tub::events))));
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