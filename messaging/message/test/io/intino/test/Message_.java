package io.intino.test;

import io.intino.alexandria.message.Message;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class Message_ {

	@Test
	public void overrideAttribute() {
		new Message("aaaa").set("nombre", "aaa@aaaa").set("nombre", "aaaa");
	}

	@Test
	public void iterables() {
		Message m = new Message("something");
		m.set("array", new int[]{1, 2, 3});
		m.set("list", List.of(1, 2, 3));
		m.set("queue", new ArrayDeque<>(List.of(1, 2, 3)));

		assertArrayEquals(new int[]{1, 2, 3}, m.get("array").as(int[].class));
		assertArrayEquals(new int[]{1, 2, 3}, m.get("list").as(int[].class));
		assertArrayEquals(new int[]{1, 2, 3}, m.get("queue").as(int[].class));
	}

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
						"battery: 80.0\n" +
						"cpuUsage: 11.95\n" +
						"isPlugged: true\n" +
						"isScreenOn: false\n" +
						"temperature: 29.0\n" +
						"created: 2017-03-22T12:56:18Z\n" +
						"taps: 100\n");
	}

	@Test
	public void should_contain_list_attributes() {
		Message message = new Message("Multiline")
				.append("name", "John")
				.append("age", 30)
				.append("age", 20)
				.append("comment", "hello")
				.append("comment", "world")
				.append("comment", "!!!");
		assertThat(message.get("age").toString()).isEqualTo("30\u000120");
		assertThat(message.get("comment").toString()).isEqualTo("hello\u0001world\u0001!!!");
		assertThat(message.toString()).isEqualTo("" +
				"[Multiline]\n" +
				"name: John\n" +
				"age: 30\u000120\n" +
				"comment: hello\u0001world\u0001!!!\n");
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
		message.remove("isScreenOn");
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
}
