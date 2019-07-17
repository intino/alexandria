package io.intino.test;

import io.intino.alexandria.mapp.MappReader;
import io.intino.alexandria.tabb.ColumnStream;
import io.intino.alexandria.tabb.ColumnStream.Type;
import io.intino.alexandria.tabb.TabbBuilder;
import io.intino.alexandria.tabb.TabbReader;
import io.intino.alexandria.tabb.streamers.MappColumnStreamer;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static io.intino.alexandria.tabb.ColumnStream.Type.Nominal;
import static org.assertj.core.api.Assertions.assertThat;

public class TabbBuilder_ {

	private File tabbDirectory = new File("test-res/tabbs/");
	private File mapps = new File("test-res/mapps");

	@Test
	public void should_read_empty_tabb() throws IOException {
		TabbBuilder builder = new TabbBuilder();
		builder.save(new File(tabbDirectory, "result.tabb"));
		assertThat(!resultTabbFile().exists());
	}

	@Test
	public void should_build_a_column() throws IOException {
		TabbBuilder builder = new TabbBuilder();
		MappColumnStreamer streamer = new MappColumnStreamer(new MappReader(new File(mapps, "tests.mapp")));
		streamer.add(new MappColumnStreamer.BypassSelector("tests", Nominal));
		builder.add(streamer.get());
		builder.save(new File(tabbDirectory, "result.tabb"));
		TabbReader tabb = tabbReader();
		assertThat(tabb.size()).isEqualTo(3);
		tabb.next();
		TabbReader.Value value = tabbReader().get(0);
		assertThat(value).isNotNull();
		assertThat(value.isAvailable());
		assertThat(value.type()).isEqualTo(Type.Nominal);
		assertThat(value.asString()).isEqualTo("test1");
		tabb.next();
		value = tabb.get(0);
		assertThat(value.asString()).isEqualTo("test2");
		tabb.next();
		value = tabb.get(0);
		assertThat(value.asString()).isEqualTo("test1");
	}

	@Test
	public void should_read_a_column_with_many_values() throws IOException {
		TabbBuilder builder = new TabbBuilder();
		MappColumnStreamer streamer = new MappColumnStreamer(new MappReader(new File(mapps, "many_values.mapp")));
		streamer.add(new MappColumnStreamer.BypassSelector("many_values", Nominal));
		builder.add(streamer.get());
		builder.save(new File(tabbDirectory, "result.tabb"));
		TabbReader tabb = tabbReader();
		assertThat(tabb.size()).isEqualTo(50000);
		int i = 0;
		while (tabb.hasNext()) {
			tabb.next();
			TabbReader.Value value = tabb.get(0);
			assertThat(value.mode().features.length).isEqualTo(50000);
			assertThat(value).isNotNull();
			assertThat(value.isAvailable());
			assertThat(value.type()).isEqualTo(Type.Nominal);
			assertThat(value.asString()).isEqualTo("test" + i);
			i++;
		}
	}

	@Test
	public void should_read_a_column_with_multiple_values() throws IOException {
		MappColumnStreamer streamer = new MappColumnStreamer(new MappReader(new File(mapps, "multivalued.mapp")));
		streamer.add(new MappColumnStreamer.BypassSelector("multivalued", Nominal));
		TabbBuilder builder = new TabbBuilder();
		builder.add(streamer.get());
		builder.save(new File(tabbDirectory, "result.tabb"));
		TabbReader tabb = tabbReader();
		tabb.next();
		TabbReader.Value value = tabb.get(0);
		assertThat(value).isNotNull();
		assertThat(value.type()).isEqualTo(Nominal);
		assertThat(value.isAvailable());
		assertThat(value.asString()).isEqualTo("test1");
		tabb.next();
		value = tabb.get(0);
		assertThat(value.asString()).isEqualTo("test2");
		assertThat(!tabb.hasNext());

	}

	@Test
	public void should_build_multiple_integer_big_column() throws IOException {
		TabbBuilder builder = new TabbBuilder();
		builder.add(integerStream("i", 10000000));
		builder.add(integerStream("j", 10000000));
		builder.save(new File(tabbDirectory, "result.tabb"));
		TabbReader tabb = tabbReader();
		assertThat(tabb.size()).isEqualTo(10000000);
		System.out.println("Reading...");
		int i = 0;
		while (tabb.hasNext()) {
			tabb.next();
			TabbReader.Value value = tabb.get(0);
			assertThat(value).isNotNull();
			assertThat(value.isAvailable());
			assertThat(value.type()).isEqualTo(Type.Integer);
			assertThat(value.asInteger()).isLessThan(200);
			i++;
		}
		assertThat(i).isEqualTo(10000000);
	}


	@Test
	public void should_build_multiple_nominal_column() throws IOException {
		TabbBuilder builder = new TabbBuilder();
		MappColumnStreamer streamer = new MappColumnStreamer(new MappReader(new File(mapps, "tests.mapp")));
		streamer.add(new MappColumnStreamer.BypassSelector("tests", Nominal));
		builder.add(streamer.get());
		streamer = new MappColumnStreamer(new MappReader(new File(mapps, "letters.mapp")));
		streamer.add(new MappColumnStreamer.BypassSelector("letters", Nominal));
		builder.add(streamer.get());
		builder.save(new File(tabbDirectory, "result.tabb"));
		assertThat(resultTabbFile().length()).isEqualTo(435);
		TabbReader tabbReader = tabbReader();

		tabbReader.next();
		TabbReader.Value value1 = tabbReader.get(0);
		TabbReader.Value value2 = tabbReader.get(1);
		assertThat(value1).isNotNull();
		assertThat(value1.isAvailable());
		assertThat(value1.type()).isEqualTo(Nominal);
		assertThat(value1.asString()).isEqualTo("test1");
		assertThat(value2).isNotNull();
		assertThat(!value2.isAvailable());
		assertThat(value2.type()).isEqualTo(Nominal);

		tabbReader.next();
		value1 = tabbReader.get(0);
		value2 = tabbReader.get(1);
		assertThat(value1.asString()).isEqualTo("test2");
		assertThat(value2.asString()).isEqualTo("a");

		tabbReader.next();
		value1 = tabbReader.get(0);
		value2 = tabbReader.get(1);
		assertThat(value1.asString()).isEqualTo("test1");
		assertThat(value2.asString()).isEqualTo("b");
		assertThat(!tabbReader.hasNext());
	}

	private ColumnStream integerStream(String name, int size) {
		return new ColumnStream() {
			int key = 0;
			Random random = new Random();
			int current = random.nextInt(200);

			@Override
			public String name() {
				return name;
			}

			@Override
			public Type type() {
				return Type.Integer;
			}

			@Override
			public boolean hasNext() {
				return key < size;
			}

			@Override
			public void next() {
				key++;
				current = random.nextInt(200);
			}

			@Override
			public Long key() {
				return (long) key;
			}

			@Override
			public Object value() {
				return current;
			}
		};
	}


	private TabbReader tabbReader() throws IOException {
		return new TabbReader(resultTabbFile());
	}

	private File resultTabbFile() {
		return new File(tabbDirectory, "result.tabb");
	}

	@After
	public void tearDown() throws Exception {
//		resultTabbFile().delete();
	}

	private ByteArrayOutputStream output() {
		return new ByteArrayOutputStream();
	}
}
