package io.intino.test;

import io.intino.alexandria.Resource;
import io.intino.alexandria.ResourceStore;
import io.intino.alexandria.inl.*;
import org.junit.Test;
import io.intino.test.schemas.Teacher;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SuppressWarnings("ALL")
public class MessageReader_ {

	@Test
	public void should_read_empty_content() {
		MessageReader messages = new MessageReader("");
		assertThat(messages.hasNext()).isFalse();
		assertThat(messages.next()).isNull();
	}

	@Test
	public void should_read_messages_in_a_class_with_parent() throws IOException, IllegalAccessException {
		String str =
				"[Teacher]\n" +
				"name: Jose\n" +
				"money: 50.0\n" +
				"birthDate: 2016-10-04T20:10:12Z\n" +
				"university: ULPGC\n" +
				"\n" +
				"[Teacher.Country]\n" +
				"name: Spain\n";
		checkReaderWith(str);
	}

	@Test
	public void should_read_messages_with_old_format() throws IOException, IllegalAccessException {
		String str =
				"[Dialog]\n" +
				"instant = \"2019-01-14T13:34:09.742Z\"\n" +
				"opinion = \"Satisfied\"\n" +
				"cancelled = false\n" +
				"contactSet = false\n" +
				"contactData = \"\"\n" +
				"wantsToBeContacted = false\n" +
				"area = \"MockChainHotelReception\"\n" +
				"eventId = \"\"\n" +
				"eventLabel = \"\"\n" +
				"issueId = \"\"\n" +
				"touchCounter = 1\n" +
				"sensorId = \"3C15C2CBFF020000\"\n" +
				"apkVersion = \"3.0.21\"\n" +
				"fingerSizes = \"0\"\n" +
				"hearts = 1\n";
		MessageReader messages = new MessageReader(str);
		assertThat(messages.next().toString()).isEqualTo(str.replace(" =",":"));
	}


	@Test
	public void should_read_message_with_multi_line() throws IOException, IllegalAccessException {
		String str =
				"[Teacher]\n" +
				"name:\n\tJose\n\tHernandez\n" +
				"money: 50.0\n" +
				"birthDate: 2016-10-04T20:10:11Z\n" +
				"university: ULPGC\n" +
				"\n" +
				"[Teacher.Country]\n" +
				"name: Spain\n" +
				"\n" +
				"[Teacher.Phone]\n" +
				"value: +150512101402\n" +
				"\n" +
				"[Teacher.Phone.Country]\n" +
				"name: USA\n" +
				"\n" +
				"[Teacher.Phone]\n" +
				"value: +521005101402\n" +
				"\n" +
				"[Teacher.Phone.Country]\n" +
				"name: Mexico\n";
		checkReaderWith(str);
	}

	@Test
	public void should_read_messages_with_array_attributes() throws IOException, IllegalAccessException {
		String str =
				"[Menu]\n" +
				"meals:\n" +
				"\tSoup\n" +
				"\tLobster\n" +
				"\tMussels\n" +
				"\tCake\n" +
				"prices:\n" +
				"\t5.0\n" +
				"\t24.5\n" +
				"\t8.0\n" +
				"\t7.0\n" +
				"availability:\n" +
				"\ttrue\n" +
				"\tfalse\n";
		checkReaderWith(str);
	}




	private void checkReaderWith(String str) {
		MessageReader messages = new MessageReader(str);
		assertThat(messages.hasNext()).isTrue();
		assertThat(messages.next().toString()).isEqualTo(str);
		assertThat(messages.hasNext()).isFalse();
		assertThat(messages.next()).isNull();
	}

	//	@Test
	//	public void should_return_null_if_header_doesnt_match_the_class() {
	//		Crash crash = Inl.fromMessage(xxx()(MessageWithParentClass), Crash.class);
	//		assertThat(crash).isEqualTo()(nullValue());
	//
	//	}


	@Test
	public void should_read_message_in_old_format() {
		String str =
				"[Teacher]\n" +
				"name = \"Jose\"\n" +
				"money=50.0\n" +
				"birthDate= 2016-10-04T20:10:12Z\n" +
				"university = ULPGC\n" +
				"\n" +
				"[Teacher.Country]\n" +
				"name=\"Spain\"\n" +
				"continent=\n";
		Teacher teacher = InlReader.read(str).next(Teacher.class);
		assertThat(teacher.name).isEqualTo("Jose");
		assertThat(teacher.money).isEqualTo(50.0);
		assertThat(teacher.birthDate).isEqualTo(instant(2016, 10, 4, 20, 10, 12));
		assertThat(teacher.university).isEqualTo("ULPGC");
		assertThat(teacher.country.name).isEqualTo("Spain");
		assertThat(teacher.country.continent).isNull();
	}



	private ResourceStore resourceStore() {
		return new ResourceStore() {
			@Override
			public List<Resource> resources() {
				return Collections.emptyList();
			}

			@Override
			public Resource get(String id) {
				return new Resource(id){
					@Override
					public InputStream data() {
						return new ByteArrayInputStream(id.getBytes());
					}
				};
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
