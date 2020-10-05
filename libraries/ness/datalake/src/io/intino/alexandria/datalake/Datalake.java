package io.intino.alexandria.datalake;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.led.Item;
import io.intino.alexandria.mapp.MappReader;
import io.intino.alexandria.zet.ZetStream;

import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Datalake {
	String EventStoreFolder = "events";
	String SetStoreFolder = "sets";
	String LedStoreFolder = "ledger";

	EventStore eventStore();

	SetStore setStore();

	LedStore ledStore();

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

	interface SetStore {

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

			MappReader index();

			Set set(String set);

			Stream<Set> sets();

			Stream<Set> sets(Predicate<Set> filter);
		}

		interface Set {
			String name();

			Timetag timetag();

			int size();

			ZetStream content();

			Stream<Variable> variables();

			Variable variable(String name);
		}

		class Variable {
			public final String name;
			public final String value;

			public Variable(String name, String value) {
				this.name = name;
				this.value = value;
			}
		}
	}

	interface LedStore {
		Stream<LedStore.Tank> tanks();

		LedStore.Tank tank(String name);

		interface Tank {
			String name();

			Stream<LedStore.Tub> tubs();

			LedStore.Tub first();

			LedStore.Tub last();

			LedStore.Tub on(Timetag tag);

			Stream<LedStore.Tub> tubs(int count);

			Stream<LedStore.Tub> tubs(Timetag from, Timetag to);
		}

		interface Tub {
			Timetag timetag();

			Scale scale();

			LedStore.Led led(String set);

			Stream<LedStore.Led> leds();

			Stream<LedStore.Led> leds(Predicate<LedStore.Led> filter);
		}

		interface Led {
			String name();

			Timetag timetag();

			int size();

			<T extends Item> io.intino.alexandria.led.Led<T> content(Class<T> clazz);
		}

	}
}