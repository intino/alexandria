package io.intino.alexandria.datalake;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.led.Schema;
import io.intino.alexandria.mapp.MappReader;
import io.intino.alexandria.zet.ZetStream;

import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Datalake {
	String EventStoreFolder = "events";
	String SetStoreFolder = "sets";
	String LedStoreFolder = "transactions";

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

			Stream<LedStore.Led> ledger();

			LedStore.Led first();

			LedStore.Led last();

			LedStore.Led on(Timetag tag);

			Stream<LedStore.Led> ledger(Timetag from, Timetag to);
		}

		interface Led {
			Timetag timetag();

			int size();

			<T extends Schema> io.intino.alexandria.led.Led<T> content(Class<T> clazz);
		}
	}
}