package io.intino.alexandria.inl;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Message_ {

	@Test
	public void should_override_and_create_new_attributes() {
		Message message = new Message("Status");
		message.set("battery", 78.0);
		message.set("cpuUsage", 11.95);
		message.set("isPlugged", true);
		message.set("isScreenOn", false);
		message.set("temperature", 29.0);
		message.set("created", "2017-03-22T12:56:18Z");
		message.set("battery", 80.0);
		message.set("taps", 100);

		assertThat(message.read("battery").as(Double.class), is(80.0));
		assertThat(message.read("taps").as(Integer.class), is(100));
		String expected = "[Status]\nbattery: 80.0\ncpuUsage: 11.95\nisPlugged: true\nisScreenOn: false\ntemperature: 29.0\ncreated: 2017-03-22T12:56:18Z\ntaps: 100\n";
		assertThat(message.toString(), is(expected));
	}

	@Test
	public void should_remove_attributes() {
		Message message = new Message("Status");
		message.set("battery", 78.0);
		message.set("cpuUsage", 11.95);
		message.set("isPlugged", true);
		message.set("isScreenOn", false);
		message.set("temperature", 29.0);
		message.set("created", "2017-03-22T12:56:18Z");
		message.remove("battery");
		message.remove("isscreenon");
		assertThat(message.contains("battery"), is(false));
		assertThat(message.contains("isScreenOn"), is(false));
		assertThat(message.contains("isPlugged"), is(true));
	}

	@Test
	public void should_rename_attributes() {
		Message message = new Message("Status");
		message.set("battery", 78.0);
		message.set("cpuUsage", 11.95);
		message.set("isPlugged", true);
		message.set("isScreenOn", false);
		message.set("temperature", 29.0);
		message.set("created", "2017-03-22T12:56:18Z");
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
		Message message = new Message("Status");
		message.set("battery", 78.0);
		message.set("cpuUsage", 11.95);
		message.set("isPlugged", true);
		message.set("isScreenOn", false);
		message.set("temperature", 29.0);
		message.set("created", "2017-03-22T12:56:18Z");
		message.type("sensor");
		assertThat(message.is("sensor"), is(true));
		assertThat(message.contains("battery"), is(true));
	}

	@Test
	public void should_handle_document_list_attributes() {
		Message message = new Message("Document");
		message.set("name", "my attachment");
		message.attach("file1", "png", data(20));
		message.attach("file1", "png", data(30));
		message.attach("file2", "png", data(40));
		message.attach("file2", "png", data(80));
		assertThat(message.attachments().size(), is(4));
		assertThat(message.attachment(message.get("file1").split("\n")[0]).type(), is("png"));
		assertThat(message.attachment(message.get("file2").split("\n")[0]).data().length, is(40));
	}

	@Test
	public void should_handle_document_attributes() {
		Message message = new Message("Document");
		message.set("name", "my attachment");
		message.attach("attachment", "png", data(64));
		message.attach("attachment", "png", data(128));
		assertThat(message.attachments().size(), is(2));
		assertThat(message.attachment(message.get("attachment").split("\n")[0]).type(), is("png"));
		assertThat(message.attachment(message.get("attachment").split("\n")[1]).data().length, is(128));
	}

	@Test
	public void should_update_documents() {
		Message message = new Message("Document");
		message.attach("attachment", "png", data(0));
		for (Message.Attachment attachment : message.attachments())
			attachment.data(data(128));
		assertThat(message.attachments().size(), is(1));
		assertThat(message.attachment(message.get("attachment")).type(), is("png"));
		assertThat(message.attachment(message.get("attachment")).data().length, is(128));
	}

	@Test
	public void should_handle_multi_line_attributes() {
		Message message = new Message("Multiline");
		message.append("name", "John");
		message.append("age", 30);
		message.append("age", 20);
		message.append("comment", "hello");
		message.append("comment", "world");
		message.append("comment", "!!!");
		assertThat(message.get("age"), is("30\n20"));
		assertThat(message.get("comment"), is("hello\nworld\n!!!"));
		assertThat(message.toString(), is("[Multiline]\nname: John\nage:\n\t30\n\t20\ncomment:\n\thello\n\tworld\n\t!!!\n"));
	}

	private byte[] data(int size) {
		byte[] data = new byte[size];
		for (int i = 0; i < size; i++) data[i] = (byte) i;
		return data;
	}

}
