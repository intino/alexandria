package io.intino.alexandria.inl;

import messages.Messages;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import schemas.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static messages.Messages.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObjectToMessage {

	@Test
	public void should_serialize_attributes_and_component_of_a_class() throws IOException {
		Person person = new Person("Jose", 50, Serializer_.instant(2016, 10, 4, 10, 10, 11), new Country("Spain"));
		Assert.assertThat(Inl.toMessage(person).toString(), Is.is(MessageWithComponent));
	}

	@Test
	public void should_serialize_array_attributes_of_a_class() throws IOException {
		Menu menu = new Menu(new String[]{"Soup", "Lobster", "Mussels", "Cake"}, new Double[]{5.0, 24.5, 8.0, 7.0}, new Boolean[]{true, false});
		Assert.assertThat(Inl.toMessage(menu).toString(), Is.is(MenuMessage));
	}

	@Test
	public void should_serialize_empty_array_attributes_of_a_class() throws IOException {
		Menu menu = new Menu(new String[]{}, new Double[]{}, new Boolean[]{true, false});
		Assert.assertThat(Inl.toMessage(menu).toString(), Is.is(EmptyMenuMessage));
	}

	@Test
	public void should_serialize_schema() throws IOException {
		final String expectedText = "[AlertModified]\n" +
				"alert: Alerts#bbc15556-244b-45af-97b9-c0f18b1e42be\n" +
				"active: true\n" +
				"mailingList: cambullonero@monentia.es\n" +
				"applyToAllStations: false\n";
		final AlertModified object = new AlertModified().alert("Alerts#bbc15556-244b-45af-97b9-c0f18b1e42be").active(true).mailingList(asList("cambullonero@monentia.es")).applyToAllStations(false);
		final Message message = Inl.toMessage(object);
		assertEquals(expectedText, message.toString());
	}

	@Test
	public void should_deserialize_to_schema() throws IOException, IllegalAccessException {
		String text = "[AlertModified]\n" +
				"alert: Alerts#e4a80d88-7bd5-4948-bd1d-7b38f47c40c7\n" +
				"active: true\n" +
				"mailingList: jbelizon@monentia.es\n" +
				"applyToAllStations: false\n";
		AlertModified object = Inl.fromMessage(new InlReader(new ByteArrayInputStream(text.getBytes())).next(), AlertModified.class);
		assertThat(object.mailingList().size(), is(1));
		assertNotNull(object);
		final Message message = Inl.toMessage(object);
		assertEquals(text, message.toString());
	}

	@Test
	public void should_serialize_schema2() throws IOException {
		InfrastructureOperation object = new InfrastructureOperation().user("cesar").ts(Instant.parse("2018-05-22T11:17:20.895Z")).operation("Add").objectType("Responsible").objectID("josejuanhernandez").parameters(Arrays.asList("josejuanhernandez", "U0CU1BD7E", "josejuanhernandez@siani.es"));
		assertEquals(Messages.infrastructureOperation, Inl.toMessage(object).toString());
	}

	@Test
	public void should_serialize_schema_3() throws IOException {
		CredentialLogin login = new CredentialLogin().authentication("open").parameterList(parameters());
		final Message message = Inl.toMessage(login);
		assertEquals(CredentialLoginSchema, message.toString());
	}

	private static List<Parameter> parameters() {
		return new ArrayList<Parameter>() {{
			add(new Parameter("username", "mcaballero"));
			add(new Parameter("rememberMe", "true"));
			add(new Parameter("token", "abcdedgrd"));
		}};
	}
}
