package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.ui.documentation.Item;
import io.intino.alexandria.ui.documentation.Person;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.Filter;
import io.intino.alexandria.ui.model.datasource.Group;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.intino.alexandria.ui.documentation.Person.Gender.Female;
import static io.intino.alexandria.ui.documentation.Person.Gender.Male;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class Datasources {
	public static final int ItemCount = 1000;
	private static List<Item> itemPopulation = null;
	private static List<Person> personPopulation = null;
	private static Map<String, List<Group>> groupsMap = new HashMap<>();

	public static Datasource<Item> itemDatasource() {
		return new Datasource<Item>() {
			@Override
			public long itemCount(String condition, List<Filter> filters) {
				return ItemCount;
			}

			@Override
			public List<Item> items(int start, int count, String condition, List<Filter> filters, List<String> sortings) {
				List<Item> items = population();
				if (sortings.size() > 0) Collections.sort(items, sortingComparator(sortings));
				return page(items, start, count);
			}

			@Override
			public List<Group> groups(String name) {
				if (!name.equalsIgnoreCase("alphabeticOrder")) return emptyList();
				return Datasources.groups("itemalphabeticorder", population(), item -> ((Item)item).label().substring(0, 1));
			}

			public Comparator<Item> sortingComparator(List<String> sortings) {
				return Comparator.comparing(Item::label);
			}

			private List<Item> population() {
				if (itemPopulation != null) return new ArrayList<>(itemPopulation);
				itemPopulation = new ArrayList<>();
				for (int i = 0; i < ItemCount; i++)
					itemPopulation.add(new Item().label("item " + (i + 1)));
				return new ArrayList<>(itemPopulation);
			}
		};
	}

	public static Datasource<Person> personDatasource() {

		return new Datasource<Person>() {

			@Override
			public long itemCount(String condition, List<Filter> filters) {
				return filterPopulation(condition, filters).size();
			}

			@Override
			public List<Person> items(int start, int count, String condition, List<Filter> filters, List<String> sortings) {
				List<Person> persons = filterPopulation(condition, filters);
				if (sortings.size() > 0) persons.sort(sortingComparator(sortings));
				return page(persons, start, count);
			}

			@Override
			public List<Group> groups(String key) {
				if (key.toLowerCase().contains("gender")) return Datasources.groups("persongender", population(), item -> ((Person)item).gender().name());
				if (key.toLowerCase().contains("age group")) return Datasources.groups("personagegroup", population(), item -> ageGroupLabel(((Person)item).age()));
				return emptyList();
			}

			public Comparator<Person> sortingComparator(List<String> sortings) {
				if (sortings.size() <= 0) return Comparator.comparing(Person::lastName);
				Comparator<Person> comparator = null;

				for (String sorting : sortings) {
					if (comparator == null) comparator = comparator(sorting);
					else comparator.thenComparing(comparator(sorting));
				}

				return comparator;
			}

			private Comparator<Person> comparator(String sorting) {
				if (sorting.contains("oldest")) return (o1, o2) -> Integer.compare(o2.age(), o1.age());
				else if (sorting.contains("youthest")) return Comparator.comparingInt(Person::age);
				else if (sorting.contains("female")) return genderComparator(Female);
				else if (sorting.contains("male")) return genderComparator(Male);
				else return Comparator.comparing(Person::lastName);
			}

			private Comparator<Person> genderComparator(Person.Gender flag) {
				return (o1, o2) -> {
					if (o1.gender() == Female && o2.gender() == Female && flag == Female) return 1;
					if (o1.gender() == Female && o2.gender() == Female && flag == Male) return -1;
					if (o1.gender() == Male && o2.gender() == Male && flag == Male) return 1;
					if (o1.gender() == Male && o2.gender() == Male && flag == Female) return -1;
					if (o1.gender() == Male && o2.gender() == Female && flag == Female) return 1;
					if (o1.gender() == Female && o2.gender() == Male && flag == Female) return -1;
					if (o1.gender() == Male && o2.gender() == Female && flag == Male) return -1;
					if (o1.gender() == Female && o2.gender() == Male && flag == Male) return 1;
					return 1;
				};
			}

			private String ageGroupLabel(int age) {
				if (age <= 14) return "Child";
				if (age <= 24) return "Youth";
				if (age <= 64) return "Adult";
				return "Senior";
			}

			private List<Person> filterPopulation(String condition, List<Filter> filters) {
				List<Person> result = population();
				result = filterCondition(result, condition);
				result = filter(result, getFilter("gender", filters), item -> ((Person)item).gender().name());
				result = filter(result, getFilter("age group", filters), item -> ageGroupLabel(((Person)item).age()));
				return result;
			}

			private List<Person> filterCondition(List<Person> population, String condition) {
				if (condition == null || condition.isEmpty()) return population;
				return population.stream().filter(person -> person.firstName().toLowerCase().contains(condition.toLowerCase()) ||
															person.lastName().toLowerCase().contains(condition.toLowerCase()) ||
															person.gender().name().toLowerCase().equalsIgnoreCase(condition) ||
															String.valueOf(person.age()).contains(condition.toLowerCase())
				).collect(Collectors.toList());
			}

			private List<Person> filter(List<Person> population, Filter filter, Function<Object, String> valueFunction) {
				if (filter == null || filter.groups().isEmpty()) return population;
				return population.stream().filter(person -> filter.groups().contains(valueFunction.apply(person))).collect(toList());
			}

			private Filter getFilter(String name, List<Filter> filters) {
				return filters.stream().filter(f -> f.grouping().equalsIgnoreCase(name)).findFirst().orElse(null);
			}

			private List<Person> population() {
				if (personPopulation != null) return new ArrayList<>(personPopulation);
				personPopulation = new ArrayList<>();
				for (int i = 0; i < ItemCount; i++)
					personPopulation.add(new Person().firstName("first name " + (i + 1)).lastName("last name" + (i + 1)).gender(Math.random() < 0.5 ? Male : Female).age(randomAge()));
				return new ArrayList<>(personPopulation);
			}

			private int randomAge() {
				Random r = new Random();
				return r.nextInt(100);
			}
		};
	}

	private static List<Group> groups(String id, List<? extends Object> items, Function<Object, String> labelFunction) {
		if (!groupsMap.containsKey(id)) {
			Map<String, Group> groups = new HashMap<>();

			items.forEach(item -> {
				String label = labelFunction.apply(item);
				if (!groups.containsKey(label)) groups.put(label, new Group().label(label).count(0));
				Group group = groups.get(label);
				group.count(group.count() + 1);
			});

			groupsMap.put(id, new ArrayList<>(groups.values()));
		}

		return groupsMap.get(id);
	}

	private static <T> List<T> page(List<T> items, int start, int count) {
		List<T> result = new ArrayList<>();
		int end = start+count;
		if (items.size() < (start+count)) end = items.size();
		for (int i = start; i < end; i++) result.add(items.get(i));
		return result;
	}

}
