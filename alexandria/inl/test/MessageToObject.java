import io.intino.alexandria.Resource;
import io.intino.alexandria.ResourceStore;
import io.intino.alexandria.inl.Inl;
import io.intino.alexandria.inl.InlDeserializer;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import schemas.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static io.intino.alexandria.inl.InlDeserializer.deserialize;
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
	public void should_deserialize_messages_in_a_class_with_parent() throws IOException, IllegalAccessException {
		Teacher teacher = Inl.fromMessage(Inl.toMessage(MessageWithParentClass), Teacher.class);
		assertThat(teacher.name, is("Jose"));
		assertThat(teacher.money, is(50.0));
		assertThat(teacher.birthDate, is(instant(2016, 10, 4, 20, 10, 12)));
		assertThat(teacher.university, is("ULPGC"));
		assertThat(teacher.country.name, is("Spain"));
	}

	@Test
	public void should_deserialize_message_with_empty_attributes() throws IOException, IllegalAccessException {
		Teacher teacher = Inl.fromMessage(Inl.toMessage(EmptyAttributeMessage), Teacher.class);
		assertThat(teacher.name, is("Jose"));
		assertThat(teacher.money, is(50.0));
		assertThat(teacher.birthDate, is(instant(2016, 10, 4, 20, 10, 11)));
		assertThat(teacher.university, is("ULPGC"));
		assertThat(teacher.country.name, is("Spain"));
		assertThat(teacher.country.continent, is(nullValue()));
	}


	@Test
	public void should_serialize_message_with_multi_line() throws IOException, IllegalAccessException {
		Teacher teacher = Inl.fromMessage(Inl.toMessage(MultiLineMessage), Teacher.class);
		assertThat(teacher.name, is("Jose\nHernandez"));
	}

	@Test
	public void should_deserialize_messages_with_array_attributes() throws IOException, IllegalAccessException {
		Menu menu = Inl.fromMessage(Inl.toMessage(MenuMessage), Menu.class);
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
	public void should_deserialize_messages_with_array_attributes_of_size_1() throws IOException, IllegalAccessException {
		Menu menu = Inl.fromMessage(Inl.toMessage(MenuWithOnePriceMessage), Menu.class);
		assertThat(menu.prices.length, is(1));
		assertThat(menu.prices[0], is(7.0));
	}

	@Test
	public void should_deserialize_empty_array_attributes() throws IOException, IllegalAccessException {
		Menu menu = Inl.fromMessage(Inl.toMessage(EmptyMenuMessage), Menu.class);
		assertThat(menu.meals.length, is(0));
		assertThat(menu.prices.length, is(0));
		assertThat(menu.availability.length, is(2));
		assertThat(menu.availability[0], is(true));
		assertThat(menu.availability[1], is(false));
	}

	@Test
	public void should_deserialize_array_attributes_with_null_values() throws IOException, IllegalAccessException {
		Menu menu = Inl.fromMessage(Inl.toMessage(NullValueMenuMessage), Menu.class);
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
//		Crash crash = Inl.fromMessage(Inl.toMessage()(MessageWithParentClass), Crash.class);
//		assertThat(crash, is(nullValue()));
//	}

	@Test
	public void should_deserialize_message_with_multi_line_attributes() throws IOException, IllegalAccessException {
		Crash crash = Inl.fromMessage(Inl.toMessage(CrashMessage), Crash.class);
		assertThat(crash.instant.toString(), is("2017-03-21T07:39:00Z"));
		assertThat(crash.app, is("io.intino.consul"));
		assertThat(crash.deviceId, is("b367172b0c6fe726"));
		assertThat(crash.stack, is(Stack));
	}

	@Test
	public void should_deserialize_message_with_multiple_components() throws IOException, IllegalAccessException {
		Teacher teacher = Inl.fromMessage(Inl.toMessage(MultipleComponentMessage), Teacher.class);
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
		Document document = InlDeserializer.deserialize(DocumentMessage, resourceStore()).next(Document.class);
		assertThat(document.ts().toString(), is("2017-03-21T07:39:00Z"));
		assertThat(document.file().id(), is("4444-444-44-44444.png"));
		assertThat(IOUtils.toString(document.file().data()).length(), is("4444-444-44-44444.png".length()));
	}

	@Test
	public void should_deserialize_message_with_resource_list() throws IOException {
		DocumentList documentList = deserialize(DocumentListMessage, resourceStore()).next(DocumentList.class);
		assertThat(documentList.ts().toString(), is("2017-03-21T07:39:00Z"));
		assertThat(documentList.files().size(), is(2));
		assertThat(documentList.files().get(0).id(), is("4444-444-44-44444.png"));
		assertThat(documentList.files().get(1).id(), is("5555-555-55.jpeg"));
		assertThat(IOUtils.toString(documentList.files().get(0).data()).length(), is("4444-444-44-44444.png".length()));
		assertThat(IOUtils.toString(documentList.files().get(1).data()).length(), is("5555-555-55.jpeg".length()));
	}

	@Test
	public void should_deserialize_message_with_resource_array() throws IOException {
		DocumentArray documentArray = deserialize(DocumentArrayMessage, resourceStore()).next(DocumentArray.class);
		assertThat(documentArray.ts().toString(), is("2017-03-21T07:39:00Z"));
		assertThat(documentArray.files().length, is(2));
		assertThat(documentArray.files()[0].id(), is("4444-444-44-44444.png"));
		assertThat(documentArray.files()[1].id(), is("5555-555-55.jpeg"));
		assertThat(IOUtils.toString(documentArray.files()[0].data()).length(), is("4444-444-44-44444.png".length()));
		assertThat(IOUtils.toString(documentArray.files()[1].data()).length(), is("5555-555-55.jpeg".length()));
	}

	@Test
	public void should_deserialize_schema_3() throws IOException, IllegalAccessException {
		final CredentialLogin t = Inl.fromMessage(Inl.toMessage(CredentialLoginSchema), CredentialLogin.class);
		assertEquals("open", t.authentication());
		assertThat(t.parameterList().size(), is(3));
		assertThat(t.parameterList().get(0).name(), is("username"));
		assertThat(t.parameterList().get(0).value(), is("mcaballero"));
	}

	@Test
	public void should_deserialize_schema_4() throws IOException, IllegalAccessException {
		final InfrastructureOperation op = Inl.fromMessage(Inl.toMessage(infrastructureOperation), InfrastructureOperation.class);
		assertEquals("Add", op.operation());
		assertEquals("cesar", op.user());
		assertEquals("Responsible", op.objectType());
		assertEquals("josejuanhernandez", op.objectID());
		assertThat(op.parameters().size(), is(3));
		assertThat(op.parameters().get(0), is("josejuanhernandez"));
		assertThat(op.parameters().get(1), is("U0CU1BD7E"));
		assertThat(op.parameters().get(2), is("josejuanhernandez@siani.es"));
	}

	private ResourceStore resourceStore() {
		return new ResourceStore() {
			@Override
			public List<Resource> resources() {
				return Collections.emptyList();
			}

			@Override
			public Resource get(String id) {
				return new Resource(id);
			}

			@Override
			public void put(Resource resource) {

			}
		};
	}

	private Instant instant(int y, int m, int d, int h, int mn, int s) {
		return LocalDateTime.of(y, m, d, h, mn, s).atZone(ZoneId.of("UTC")).toInstant();
	}

}
