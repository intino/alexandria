package io.intino.test;

import io.intino.alexandria.Resource;
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

		assertThat(message.get("battery").as(Double.class)).isEqualTo(80.0);
		assertThat(message.get("taps").as(Integer.class)).isEqualTo(100);
		assertThat(message.toString()).isEqualTo(
				"[Status]\n" +
				"cpuUsage: 11.95\n" +
				"isPlugged: true\n" +
				"isScreenOn: false\n" +
				"temperature: 29.0\n" +
				"created: 2017-03-22T12:56:18Z\n" +
						"battery: 80.0\n" +
				"taps: 100\n");
	}

	@Test
	public void should_contain_multiline_attributes() {
		Message message = new Message("Multiline")
		    .append("name", "John")
		    .append("age", 30)
		    .append("age", 20)
		    .append("comment", "hello")
		    .append("comment", "world")
		    .append("comment", "!!!");
		assertThat(message.get("age").toString()).isEqualTo("30\n20");
		assertThat(message.get("comment").toString()).isEqualTo("hello\nworld\n!!!");
		assertThat(message.toString()).isEqualTo("" +
				"[Multiline]\n" +
				"name: John\n" +
				"age:\n" +
				"\t30\n" +
				"\t20\n" +
				"comment:\n" +
				"\thello\n" +
				"\tworld\n" +
				"\t!!!\n");
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
	public void should_handle_attachments() {
		Message message = new Message("Document")
    		.set("name", "my attachment")
    		.set("file1", "photo.png", data(120))
    		.set("file2", "cv.pdf", data(80))
    		.append("file2", "xx.png", data(0))
    		.append("file2", "yy.png", data(100));
        assertThat(message.get("file1").as(Resource.class).name()).isEqualTo("photo.png");
		assertThat(message.get("file1").as(Resource.class).bytes().length).isEqualTo(120);
		assertThat(message.get("file1").as(Resource.class).bytes()).isEqualTo(data(120));
        assertThat(message.get("file1").as(Resource[].class).length).isEqualTo(1);
        assertThat(message.get("file1").as(Resource[].class)[0].name()).isEqualTo("photo.png");
        assertThat(message.get("file2").as(Resource[].class).length).isEqualTo(3);
        assertThat(message.get("file2").as(Resource[].class)[0].name()).isEqualTo("cv.pdf");
		assertThat(message.get("file2").as(Resource[].class)[0].bytes()).isEqualTo(data(80));
		assertThat(message.get("file2").as(Resource[].class)[1].bytes()).isEqualTo(data(0));
		assertThat(message.get("file2").as(Resource[].class)[2].bytes()).isEqualTo(data(100));
        assertThat(message.attachments().size()).isEqualTo(4);
		assertThat(withoutUUID(message.toString())).isEqualTo(
		        "[Document]\n" +
                "name: my attachment\n" +
                "file1: photo.png@...\n" +
                "file2:\n" +
                "\tcv.pdf@...\n" +
                "\txx.png@...\n" +
                "\tyy.png@...\n" +
                "\n" +
                "[&]\n");
		}

	@Test
	public void should_update_attachments() {
		Message message = new Message("Document")
            .set("file", "0.png", data(0))
            .remove("file")
            .set("file","0.png",data(128));
		assertThat(message.get("file").as(Resource.class).type()).isEqualTo("png");
		assertThat(message.get("file").as(Resource.class).bytes().length).isEqualTo(128);
		assertThat(message.get("file").as(Resource.class).bytes()).isEqualTo(data(128));
		assertThat(message.attachments().size()).isEqualTo(1);
		assertThat(withoutUUID(message.toString())).isEqualTo(
		        "[Document]\n" +
                "file: 0.png@...\n" +
                "\n" +
                "[&]\n");
	}

	private byte[] data(int size) {
		byte[] data = new byte[size];
		for (int i = 0; i < size; i++) data[i] = (byte) i;
		return data;
	}

    private String withoutUUID(String str) {
        StringBuilder sb = new StringBuilder();
        for (String s : str.split("\n")) {
            if (s.contains("@")) s = s.substring(0,s.indexOf('@')+1)+"...";
            sb.append(s).append('\n');
        }
        return sb.toString();
    }
}
