package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.ui.documentation.Item;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.model.Datasource;

import java.util.ArrayList;
import java.util.List;

public class Datasources {

	public static Datasource<Item> itemDatasource() {
		return new Datasource<Item>() {
			@Override
			public int itemCount(String condition) {
				return 1000;
			}

			@Override
			public List<Item> items(int start, int count, String condition) {
				List<Item> result = new ArrayList<>();
				for (int i = start; i < start + count; i++)
					result.add(new Item().label("Item " + (i+1)));
				return result;
			}
		};
	}

	public static Datasource<Person> personDatasource() {
		return new Datasource<Person>() {
			@Override
			public int itemCount(String condition) {
				return 1000;
			}

			@Override
			public List<Person> items(int start, int count, String condition) {
				List<Person> result = new ArrayList<>();
				for (int i = start; i < start + count; i++)
					result.add(new Person().firstName("first name " + (i+1)).lastName("last name" + (i+1)));
				return result;
			}
		};
	}

}
