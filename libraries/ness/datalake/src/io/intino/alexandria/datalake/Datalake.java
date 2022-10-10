package io.intino.alexandria.datalake;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.event.EventStream;

import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Datalake {
	String EventStoreFolder = "events";
	String TripletStoreFolder = "triplets";

	EventStore eventStore();

	TripletStore tripletsStore();

	interface EventStore {

		Stream<Tank> tanks();

		Tank tank(String name);

		interface Tank {
			String name();

			Scale scale();

			Stream<EventStore.Tub> tubs();

			EventStore.Tub first();

			EventStore.Tub last();

			EventStore.Tub on(Timetag tag);

			EventStream content();

			EventStream content(Predicate<Timetag> filter);
		}

		interface Tub {
			Timetag timetag();

			EventStream events();
		}
	}

	interface TripletStore {

		Stream<Tank> tanks();

		Tank tank(String name);

		interface Tank {
			String name();

			Stream<Tub> tubs();

			Tub first();

			Tub last();

			Tub on(Timetag tag);

			Stream<Tub> tubs(int count);

			Stream<Tub> tubs(Timetag from, Timetag to);
		}

		interface Tub {
			Timetag timetag();

			Scale scale();

			Stream<Triplet> triplets();

			Stream<Triplet> triplets(Predicate<Triplet> filter);
		}


		class Triplet {
			String[] fields;

			public Triplet(String[] fields) {
				this.fields = fields;
			}

			public String subject() {
				return fields[0];
			}

			public String verb() {
				return fields[1];
			}


			public String object() {
				return fields[2];
			}

			public String get(int index) {
				return fields[index];
			}

			@Override
			public String toString() {
				return String.join("\t", fields);
			}
		}
	}
}