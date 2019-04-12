import io.intino.alexandria.mapp.MappReader;
import io.intino.alexandria.tabb.ColumnStream.Type;
import io.intino.alexandria.tabb.MappColumnStream;
import io.intino.alexandria.tabb.TabbBuilder;
import io.intino.alexandria.tabb.TabbReader;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static io.intino.alexandria.tabb.ColumnStream.Type.Nominal;
import static org.assertj.core.api.Assertions.assertThat;

public class TabbBuilder_ {

	private File tabbDirectory = new File("test-res/tabbs/");
	private File mapps = new File("test-res/mapps");

	@Test
	public void should_read_empty_tabb() throws IOException {
		TabbBuilder builder = new TabbBuilder();
		builder.save(tabbDirectory, "result");
		assertThat(!resultTabbFile().exists());
	}

	@Test
	public void should_build_a_column() throws IOException {
		TabbBuilder builder = new TabbBuilder();
		builder.add(new MappColumnStream(new MappReader(new File(mapps, "test1.mapp")), Nominal));
		builder.save(tabbDirectory, "result");
		assertThat(tabbDirectory.length()).isEqualTo(192);
		TabbReader tabb = tabb();
		assertThat(tabb.size()).isEqualTo(3);
		tabb.next();
		TabbReader.Value value = tabb().get(0);
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
		builder.add(new MappColumnStream(new MappReader(new File(mapps, "many_values.mapp")), Nominal));
		builder.save(tabbDirectory, "result");
		assertThat(tabbDirectory.length()).isEqualTo(212644);
		TabbReader tabb = tabb();
		assertThat(tabb.size()).isEqualTo(50000);
		tabb.next();
		TabbReader.Value value = tabb().get(0);
		assertThat(value).isNotNull();
		assertThat(value.isAvailable());
		assertThat(value.type()).isEqualTo(Type.Nominal);
		int i = 0;
		while (tabb.hasNext()) {
			assertThat(value.asString()).isEqualTo("test" + i);
			i++;
		}
	}

//	@Test
//	public void should_read_a_column_with_multiple_values() throws IOException {
//		TabbBuilder builder = new TabbBuilder();
//		builder.add("multivalued", Nominal).with(new MappReader(new File(mapps, "multivalued.mapp")));
//		builder.save(tabbDirectory);
//		assertThat(tabbDirectory.length()).isEqualTo(309L);
//		Tabb tabb = tabb();
//		assertThat(tabb.size()).isEqualTo(2);
//		Tabb.Column firstColumn = tabb().iterator().next();
//		assertThat(firstColumn).isNotNull();
//		assertThat(firstColumn.name).isEqualTo("multivalued");
//		assertThat(firstColumn.type).isEqualTo(Nominal);
//		DataInputStream data = firstColumn.data;
//		assertThat(data).isNotNull();
//		assertThat(firstColumn.mode[data.readInt()]).isEqualTo("test1");
//		assertThat(firstColumn.mode[data.readInt()]).isEqualTo("test2");
//		assertThat(data.available()).isZero();
//	}
//
//	@Test
//	public void should_build_multiple_integer_column() throws IOException {
//		File file = new File(mapps, "integer.mapp");
//		MappBuilder test = new MappBuilder();
//		test.put(1L, "134");
//		test.put(2L, "182");
//		test.put(3L, "233");
//		test.save(file);
//		TabbBuilder builder = new TabbBuilder();
//		builder.add("i", Type.Integer).with(new MappReader(new File(mapps, "integer.mapp")));
//		builder.add("i", Type.Integer).with(new ColumnStream<Integer>() {
//			@Override
//			public Item<IntegerStream> next() {
//				return new Item<IntegerStream>() {
//					@Override
//					public long key() {
//						return 0;
//					}
//
//					@Override
//					public IntegerStream value() {
//						return null;
//					}
//				};
//			}
//
//			@Override
//			public boolean hasNext() {
//				return false;
//			}
//
//			@Override
//			public void close() {
//
//			}
//		});
//		builder.add("d", Type.Double).with(new MappReader(new File(mapps, "integer.mapp")));
//		builder.add("n", Nominal).with(new MappReader(new File(mapps, "integer.mapp")));
//		builder.save(tabbDirectory);
//	}
//
//
//	@Test
//	public void should_build_multiple_nominal_column() throws IOException {
//		TabbBuilder builder = new TabbBuilder();
//		builder.add("test1", Nominal).with(new MappReader(new File(mapps, "test1.mapp")));
//		builder.add("test2", Nominal).with(new MappReader(new File(mapps, "test2.mapp")));
//		builder.save(tabbDirectory);
//		assertThat(tabbDirectory.length()).isEqualTo(420);
//		Tabb tabb = tabb();
//		assertThat(tabb.size()).isEqualTo(3);
//		Iterator<Tabb.Column> iterator = tabb().iterator();
//		Tabb.Column firstColumn = iterator.next();
//		assertThat(firstColumn).isNotNull();
//		assertThat(firstColumn.name).isEqualTo("test1");
//		assertThat(firstColumn.type).isEqualTo(Nominal);
//		DataInputStream data = new DataInputStream(firstColumn.data);
//		assertThat(data).isNotNull();
//		assertThat(firstColumn.mode[data.readInt()]).isEqualTo("test1");
//		assertThat(firstColumn.mode[data.readInt()]).isEqualTo("test2");
//		assertThat(firstColumn.mode[data.readInt()]).isEqualTo("test1");
//		assertThat(data.available()).isZero();
//
//		Tabb.Column secondColumn = iterator.next();
//		assertThat(secondColumn).isNotNull();
//		assertThat(secondColumn.name).isEqualTo("test2");
//		assertThat(secondColumn.type).isEqualTo(Nominal);
//		data = new DataInputStream(secondColumn.data);
//		assertThat(data).isNotNull();
//		assertThat(data.readInt()).isEqualTo(ColumnSerializer.NaInt);
//		int i1 = data.readInt();
//		assertThat(secondColumn.mode[i1]).isEqualTo("a");
//		assertThat(secondColumn.mode[data.readInt()]).isEqualTo("b");
//		assertThat(data.available()).isZero();
//	}


	private TabbReader tabb() throws IOException {
		return new TabbReader(resultTabbFile());
	}

	private File resultTabbFile() {
		return new File(tabbDirectory, "result.tabb");
	}

	@After
	public void tearDown() throws Exception {
//		tabbDirectory.delete();
	}

	private ByteArrayOutputStream output() {
		return new ByteArrayOutputStream();
	}
}
