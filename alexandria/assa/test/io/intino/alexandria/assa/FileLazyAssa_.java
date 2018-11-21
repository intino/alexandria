package io.intino.alexandria.assa;

import io.intino.alexandria.assa.loaders.FileLazyAssa;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class FileLazyAssa_ {

	private FileOutputStream output = output();

	@Test
	public void should_read_name() throws IOException {
		AssaBuilder<String> builder = new AssaBuilder<>("feature");
		builder.save(output);
		assertThat(file().length()).isEqualTo(21);
		Assertions.assertThat(FileLazyAssa.of(file(), String.class).name()).isEqualTo("feature");
		assertThat(FileLazyAssa.of(file(), String.class).size()).isEqualTo(0);
	}


	@Test
	public void should_read_key_value() throws IOException {
		AssaBuilder<String> builder = new AssaBuilder<>("feature");
		builder.put(10000, "100");
		builder.save(output);
		assertThat(file().length()).isEqualTo(39);
		assertThat(FileLazyAssa.of(file(), String.class).size()).isEqualTo(1);
		assertThat(FileLazyAssa.of(file(), String.class).get(10000)).isEqualTo("100");
	}

	@Test
	public void should_read_keys_with_same_value() throws IOException {
		AssaBuilder<String> builder = new AssaBuilder<>("feature");
		long[] keys = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
		for (long key : keys) builder.put(key, "100");
		builder.save(output);
		assertThat(file().length()).isEqualTo(147);
		assertThat(FileLazyAssa.of(file(), String.class).size()).isEqualTo(10);
		assertThat(FileLazyAssa.of(file(), String.class).get(100)).isEqualTo("100");
		assertThat(FileLazyAssa.of(file(), String.class).get(900)).isEqualTo("100");
		assertThat(FileLazyAssa.of(file(), String.class).get(1000)).isEqualTo("100");
		assertThat(FileLazyAssa.of(file(), String.class).get(555)).isNull();
	}

	@Test
	public void should_read_keys_with_different_values() throws IOException {
		AssaBuilder<String> builder = new AssaBuilder<>("feature");
		long[] keys = {10, 2, 8, 4, 7, 9, 3, 6, 1, 5};
		String[] values = {"100", "200", "300", "400", "500", "600", "700", "800", "900", "100"};
		for (int i = 0; i < keys.length; i++) builder.put(keys[i], values[i]);
		builder.save(output);
		assertThat(file().length()).isEqualTo(195);
		assertThat(FileLazyAssa.of(file(), String.class).size()).isEqualTo(10);
		assertThat(FileLazyAssa.of(file(), String.class).get(1)).isEqualTo("900");
		assertThat(FileLazyAssa.of(file(), String.class).get(3)).isEqualTo("700");
		assertThat(FileLazyAssa.of(file(), String.class).get(9)).isEqualTo("600");
		assertThat(FileLazyAssa.of(file(), String.class).get(10)).isEqualTo("100");
	}

	@Test
	public void should_read_many_keys_with_same_value() throws IOException {
		AssaBuilder<String> builder = new AssaBuilder<>("feature");
		long[] keys = {10, 2, 8, 4, 7, 9, 3, 6, 1, 5};
		builder.put(keys, "100");
		builder.save(output);
		assertThat(file().length()).isEqualTo(147);
		assertThat(FileLazyAssa.of(file(), String.class).size()).isEqualTo(10);
		assertThat(FileLazyAssa.of(file(), String.class).get(1)).isEqualTo("100");
		assertThat(FileLazyAssa.of(file(), String.class).get(9)).isEqualTo("100");
		assertThat(FileLazyAssa.of(file(), String.class).get(10)).isEqualTo("100");
	}

	@Test
	public void should_store_many_keys() throws IOException {
		AssaBuilder<String> builder = new AssaBuilder<>("feature");
		for (int i = 1; i <= 10000; i++) {
			builder.put(10000 - i, "x" + i);
		}
		builder.save(output);
		assertThat(FileLazyAssa.of(file(), String.class).get(9999)).isEqualTo("x1");
		assertThat(FileLazyAssa.of(file(), String.class).get(9950)).isEqualTo("x50");
		assertThat(FileLazyAssa.of(file(), String.class).get(9900)).isEqualTo("x100");
		assertThat(FileLazyAssa.of(file(), String.class).get(0)).isEqualTo("x10000");
	}

	private File file() {
		return new File("test.assa");
	}

	private FileOutputStream output() {
		try {
			return new FileOutputStream(file());
		} catch (FileNotFoundException e) {
			return null;
		}
	}
}
