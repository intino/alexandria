package io.intino.test;

import io.intino.alexandria.inl.Message;
import io.intino.alexandria.zim.ZimReader;
import io.intino.alexandria.zim.ZimStream;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

public class ZimReader_ {

	@Before
	public void setUp() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	@Test
	public void should_parse_multiple_messages() {
		ZimStream zimStream = new ZimReader(Messages.StatusMessage);
		Message[] messages = new Message[3];
		messages[0] = zimStream.next();
		messages[1] = zimStream.next();
		messages[2] = zimStream.next();
		assertThat(messages[0].is("status")).isTrue();
		assertThat(messages[0].contains("battery")).isTrue();
		assertThat(messages[0].contains("isPlugged")).isTrue();
		assertThat(messages[0].contains("created")).isTrue();
		assertThat(messages[0].contains("xxxx")).isFalse();
		assertThat(messages[0].get("battery").as(Double.class)).isEqualTo(78.0);
		assertThat(messages[0].get("isPlugged").as(Boolean.class)).isTrue();
		assertThat(messages[0].get("created").as(Instant.class).toString()).isEqualTo("2017-03-22T12:56:18Z");
		assertThat(messages[2]).isNull();
	}

	@Test
	public void should_ignore_empty_attributes() {
		String inl =
			"[Teacher]\n" +
			"name: Jose\n" +
			"money: 50.0\n" +
			"birthDate: 2016-10-04T20:10:11Z\n" +
			"university: ULPGC\n" +
			"\n" +
			"[Person.Country]\n" +
			"name: Spain\n" +
			"continent:\n";
		Message message = new ZimReader(inl).next();
		assertThat(message.is("teacher")).isTrue();
		assertThat(message.contains("name")).isTrue();
		assertThat(message.contains("money")).isTrue();
		assertThat(message.contains("BirthDate")).isTrue();
		assertThat(message.components("country").get(0).contains("name")).isTrue();
		assertThat(message.components("country").get(0).contains("continent")).isFalse();
		assertThat(message.components("country").get(0).get("name").as(String.class)).isEqualTo("Spain");
		assertThat(message.components("country").get(0).get("continent").as(String.class)).isNull();

	}

	@Test
	public void should_parse_multiline_attributes() {
		Message message = new ZimReader(Messages.CrashMessage).next();
		assertThat(message.type()).isEqualTo("Crash");
		assertThat(message.contains("instant")).isTrue();
		assertThat(message.contains("app")).isTrue();
		assertThat(message.contains("deviceId")).isTrue();
		assertThat(message.contains("stack")).isTrue();
		assertThat(message.get("instant").as(Instant.class).toString()).isEqualTo("2017-03-21T07:39:00Z");
		assertThat(message.get("app").as(String.class)).isEqualTo("io.intino.consul");
		assertThat(message.get("deviceId").as(String.class)).isEqualTo("b367172b0c6fe726");
		assertThat(message.get("stack").as(String.class)).isEqualTo(Messages.Stack);
	}

	@Test
	public void should_parse_message_with_multiple_components() {
		Message message = new ZimReader(Messages.MultipleComponentMessage).next();
		assertThat(message.type()).isEqualTo("Teacher");
		assertThat(message.components("country").size()).isEqualTo(1);
		assertThat(message.components("country").get(0).type()).isEqualTo("Country");
		assertThat(message.components("country").get(0).get("name").as(String.class)).isEqualTo("Spain");
		assertThat(message.components("phone").size()).isEqualTo(2);
		assertThat(message.toString()).isEqualTo(Messages.MultipleComponentMessage);
	}

}
