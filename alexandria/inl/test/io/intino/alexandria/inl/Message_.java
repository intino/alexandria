package io.intino.alexandria.inl;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Message_ {

	private Message message;

	@Before
	public void setUp() {
		message = new Message("Status");
		message.set("battery", 78.0);
		message.set("cpuUsage", 11.95);
		message.set("isPlugged", true);
		message.set("isScreenOn", false);
		message.set("temperature", 29.0);
		message.set("created", "2017-03-22T12:56:18Z");
	}



	@Test
	public void should_override_and_create_new_attributes() {
		message.set("battery", 80.0);
		message.set("taps", 100);
		assertThat(message.read("battery").as(Double.class), is(80.0));
		assertThat(message.read("taps").as(Integer.class), is(100));
		assertThat(message.toString(), is("[Status]\nbattery: 80.0\ncpuUsage: 11.95\nisPlugged: true\nisScreenOn: false\ntemperature: 29.0\ncreated: 2017-03-22T12:56:18Z\ntaps: 100"));
	}

	@Test
	public void should_remove_attributes() {
		message.remove("battery");
		message.remove("isscreenon");
		assertThat(message.contains("battery"), is(false));
		assertThat(message.contains("isScreenOn"), is(false));
		assertThat(message.contains("isPlugged"), is(true));
	}

	@Test
	public void should_rename_attributes() {
		message
				.rename("isPlugged", "plugged")
				.rename("battery", "b");
		assertThat(message.contains("battery"), is(false));
		assertThat(message.contains("b"), is(true));
		assertThat(message.contains("isPlugged"), is(false));
		assertThat(message.contains("plugged"), is(true));
	}

	@Test
	public void should_change_type() {
		message.type("sensor");
		assertThat(message.is("sensor"), is(true));
		assertThat(message.contains("battery"), is(true));
	}

	@Test
	public void should_handle_document_list_attributes() {
		Message message = new Message("Document");
		message.set("name", "my file");
		message.write("file", "png", data(20));
		message.write("file", "png", data(30));
		message.set("file", "png", data(40));
		message.write("file", "png", data(80));
		assertThat(message.attachments().size(), is(2));
		assertThat(message.attachments().size(), is(2));
		assertThat(message.attachment(message.get("file").split("\n")[0]).type(), is("png"));
		assertThat(message.attachment(message.get("file").split("\n")[0]).data().length, is(40));
	}

	@Test
	public void should_handle_document_attributes() {
		Message message = new Message("Document");
		message.set("name", "my file");
		message.set("file", "png", data(64));
		message.set("file", "png", data(128));
		assertThat(message.attachments().size(), is(1));
		assertThat(message.attachment(message.get("file")).type(), is("png"));
		assertThat(message.attachment(message.get("file")).data().length, is(128));
	}

	@Test
	public void should_update_documents() {
		Message message = new Message("Document");
		message.write("file", "png", data(0));
		for (Message.Attachment attachment : message.attachments()) {
			attachment.data(data(128));
		}
		assertThat(message.attachments().size(), is(1));
		assertThat(message.attachment(message.get("file")).type(), is("png"));
		assertThat(message.attachment(message.get("file")).data().length, is(128));
	}

	@Test
	public void should_handle_multi_line_attributes() {
		Message message = new Message("Multiline");
		message.write("name", "John");
		message.write("age", 30);
		message.write("age", 20);
		message.write("comment", "hello");
		message.write("comment", "world");
		message.write("comment", "!!!");
		assertThat(message.get("age"), is("30\n20"));
		assertThat(message.get("comment"), is("hello\nworld\n!!!"));
		assertThat(message.toString(), is("[Multiline]\nname: John\nage:\n\t30\n\t20\ncomment:\n\thello\n\tworld\n\t!!!"));
	}

	private byte[] data(int size) {
		byte[] data = new byte[size];
		for (int i = 0; i < size; i++) data[i] = (byte) i;
		return data;
	}

}
