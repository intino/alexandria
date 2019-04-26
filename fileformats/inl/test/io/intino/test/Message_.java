package io.intino.test;

import io.intino.alexandria.inl.Message;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Message_ {

	@Test
	public void should_contain_attributes() {
		Message message = new Message("Status")
				.set("battery", 78.0)
				.set("cpuUsage", 11.95)
				.set("isPlugged", true)
				.set("isScreenOn", false)
				.set("temperature", 29.0)
				.set("created", "2017-03-22T12:56:18Z")
				.set("battery", 80.0)
				.set("taps", 100);

		assertThat(message.read("battery").as(Double.class)).isEqualTo(80.0);
		assertThat(message.read("taps").as(Integer.class)).isEqualTo(100);
		assertThat(message.toString()).isEqualTo(
				"[Status]\n" +
						"battery: 80.0\n" +
						"cpuUsage: 11.95\n" +
						"isPlugged: true\n" +
						"isScreenOn: false\n" +
						"temperature: 29.0\n" +
						"created: 2017-03-22T12:56:18Z\n" +
						"taps: 100\n");
	}

	@Test
	public void should_remove_attributes() {
		Message message = new Message("Status")
				.set("battery", 78.0)
				.set("cpuUsage", 11.95)
				.set("isPlugged", true)
				.set("isScreenOn", false)
				.set("temperature", 29.0)
				.set("created", "2017-03-22T12:56:18Z")
				.remove("battery")
				.remove("isscreenon");
		assertThat(message.contains("battery")).isEqualTo(false);
		assertThat(message.contains("isScreenOn")).isEqualTo(false);
		assertThat(message.contains("isPlugged")).isEqualTo(true);
	}

	@Test
	public void should_rename_attributes() {
		Message message = new Message("Status")
				.set("battery", 78.0)
				.set("cpuUsage", 11.95)
				.set("isPlugged", true)
				.set("isScreenOn", false)
				.set("temperature", 29.0)
				.set("created", "2017-03-22T12:56:18Z")
				.rename("isPlugged", "plugged")
				.rename("battery", "b");
		assertThat(message.contains("battery")).isEqualTo(false);
		assertThat(message.contains("b")).isEqualTo(true);
		assertThat(message.contains("isPlugged")).isEqualTo(false);
		assertThat(message.contains("plugged")).isEqualTo(true);
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
		assertThat(message.is("sensor")).isEqualTo(true);
		assertThat(message.contains("battery")).isEqualTo(true);
	}

	@Test
	public void should_handle_document_list_attributes() {
		Message message = new Message("Document");
		message.set("name", "my attachment");
		message.attach("file1", "png", data(20));
		message.attach("file1", "png", data(30));
		message.attach("file2", "png", data(40));
		message.attach("file2", "png", data(80));
		assertThat(message.attachments().size()).isEqualTo(4);
		assertThat(message.attachment(message.get("file1").split("\n")[0]).type()).isEqualTo("png");
		assertThat(message.attachment(message.get("file2").split("\n")[0]).data().length).isEqualTo(40);
	}

	@Test
	public void should_handle_document_attributes() {
		Message message = new Message("Document");
		message.set("name", "my attachment");
		message.attach("attachment", "png", data(64));
		message.attach("attachment", "png", data(128));
		assertThat(message.attachments().size()).isEqualTo(2);
		assertThat(message.attachment(message.get("attachment").split("\n")[0]).type()).isEqualTo("png");
		assertThat(message.attachment(message.get("attachment").split("\n")[1]).data().length).isEqualTo(128);
	}

	@Test
	public void should_update_documents() {
		Message message = new Message("Document");
		message.attach("attachment", "png", data(0));
		for (Message.Attachment attachment : message.attachments())
			attachment.data(data(128));
		assertThat(message.attachments().size()).isEqualTo(1);
		assertThat(message.attachment(message.get("attachment")).type()).isEqualTo("png");
		assertThat(message.attachment(message.get("attachment")).data().length).isEqualTo(128);
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
		assertThat(message.get("age")).isEqualTo("30\n20");
		assertThat(message.get("comment")).isEqualTo("hello\nworld\n!!!");
		assertThat(message.toString()).isEqualTo("[Multiline]\nname: John\nage:\n\t30\n\t20\ncomment:\n\thello\n\tworld\n\t!!!\n");
	}

	private byte[] data(int size) {
		byte[] data = new byte[size];
		for (int i = 0; i < size; i++) data[i] = (byte) i;
		return data;
	}

}
