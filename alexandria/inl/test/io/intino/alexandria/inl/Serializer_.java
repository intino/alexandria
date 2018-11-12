package io.intino.alexandria.inl;

import io.intino.alexandria.inl.Inl;
import org.junit.Test;
import schemas.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.util.Arrays.asList;
import static messages.Messages.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Serializer_ {

	@Test
	public void should_serialize_attributes_and_component_of_a_class() throws IOException {
		Person person = new Person("Jose", 50, instant(2016, 10, 4, 10, 10, 11), new Country("Spain"));
		assertThat(Inl.toMessage(person).toString(), is(MessageWithComponent));
	}

	@Test
	public void should_serialize_array_attributes_of_a_class() throws IOException {
		Menu menu = new Menu(new String[]{"Soup", "Lobster", "Mussels", "Cake"}, new Double[]{5.0, 24.5, 8.0, 7.0}, new Boolean[]{true, false});
		assertThat(Inl.toMessage(menu).toString(), is(MenuMessage));
	}

	@Test
	public void should_serialize_empty_array_attributes_of_a_class() throws IOException {
		Menu menu = new Menu(new String[]{}, new Double[]{}, new Boolean[]{true, false});
		assertThat(Inl.toMessage(menu).toString(), is(EmptyMenuMessage));
	}

	@Test
	public void should_serialize_array_attribute_with_null_values_of_a_class() throws IOException {
		Menu menu = new Menu(new String[]{"Soup", null, "Mussels", "Cake"}, new Double[]{5.0, null, 8.0, 7.0}, new Boolean[]{true, false});
		assertThat(Inl.toMessage(menu).toString(), is(NullValueMenuMessage));
	}


	@Test
	public void should_not_serialize_null_attributes_of_a_class() throws IOException {
		Person person = new Person("Jose", 50, null, null);
		assertThat(Inl.toMessage(person).toString(), is("[Person]\nname: Jose\nmoney: 50.0\n"));
	}

	@Test
	public void should_serialize_message_with_multiple_components() throws IOException {
		Teacher teacher = new Teacher("Jose", 50, instant(2016, 10, 4, 20, 10, 11), new Country("Spain"));
		teacher.university = "ULPGC";
		teacher.add(new Phone("+150512101402", new Country("USA")));
		teacher.add(new Phone("+521005101402", new Country("Mexico")));
		assertThat(Inl.toMessage(teacher).toString(), is(MultipleComponentMessage));
	}

	@Test
	public void should_serialize_message_with_multi_line() throws IOException {
		Teacher teacher = new Teacher("Jose\nHernandez", 50, instant(2016, 10, 4, 20, 10, 11), new Country("Spain"));
		teacher.university = "ULPGC";
		teacher.add(new Phone("+150512101402", new Country("USA")));
		teacher.add(new Phone("+521005101402", new Country("Mexico")));
		assertThat(Inl.toMessage(teacher).toString(), is(MultiLineMessage));
	}

	@Test
	public void should_serialize_crash() throws IOException {
		Crash crash = new Crash();
		crash.instant = instant(2017, 3, 21, 7, 39, 0);
		crash.app = "io.intino.consul";
		crash.deviceId = "b367172b0c6fe726";
		crash.stack = Stack;
		assertThat(Inl.toMessage(crash).toString(), is(CrashMessage));
	}

	static Instant instant(int y, int m, int d, int h, int mn, int s) {
		return LocalDateTime.of(y, m, d, h, mn, s).atZone(ZoneId.of("UTC")).toInstant();
	}

}
