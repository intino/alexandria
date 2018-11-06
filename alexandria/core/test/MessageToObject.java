import io.intino.konos.alexandria.Inl;
import io.intino.konos.alexandria.schema.ResourceLoader;
import io.intino.ness.inl.Message;
import org.apache.tika.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import schemas.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static io.intino.konos.alexandria.schema.Deserializer.deserialize;
import static messages.Messages.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@SuppressWarnings("ALL")
public class MessageToObject {

	@Before
	public void setUp() {
	}

	@Test
	public void should_deserialize_messages_in_a_class_with_parent() {
		Teacher teacher = Inl.fromMessage(Message.load(MessageWithParentClass), Teacher.class);
		assertThat(teacher.name, is("Jose"));
		assertThat(teacher.money, is(50.0));
		assertThat(teacher.birthDate, is(instant(2016, 10, 4, 20, 10, 12)));
		assertThat(teacher.university, is("ULPGC"));
		assertThat(teacher.country.name, is("Spain"));
	}

	@Test
	public void should_deserialize_message_with_empty_attributes() {
		Teacher teacher = Inl.fromMessage(Message.load(EmptyAttributeMessage), Teacher.class);
		assertThat(teacher.name, is("Jose"));
		assertThat(teacher.money, is(50.0));
		assertThat(teacher.birthDate, is(instant(2016, 10, 4, 20, 10, 11)));
		assertThat(teacher.university, is("ULPGC"));
		assertThat(teacher.country.name, is("Spain"));
		assertThat(teacher.country.continent, is(nullValue()));
	}


	@Test
	public void should_serialize_message_with_multi_line() {
		Teacher teacher = Inl.fromMessage(Message.load(MultiLineMessage), Teacher.class);
		assertThat(teacher.name, is("Jose\nHernandez"));
	}

	@Test
	public void should_deserialize_messages_with_array_attributes() {
		Menu menu = Inl.fromMessage(Message.load(MenuMessage), Menu.class);
		assertThat(menu.meals.length, is(4));
		assertThat(menu.prices.length, is(4));
		assertThat(menu.availability.length, is(2));
		assertThat(menu.meals[0], is("Soup"));
		assertThat(menu.meals[1], is("Lobster"));
		assertThat(menu.prices[0], is(5.0));
		assertThat(menu.prices[1], is(24.5));
		assertThat(menu.prices[2], is(8.0));
		assertThat(menu.availability[0], is(true));
		assertThat(menu.availability[1], is(false));
	}

	@Test
	public void should_deserialize_messages_with_array_attributes_of_size_1() {
		Menu menu = Inl.fromMessage(Message.load(MenuWithOnePriceMessage), Menu.class);
		assertThat(menu.prices.length, is(1));
		assertThat(menu.prices[0], is(7.0));
	}

	@Test
	public void should_deserialize_empty_array_attributes() {
		Menu menu = Inl.fromMessage(Message.load(EmptyMenuMessage), Menu.class);
		assertThat(menu.meals.length, is(0));
		assertThat(menu.prices.length, is(0));
		assertThat(menu.availability.length, is(2));
		assertThat(menu.availability[0], is(true));
		assertThat(menu.availability[1], is(false));
	}

	@Test
	public void should_deserialize_array_attributes_with_null_values() {
		Menu menu = Inl.fromMessage(Message.load(NullValueMenuMessage), Menu.class);
		assertThat(menu.meals.length, is(4));
		assertThat(menu.prices.length, is(4));
		assertThat(menu.availability.length, is(2));
		assertThat(menu.meals[0], is("Soup"));
		assertThat(menu.meals[1], is(nullValue()));
		assertThat(menu.prices[0], is(5.0));
		assertThat(menu.prices[1], is(nullValue()));
		assertThat(menu.prices[2], is(8.0));
		assertThat(menu.availability[0], is(true));
		assertThat(menu.availability[1], is(false));
	}
//
//	@Test
//	public void should_return_null_if_header_doesnt_match_the_class() {
//		Crash crash = Inl.fromMessage(Message.load(MessageWithParentClass), Crash.class);
//		assertThat(crash, is(nullValue()));
//	}

	@Test
	public void should_deserialize_message_with_multi_line_attributes() {
		Crash crash = Inl.fromMessage(Message.load(CrashMessage), Crash.class);
		assertThat(crash.instant.toString(), is("2017-03-21T07:39:00Z"));
		assertThat(crash.app, is("io.intino.consul"));
		assertThat(crash.deviceId, is("b367172b0c6fe726"));
		assertThat(crash.stack, is(Stack));
	}

	@Test
	public void should_deserialize_message_with_multiple_components() {
		Teacher teacher = Inl.fromMessage(Message.load(MultipleComponentMessage), Teacher.class);
		assertThat(teacher.name, is("Jose"));
		assertThat(teacher.money, is(50.0));
		assertThat(teacher.birthDate, is(instant(2016, 10, 4, 20, 10, 11)));
		assertThat(teacher.country.name, is("Spain"));
		assertThat(teacher.phones.size(), is(2));
		assertThat(teacher.phones.get(0).value, is("+150512101402"));
		assertThat(teacher.phones.get(0).country.name, is("USA"));
		assertThat(teacher.phones.get(1).value, is("+521005101402"));
		assertThat(teacher.phones.get(1).country.name, is("Mexico"));
	}

	@Test
	public void should_deserialize_message_in_old_format() {
		Teacher teacher = deserialize(OldFormatMessage).next(Teacher.class);
		assertThat(teacher.name, is("Jose"));
		assertThat(teacher.money, is(50.0));
		assertThat(teacher.birthDate, is(instant(2016, 10, 4, 20, 10, 12)));
		assertThat(teacher.university, is("ULPGC"));
		assertThat(teacher.country.name, is("Spain"));
		assertThat(teacher.country.continent, is(nullValue()));
	}

	@Test
	public void should_deserialize_message_with_resource() throws IOException {
		Document document = deserialize(DocumentMessage, id -> new byte[id.length()]).next(Document.class);
		assertThat(document.ts().toString(), is("2017-03-21T07:39:00Z"));
		assertThat(document.file().id(), is("4444-444-44-44444.png"));
		assertThat(IOUtils.toString(document.file().data()).length(), is("4444-444-44-44444.png".length()));
	}

	@Test
	public void should_deserialize_message_with_resource_list() throws IOException {
		DocumentList documentList = deserialize(DocumentListMessage, getResourceLoader()).next(DocumentList.class);
		assertThat(documentList.ts().toString(), is("2017-03-21T07:39:00Z"));
		assertThat(documentList.files().size(), is(2));
		assertThat(documentList.files().get(0).id(), is("4444-444-44-44444.png"));
		assertThat(documentList.files().get(1).id(), is("5555-555-55.jpeg"));
		assertThat(IOUtils.toString(documentList.files().get(0).data()).length(), is("4444-444-44-44444.png".length()));
		assertThat(IOUtils.toString(documentList.files().get(1).data()).length(), is("5555-555-55.jpeg".length()));
	}

	@Test
	public void should_deserialize_message_with_resource_array() throws IOException {
		DocumentArray documentArray = deserialize(DocumentArrayMessage, getResourceLoader()).next(DocumentArray.class);
		assertThat(documentArray.ts().toString(), is("2017-03-21T07:39:00Z"));
		assertThat(documentArray.files().length, is(2));
		assertThat(documentArray.files()[0].id(), is("4444-444-44-44444.png"));
		assertThat(documentArray.files()[1].id(), is("5555-555-55.jpeg"));
		assertThat(IOUtils.toString(documentArray.files()[0].data()).length(), is("4444-444-44-44444.png".length()));
		assertThat(IOUtils.toString(documentArray.files()[1].data()).length(), is("5555-555-55.jpeg".length()));
	}

	@Test
	public void should_deserialize_schema_3() {
		final CredentialLogin t = Inl.fromMessage(Message.load(CredentialLoginSchema), CredentialLogin.class);
		assertEquals("open", t.authentication());
		assertThat(t.parameterList().size(), is(3));
		assertThat(t.parameterList().get(0).name(), is("username"));
		assertThat(t.parameterList().get(0).value(), is("mcaballero"));
	}

	@Test
	public void should_deserialize_schema_4() {
		final InfrastructureOperation op = Inl.fromMessage(Message.load(infrastructureOperation), InfrastructureOperation.class);
		assertEquals("Add", op.operation());
		assertEquals("cesar", op.user());
		assertEquals("Responsible", op.objectType());
		assertEquals("josejuanhernandez", op.objectID());
		assertThat(op.parameters().size(), is(3));
		assertThat(op.parameters().get(0), is("josejuanhernandez"));
		assertThat(op.parameters().get(1), is("U0CU1BD7E"));
		assertThat(op.parameters().get(2), is("josejuanhernandez@siani.es"));
	}

	private ResourceLoader getResourceLoader() {
		return id -> new byte[id.length()];
	}

	private Instant instant(int y, int m, int d, int h, int mn, int s) {
		return LocalDateTime.of(y, m, d, h, mn, s).atZone(ZoneId.of("UTC")).toInstant();
	}

}
