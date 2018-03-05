import io.intino.konos.alexandria.schema.Resource;
import io.intino.konos.alexandria.schema.ResourceStore;
import io.intino.konos.alexandria.schema.Serializer;
import org.apache.tika.io.IOUtils;
import org.junit.Test;
import schemas.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static io.intino.konos.alexandria.schema.Serializer.serialize;
import static java.util.Arrays.asList;
import static messages.Messages.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Serializer_ {

	@Test
	public void should_serialize_attributes_and_component_of_a_class() {
		Person person = new Person("Jose", 50, instant(2016, 10, 4, 10, 10, 11), new Country("Spain"));
		assertThat(serialize(person).toInl(), is(MessageWithComponent));
	}

	@Test
	public void should_serialize_array_attributes_of_a_class() {
		Menu menu = new Menu(new String[]{"Soup", "Lobster", "Mussels", "Cake"}, new Double[]{5.0, 24.5, 8.0, 7.0}, new Boolean[]{true, false});
		assertThat(serialize(menu).toInl(), is(MenuMessage));
	}

	@Test
	public void should_serialize_empty_array_attributes_of_a_class() {
		Menu menu = new Menu(new String[]{}, new Double[]{}, new Boolean[]{true, false});
		assertThat(serialize(menu).toInl(), is(EmptyMenuMessage));
	}

	@Test
	public void should_serialize_array_attribute_with_null_values_of_a_class() {
		Menu menu = new Menu(new String[]{"Soup", null, "Mussels", "Cake"}, new Double[]{5.0, null, 8.0, 7.0}, new Boolean[]{true, false});
		assertThat(serialize(menu).toInl(), is(NullValueMenuMessage));
	}

	@Test
	public void should_serialize_a_class_with_mapping() {
		MenuX menu = new MenuX(new String[]{"Soup", "Lobster", "Mussels", "Cake"}, new Double[]{5.0, 24.5, 8.0, 7.0}, new Boolean[]{true, false});
		Serializer serializer = serialize(menu).
				map("MenuX", "Menu").
				map("_meals", "meals").
				map("_prices", "prices").
				map("_availability", "availability");
		assertThat(serializer.toInl(), is(MenuMessage));
	}

	@Test
	public void should_not_serialize_null_attributes_of_a_class() {
		Person person = new Person("Jose", 50, null, null);
		assertThat(serialize(person).toInl(), is("[Person]\nname: Jose\nmoney: 50.0\n"));
	}

	@Test
	public void should_serialize_message_with_multiple_components() {
		Teacher teacher = new Teacher("Jose", 50, instant(2016, 10, 4, 20, 10, 11), new Country("Spain"));
		teacher.university = "ULPGC";
		teacher.add(new Phone("+150512101402", new Country("USA")));
		teacher.add(new Phone("+521005101402", new Country("Mexico")));
		assertThat(serialize(teacher).toInl(), is(MultipleComponentMessage));
	}

	@Test
	public void should_serialize_message_with_multi_line() {
		Teacher teacher = new Teacher("Jose\nHernandez", 50, instant(2016, 10, 4, 20, 10, 11), new Country("Spain"));
		teacher.university = "ULPGC";
		teacher.add(new Phone("+150512101402", new Country("USA")));
		teacher.add(new Phone("+521005101402", new Country("Mexico")));
		assertThat(serialize(teacher).toInl(), is(MultiLineMessage));
	}

	@Test
	public void should_serialize_a_list_of_objects() {
		Status status1 = new Status().battery(78.0).cpuUsage(11.95).isPlugged(true).isScreenOn(false).temperature(29.0).created("2017-03-22T12:56:18Z");
		Status status2 = new Status().battery(78.0).cpuUsage(11.95).isPlugged(true).isScreenOn(true).temperature(29.0).created("2017-03-22T12:56:18Z");
		assertThat(serialize(asList(status1, status2)).toInl(), is(StatusMessage.replaceAll(" = ", "=")));
	}

	@Test
	public void should_serialize_crash() {
		Crash crash = new Crash();
		crash.instant = instant(2017, 3, 21, 7, 39, 0);
		crash.app = "io.intino.consul";
		crash.deviceId = "b367172b0c6fe726";
		crash.stack = Stack;
		assertThat(serialize(crash).toInl(), is(CrashMessage));
	}

	@Test
	public void should_serialize_document() throws IOException {
		Document document = new Document(instant(2017, 3, 21, 7, 39, 0), new Resource("4444-444-44-44444.png").data(new byte[100]));
		ResourceStore documentStore = ResourceStore.collector();
		assertThat(serialize(document, documentStore).toInl(), is(DocumentMessage));
		assertThat(documentStore.resources().size(), is(1));
		assertThat(documentStore.resources().get(0).id(), is("4444-444-44-44444.png"));
		assertThat(IOUtils.toString(documentStore.resources().get(0).data()).length(), is(100));
	}


	@Test
	public void should_serialize_document_list() throws IOException {
		DocumentList documentList = new DocumentList(instant(2017, 3, 21, 7, 39, 0), new Resource("4444-444-44-44444.png").data(new byte[100]), new Resource("5555-555-55.jpeg").data(new byte[80]));
		ResourceStore documentStore = ResourceStore.collector();
		assertThat(serialize(documentList, documentStore).toInl(), is(DocumentListMessage));
		assertThat(documentStore.resources().size(), is(2));
		assertThat(documentStore.resources().get(0).id(), is("4444-444-44-44444.png"));
		assertThat(IOUtils.toString(documentStore.resources().get(0).data()).length(), is(100));
		assertThat(documentStore.resources().get(1).id(), is("5555-555-55.jpeg"));
		assertThat(IOUtils.toString(documentStore.resources().get(0).data()).length(), is(80));
	}

	@Test
	public void should_serialize_document_array() throws IOException {
		DocumentArray documentArray = new DocumentArray(instant(2017, 3, 21, 7, 39, 0), new Resource("4444-444-44-44444.png").data(new byte[100]), new Resource("5555-555-55.jpeg").data(new byte[80]));
		ResourceStore documentStore = ResourceStore.collector();
		assertThat(serialize(documentArray, documentStore).toInl(), is(DocumentArrayMessage));
		assertThat(documentStore.resources().size(), is(2));
		assertThat(documentStore.resources().get(0).id(), is("4444-444-44-44444.png"));
		assertThat(IOUtils.toString(documentStore.resources().get(0).data()).length(), is(100));
		assertThat(documentStore.resources().get(1).id(), is("5555-555-55.jpeg"));
		assertThat(IOUtils.toString(documentStore.resources().get(0).data()).length(), is(80));
	}

	private Instant instant(int y, int m, int d, int h, int mn, int s) {
		return LocalDateTime.of(y, m, d, h, mn, s).atZone(ZoneId.of("UTC")).toInstant();
	}

}
