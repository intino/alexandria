package io.intino.alexandria.assa;

import io.intino.alexandria.zet.Zet;
import io.intino.alexandria.zet.ZetReader;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class AssaBuilder_ {

	@Test
	public void should_build_assa_from_zet() throws IOException {
		File directory = new File("test-res/Circuito.Codigo/201811/");
		AssaBuilder<Serializable> builder = new AssaBuilder<>("Circuito.Codigo");
		for (File zetFile : Objects.requireNonNull(directory.listFiles(f -> f.getName().endsWith(".zet")))) {
			String zetName = zetFile.getName().replace(".zet", "");
			ZetReader reader = new ZetReader(zetFile);
			builder.put(new Zet(reader).ids(), zetName);
		}
		File file = new File("test-res/circuito-codigo.assa");
		builder.save(file);
	}

	@Test
	public void should_read_assa_with_one_entry_as_stream() {
		File file = new File("test-res/circuito-codigo.assa");
		AssaReader<String> reader = new AssaReader<>(file);
		assertThat(reader.size()).isEqualTo(1);
		assertThat(reader.hasNext()).isEqualTo(true);
		AssaStream.Item<String> next = reader.next();
		assertThat(next.key()).isEqualTo(0);
		assertThat(next.object()).isEqualTo("ACO53015");
	}

	@Test
	public void should_build_assa_from_two_zets() throws IOException {
		File directory = new File("test-res/Circuito.Codigo2/201811/");
		AssaBuilder<Serializable> builder = new AssaBuilder<>("Circuito.Codigo");
		for (File zetFile : Objects.requireNonNull(directory.listFiles(f -> f.getName().endsWith(".zet")))) {
			String zetName = zetFile.getName().replace(".zet", "");
			ZetReader reader = new ZetReader(zetFile);
			long[] ids = new Zet(reader).ids();
			builder.put(ids, zetName);
		}
		File file = new File("test-res/circuito-codigo2.assa");
		builder.save(file);
	}

	@Test
	public void should_read_assa_with_two_entries_as_stream() {
		File file = new File("test-res/circuito-codigo2.assa");
		AssaReader<String> reader = new AssaReader<>(file);
		assertThat(reader.size()).isEqualTo(2);
		assertThat(reader.hasNext()).isEqualTo(true);
		AssaStream.Item<String> next = reader.next();
		assertThat(next.key()).isEqualTo(0);
		assertThat(next.object()).isEqualTo("ACO53015");
		assertThat(reader.hasNext()).isEqualTo(true);
		next = reader.next();
		assertThat(next.key()).isEqualTo(1);
		assertThat(next.object()).isEqualTo("ACO53025");
	}
}
