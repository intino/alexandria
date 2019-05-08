package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.ui.documentation.Item;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.Filter;
import io.intino.alexandria.ui.model.datasource.Group;

import java.util.*;
import java.util.function.Function;

import static java.util.Collections.emptyList;

public class Datasources {

	public static Datasource<Item> itemDatasource() {
		return new Datasource<Item>() {
			@Override
			public int itemCount(String condition, List<Filter> filters) {
				return 1000;
			}

			@Override
			public List<Item> items(int start, int count, String condition, List<Filter> filters) {
				List<Item> result = new ArrayList<>();
				for (int i = start; i < start + count; i++)
					result.add(new Item().label("Item " + (i+1)));
				return result;
			}

			@Override
			public List<Group> groups(String name, List<Item> items) {
				if (!name.equalsIgnoreCase("alphabeticOrder")) return emptyList();
				return Datasources.groups(items, item -> ((Item)item).label());
			}
		};
	}

	public static Datasource<Person> personDatasource() {
		return new Datasource<Person>() {
			@Override
			public int itemCount(String condition, List<Filter> filters) {
				return 1000;
			}

			@Override
			public List<Person> items(int start, int count, String condition, List<Filter> filters) {
				List<Person> result = new ArrayList<>();
				for (int i = start; i < start + count; i++)
					result.add(new Person().firstName("first name " + (i+1)).lastName("last name" + (i+1)));
				return result;
			}

			@Override
			public List<Group> groups(String name, List<Person> items) {
				if (!name.equalsIgnoreCase("alphabeticOrder")) return emptyList();
				return Datasources.groups(items, item -> ((Person)item).firstName());
			}
		};
	}

	private static List<Group> groups(List<? extends Object> items, Function<Object, String> labelFunction) {
		Map<String, Group> groups = new HashMap<>();

		items.forEach(item -> {
			String order = labelFunction.apply(item).substring(0, 1);
			if (!groups.containsKey(order)) groups.put(order, new Group().label(order).count(0));
			Group group = groups.get(order);
			group.count(group.count() + 1);
		});

		return new ArrayList<>(groups.values());
	}

}
