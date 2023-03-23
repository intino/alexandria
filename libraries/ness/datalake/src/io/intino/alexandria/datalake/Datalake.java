package io.intino.alexandria.datalake;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.measurement.MeasurementEvent;
import io.intino.alexandria.event.message.MessageEvent;
import io.intino.alexandria.event.resource.ResourceEvent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.intino.alexandria.event.EventStream.merge;
import static io.intino.alexandria.event.EventStream.sequence;

public interface Datalake {
	String MessageStoreFolder = "messages";
	String MeasurementStoreFolder = "measurements";
	String ResourceStoreFolder = "resources";

	Store<MessageEvent> messageStore();

	Store<MeasurementEvent> measurementStore();

	ResourceStore resourceStore();

	interface Store<T extends Event> {
		Stream<Tank<T>> tanks();

		Tank<T> tank(String name);

		default Stream<T> content() {
			return tanks().flatMap(Tank::content);
		}

		default Scale scale() {
			return tanks().parallel().map(Tank::scale).filter(Objects::nonNull).findAny().orElse(null);
		}

		interface Tank<T extends Event> {
			String name();

			default Scale scale() {
				List<Source<T>> sources = sources().collect(Collectors.toList());
				return sources.isEmpty() ? null : sources.get(0).scale();
			}

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
			String name();

			Stream<Store.Tub<T>> tubs();

			Store.Tub<T> first();

			Store.Tub<T> last();

			Store.Tub<T> on(Timetag tag);

			default Scale scale() {
				return first().timetag().scale();
			}

			default Stream<Tub<T>> tubs(Timetag from, Timetag to) {
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

	interface ResourceStore extends Store<ResourceEvent> {
		default Optional<ResourceEvent> find(String rei) {return find(new ResourceEvent.REI(rei));}
		Optional<ResourceEvent> find(ResourceEvent.REI rei);
	}
}