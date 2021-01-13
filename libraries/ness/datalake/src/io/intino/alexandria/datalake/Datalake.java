package io.intino.alexandria.datalake;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.mapp.MappReader;
import io.intino.alexandria.zet.ZetStream;

import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Datalake {
	String EventStoreFolder = "events";
	String SetStoreFolder = "sets";
	String TransactionStoreFolder = "transactions";

	EventStore eventStore();

	SetStore setStore();

	TransactionStore transactionStore();

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

	interface TransactionStore {
		Stream<TransactionStore.Tank> tanks();

		TransactionStore.Tank tank(String name);

		interface Tank {
			String name();

			Stream<Transaction> transactions();

			Transaction first();

			Transaction last();

			Transaction on(Timetag tag);

			Stream<Transaction> transactions(Timetag from, Timetag to);
		}

		interface Transaction {
			Timetag timetag();

			int size();

			<T extends Schema> LedStream<T> content(SchemaFactory<T> clazz);
		}
	}
}