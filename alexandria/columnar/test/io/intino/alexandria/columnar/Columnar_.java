package io.intino.alexandria.columnar;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.assa.Assa;
import io.intino.alexandria.assa.loaders.FileLazyAssa;
import io.intino.alexandria.zet.Zet;
import io.intino.alexandria.zet.ZetReader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class Columnar_ {
	private Columnar columnar;
	private File result;


	@Before
	public void setUp() throws Exception {
		columnar = new Columnar(new File("test-res/columnar"));
		result = new File("test-res/result");
	}


	@Test
	public void should_load_assa_from_one_zet() throws IOException {
		columnar.load("single").from(new File("test-res/zets/single"));
	}

	@Test
	public void should_export_column_to_csv() throws IOException, ClassNotFoundException {
		columnar.select("single").from(new Timetag("201810")).intoCSV(new File(result, "201810.csv"));
	}


//	AssaStream.Merge.of(zetsOf(directory)).save("single", new File("single.assa"));
//	Assa<String> assa = FileLazyAssa.of(new File("single.assa"), String.class);
//	Zet zet = new Zet(new ZetReader(new File(directory, "one.zet")));
//	assertThat(assa.name()).isEqualTo("single");
//	assertThat(assa.size()).isEqualTo(zet.size());
//	assertThat(assa.get(1200404L)).isNull();
//		for (long id : zet.ids()) {
//		assertThat(assa.get(id)).isNotNull();
//		assertThat(assa.get(id)).isEqualTo("one");
//	}

	@Test
	public void should_build_assa_from_multiple_zets() throws IOException, ClassNotFoundException {
		File directory = new File("test-res/zets/multiple");
//		AssaStream.Merge.of(zetsOf(directory)).save("multiple", new File("multiple.assa"));
		Assa<String> assa = FileLazyAssa.of(new File("multiple.assa"), String.class);
		assertThat(assa.name()).isEqualTo("multiple");
		assertThat(assa.size()).isEqualTo(7943);
		for (String name : new String[]{"one", "two"}) {
			Zet zet = new Zet(new ZetReader(new File(directory, name + ".zet")));
			for (long id : zet.ids()) {
				assertThat(assa.get(id)).isNotNull();
				assertThat(assa.get(id)).isEqualTo(name);
			}
		}
	}


}
