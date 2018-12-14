package io.intino.alexandria.assa;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class Assa_ {

	private ByteArrayOutputStream output = output();

	@Test
	public void should_read_empty_assa() throws IOException {
		AssaBuilder builder = new AssaBuilder();
		builder.save(output);
		assertThat(output.toByteArray().length).isEqualTo(8);
		assertThat(assa().size()).isEqualTo(0);
	}

	@Test
	public void should_read_key_value() throws IOException {
		AssaBuilder builder = new AssaBuilder();
		builder.put(100, "100");
		builder.save(output);
		assertThat(output.toByteArray().length).isEqualTo(20);
		assertThat(assa().size()).isEqualTo(1);
		assertThat(assa().get(100)).isEqualTo("100");
	}

	@Test
	public void should_read_keys_with_same_value() throws IOException {
		AssaBuilder builder = new AssaBuilder();
		long[] keys = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		for (int i = 0; i < keys.length; i++) {
			builder.put(keys[i], "100");
		}
		builder.save(output);
		assertThat(output.toByteArray().length).isEqualTo(38);
		assertThat(assa().size()).isEqualTo(10);
		assertThat(assa().get(1)).isEqualTo("100");
		assertThat(assa().get(9)).isEqualTo("100");
		assertThat(assa().get(10)).isEqualTo("100");
	}

	@Test
	public void should_read_keys_with_different_values() throws IOException {
		AssaBuilder builder = new AssaBuilder();
		long[] keys = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		String[] values = {"100", "200", "300", "400", "500", "600", "700", "800", "900", "100"};
		for (int i = 0; i < keys.length; i++) builder.put(keys[i], values[i]);
		builder.save(output);
		assertThat(output.toByteArray().length).isEqualTo(78);
		assertThat(assa().size()).isEqualTo(10);
		assertThat(assa().get(1)).isEqualTo("100");
		assertThat(assa().get(3)).isEqualTo("300");
		assertThat(assa().get(9)).isEqualTo("900");
		assertThat(assa().get(10)).isEqualTo("100");
	}

	@Test
	public void should_read_many_keys_with_same_value() throws IOException {
		AssaBuilder builder = new AssaBuilder();
		builder.put(new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, "100");
		builder.save(output);
		assertThat(output.toByteArray().length).isEqualTo(38);
		assertThat(assa().size()).isEqualTo(10);
		assertThat(assa().get(1)).isEqualTo("100");
		assertThat(assa().get(9)).isEqualTo("100");
		assertThat(assa().get(10)).isEqualTo("100");
	}

	private Assa assa() throws IOException {
		return new Assa(new AssaReader("test", input()));
	}

	private InputStream input() {
		return new ByteArrayInputStream(output.toByteArray());
	}

	private ByteArrayOutputStream output() {
		return new ByteArrayOutputStream();
	}
}
