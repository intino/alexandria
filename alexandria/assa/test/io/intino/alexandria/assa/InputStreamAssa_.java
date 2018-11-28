package io.intino.alexandria.assa;

import io.intino.alexandria.assa.loaders.InputStreamAssa;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class InputStreamAssa_ {

	private ByteArrayOutputStream output = output();

	@Test
	public void should_read_name() throws IOException {
		AssaBuilder<String> builder = new AssaBuilder<>("feature");
		builder.save(output);
		assertThat(output.toByteArray().length).isEqualTo(21);
		Assertions.assertThat(InputStreamAssa.of(input(), String.class).name()).isEqualTo("feature");
		assertThat(InputStreamAssa.of(input(), String.class).size()).isEqualTo(0);
	}


	@Test
	public void should_read_key_value() throws IOException {
		AssaBuilder<String> builder = new AssaBuilder<>("feature");
		builder.put(100, "100");
		builder.save(output);
		assertThat(output.toByteArray().length).isEqualTo(39);
		assertThat(InputStreamAssa.of(input(), String.class).size()).isEqualTo(1);
		assertThat(InputStreamAssa.of(input(), String.class).get(100)).isEqualTo("100");
	}

	@Test
	public void should_read_keys_with_same_value() throws IOException {
		AssaBuilder<String> builder = new AssaBuilder<>("feature");
		long[] keys = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		for (int i = 0; i < keys.length; i++) {
			builder.put(keys[i], "100");
		}
		builder.save(output);
		assertThat(output.toByteArray().length).isEqualTo(147);
		assertThat(InputStreamAssa.of(input(), String.class).size()).isEqualTo(10);
		assertThat(InputStreamAssa.of(input(), String.class).get(1)).isEqualTo("100");
		assertThat(InputStreamAssa.of(input(), String.class).get(9)).isEqualTo("100");
		assertThat(InputStreamAssa.of(input(), String.class).get(10)).isEqualTo("100");
	}

	@Test
	public void should_read_keys_with_different_values() throws IOException {
		AssaBuilder<String> builder = new AssaBuilder<>("feature");
		long[] keys = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		String[] values = {"100", "200", "300", "400", "500", "600", "700", "800", "900", "100"};
		for (int i = 0; i < keys.length; i++) builder.put(keys[i], values[i]);
		builder.save(output);
		assertThat(output.toByteArray().length).isEqualTo(195);
		assertThat(InputStreamAssa.of(input(), String.class).size()).isEqualTo(10);
		assertThat(InputStreamAssa.of(input(), String.class).get(1)).isEqualTo("100");
		assertThat(InputStreamAssa.of(input(), String.class).get(3)).isEqualTo("300");
		assertThat(InputStreamAssa.of(input(), String.class).get(9)).isEqualTo("900");
		assertThat(InputStreamAssa.of(input(), String.class).get(10)).isEqualTo("100");
	}

	@Test
	public void should_read_many_keys_with_same_value() throws IOException {
		AssaBuilder<String> builder = new AssaBuilder<>("feature");
		builder.put(new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, "100");
		builder.save(output);
		assertThat(output.toByteArray().length).isEqualTo(147);
		assertThat(InputStreamAssa.of(input(), String.class).size()).isEqualTo(10);
		assertThat(InputStreamAssa.of(input(), String.class).get(1)).isEqualTo("100");
		assertThat(InputStreamAssa.of(input(), String.class).get(9)).isEqualTo("100");
		assertThat(InputStreamAssa.of(input(), String.class).get(10)).isEqualTo("100");
	}

	private InputStream input() {
		return new ByteArrayInputStream(output.toByteArray());
	}

	private ByteArrayOutputStream output() {
		return new ByteArrayOutputStream();
	}
}
