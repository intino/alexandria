import io.intino.alexandria.inl.Message;
import io.intino.alexandria.zim.ZimReader;
import io.intino.alexandria.zim.ZimStream;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.TimeZone;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class SourceZimStream_ {

	@Before
	public void setUp() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	@Test
	public void should_parse_multiple_messages() throws Exception {
		ZimStream zimStream = new ZimReader(Messages.StatusMessage);
		Message[] messages = new Message[3];
		messages[0] = zimStream.next();
		messages[1] = zimStream.next();
		messages[2] = zimStream.next();
		assertThat(messages[0].is("status"), is(true));
		assertThat(messages[0].contains("battery"), is(true));
		assertThat(messages[0].contains("isPlugged"), is(true));
		assertThat(messages[0].contains("created"), is(true));
		assertThat(messages[0].contains("xxxx"), is(false));
		assertThat(messages[0].read("battery").as(Double.class), is(78.0));
		assertThat(messages[0].read("isPlugged").as(Boolean.class), is(true));
		assertThat(messages[0].read("created").as(Instant.class).toString(), is("2017-03-22T12:56:18Z"));
		assertThat(messages[2], is(nullValue()));
	}

	@Test
	public void should_ignore_empty_attributes() throws Exception {
		Message message = new ZimReader(Messages.EmptyAttributeMessage).next();
		assertThat(message.is("teacher"), is(true));
		assertThat(message.contains("name"), is(true));
		assertThat(message.contains("money"), is(true));
		assertThat(message.contains("BirthDate"), is(true));
		assertThat(message.components("country").get(0).contains("name"), is(true));
		assertThat(message.components("country").get(0).contains("continent"), is(false));
		assertThat(message.components("country").get(0).read("name").as(String.class), is("Spain"));
		assertThat(message.components("country").get(0).read("continent").as(String.class), is(nullValue()));

	}

	@Test
	public void should_parse_multiline_attributes() throws Exception {
		Message message = new ZimReader(Messages.CrashMessage).next();
		assertThat(message.type(), is("Crash"));
		assertThat(message.contains("instant"), is(true));
		assertThat(message.contains("app"), is(true));
		assertThat(message.contains("deviceId"), is(true));
		assertThat(message.contains("stack"), is(true));
		assertThat(message.read("instant").as(Instant.class).toString(), is("2017-03-21T07:39:00Z"));
		assertThat(message.read("app").as(String.class), is("io.intino.consul"));
		assertThat(message.read("deviceId").as(String.class), is("b367172b0c6fe726"));
		assertThat(message.read("stack").as(String.class), is(Messages.Stack));
	}

	@Test
	public void should_parse_message_with_multiple_components() throws Exception {
		Message message = new ZimReader(Messages.MultipleComponentMessage).next();
		assertThat(message.type(), is("Teacher"));
		assertThat(message.components("country").size(), is(1));
		assertThat(message.components("country").get(0).type(), is("Country"));
		assertThat(message.components("country").get(0).read("name").as(String.class), is("Spain"));
		assertThat(message.components("phone").size(), is(2));
		assertThat(message.toString(), is(Messages.MultipleComponentMessage.trim()));
	}

	@Test
	public void should_parse_message_in_old_format() throws Exception {
		Message message = new ZimReader(Messages.OldFormatMessage).next();
		assertThat(message.type(), is("Teacher"));
		assertThat(message.read("name").as(String.class), is("Jose"));
		assertThat(message.components("country").size(), is(1));
		assertThat(message.components("country").get(0).type(), is("Country"));
		assertThat(message.components("country").get(0).read("name").as(String.class), is("Spain"));

		message.type("Teacher");
		assertThat(message.toString(), is(Messages.MessageWithParentClass.trim()));
	}

}
