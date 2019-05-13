package io.intino.test;

import io.intino.alexandria.mapp.Mapp;
import io.intino.alexandria.mapp.MappBuilder;
import io.intino.alexandria.mapp.MappReader;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class Mapp_ {

	private ByteArrayOutputStream output = output();

	@Test
	public void should_read_empty_mapp() throws IOException {
		MappBuilder builder = new MappBuilder();
		builder.save(output);
		assertThat(output.toByteArray().length).isEqualTo(8);
		assertThat(mapp().size()).isEqualTo(0);
	}

	@Test
	public void should_read_key_value() throws IOException {
		MappBuilder builder = new MappBuilder();
		builder.put(100, "100");
		builder.save(output);
		assertThat(output.toByteArray().length).isEqualTo(17);
		assertThat(mapp().size()).isEqualTo(1);
		assertThat(mapp().get(100)).isEqualTo("100");
	}

	@Test
	public void should_read_keys_with_same_value() throws IOException {
		MappBuilder builder = new MappBuilder();
		long[] keys = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		for (int i = 0; i < keys.length; i++) {
			builder.put(keys[i], "100");
		}
		builder.save(output);
		assertThat(output.toByteArray().length).isEqualTo(35);
		assertThat(mapp().size()).isEqualTo(10);
		assertThat(mapp().get(1)).isEqualTo("100");
		assertThat(mapp().get(9)).isEqualTo("100");
		assertThat(mapp().get(10)).isEqualTo("100");
	}

	@Test
	public void should_read_keys_with_different_values() throws IOException {
		MappBuilder builder = new MappBuilder();
		long[] keys = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		String[] values = {"100", "200", "300", "400", "500", "600", "700", "800", "900", "100"};
		for (int i = 0; i < keys.length; i++) builder.put(keys[i], values[i]);
		builder.save(output);
		assertThat(output.toByteArray().length).isEqualTo(75);
		assertThat(mapp().size()).isEqualTo(10);
		assertThat(mapp().get(1)).isEqualTo("100");
		assertThat(mapp().get(3)).isEqualTo("300");
		assertThat(mapp().get(9)).isEqualTo("900");
		assertThat(mapp().get(10)).isEqualTo("100");
	}

	@Test
	public void should_read_many_keys_with_same_value() throws IOException {
		MappBuilder builder = new MappBuilder();
		builder.put(new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, "100");
		builder.save(output);
		assertThat(output.toByteArray().length).isEqualTo(35);
		assertThat(mapp().size()).isEqualTo(10);
		assertThat(mapp().get(1)).isEqualTo("100");
		assertThat(mapp().get(9)).isEqualTo("100");
		assertThat(mapp().get(10)).isEqualTo("100");
	}

	private Mapp mapp() throws IOException {
		return new Mapp(new MappReader("test", input()));
	}

	private InputStream input() {
		return new ByteArrayInputStream(output.toByteArray());
	}

	private ByteArrayOutputStream output() {
		return new ByteArrayOutputStream();
	}
}
