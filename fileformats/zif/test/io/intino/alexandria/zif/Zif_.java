package io.intino.alexandria.zif;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class Zif_ {

	@Before
	public void setup() throws IOException {
		String[][] data = new String[][] {
				new String[] {"884d6028-64fd-40f4-9e71-d6d43a4ae18e", "identifier/email", "barak@gmail.com"},
				new String[] {"884d6028-64fd-40f4-9e71-d6d43a4ae18e", "identifier/rpe", "239450391230"},
				new String[] {"884d6028-64fd-40f4-9e71-d6d43a4ae18e", "identifier/dni", "44201034R"},
				new String[] {"884d6028-64fd-40f4-9e71-d6d43a4ae18e", "identifier/name", "Barack Obama"},
				new String[] {"884d6028-64fd-40f4-9e71-d6d43a4ae18e", "feature/photo", "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"},
				new String[] {"884d6028-64fd-40f4-9e71-d6d43a4ae18e", "feature/skill/english", "c1"},
				new String[] {"884d6028-64fd-40f4-9e71-d6d43a4ae18e", "feature/lang", "es,en"},
				new String[] {"884d6028-64fd-40f4-9e71-d6d43a4ae18e", "feature/skill/english", "c2"},
				new String[] {"884d6028-64fd-40f4-9e71-d6d43a4ae18e", "feature/status", "enabled"},
				new String[] {"884d6028-64fd-40f4-9e71-d6d43a4ae18e", "role/president", "assigned from 20140101 until 20161231"},
				new String[] {"884d6028-64fd-40f4-9e71-d6d43a4ae18e", "role/executive", "assigned from 20140101 until 20161231"},
				new String[] {"884d6028-64fd-40f4-9e71-d6d43a4ae18e", "feature/status", "disabled"},
				new String[] {"8885e118-537a-4628-a607-b10672103a8f", "identifier/email", "pedrosanchez@gmail.com"},
				new String[] {"8885e118-537a-4628-a607-b10672103a8f", "feature/status", "enabled"},
				new String[] {"8885e118-537a-4628-a607-b10672103a8f", "role/president", "assigned"}
		};
		Zif zif = new Zif();
		for (String[] line : data)
			zif.append(line[0], line[1], line[2]);
		zif.save(new File("example.zif"));
	}

	@Test
	public void should_get_by_id() throws IOException {
		Zif zif = new Zif(new File("example.zif"));
		assertThat(zif.get("884d6028-64fd-40f4-9e71-d6d43a4ae18e").size()).isEqualTo(12);
		for (Zif.Assertion assertion : zif.get("884d6028-64fd-40f4-9e71-d6d43a4ae18e")) {
			assertThat(assertion.id()).isEqualTo("884d6028-64fd-40f4-9e71-d6d43a4ae18e");
		}
	}

	@Test
	public void should_search_by_identifier() throws IOException {
		Zif zif = new Zif(new File("example.zif"));
		assertThat(zif.search("c2").size()).isEqualTo(0);
		assertThat(zif.search("44201034R").contains("884d6028-64fd-40f4-9e71-d6d43a4ae18e")).isTrue();
		assertThat(zif.search("239450391230").contains("884d6028-64fd-40f4-9e71-d6d43a4ae18e")).isTrue();
	}

	@Test
	public void should_search_by_property() throws IOException {
		Zif zif = new Zif(new File("example.zif"));
		assertThat(zif.search("identifier/dni", is("44201034R")).contains("884d6028-64fd-40f4-9e71-d6d43a4ae18e")).isTrue();
		assertThat(zif.search("identifier/rpe", is("239450391230")).contains("884d6028-64fd-40f4-9e71-d6d43a4ae18e")).isTrue();
		assertThat(zif.search("feature/status", is("enabled")).contains("884d6028-64fd-40f4-9e71-d6d43a4ae18e")).isFalse();
		assertThat(zif.search("feature/status", is("enabled")).contains("8885e118-537a-4628-a607-b10672103a8f")).isTrue();
		assertThat(zif.search("role/president", isEnabled("20191020")).size()).isEqualTo(1);
		assertThat(zif.search("role/president", isEnabled("20141020")).size()).isEqualTo(2);
		assertThat(zif.search("feature/skill/english", is("c1")).size()).isEqualTo(0);
		assertThat(zif.search("feature/skill/english", is("c2")).contains("884d6028-64fd-40f4-9e71-d6d43a4ae18e")).isTrue();
	}

	@Test
	public void should_describe_query() throws IOException {
		String barack = "" +
				"[884d6028-64fd-40f4-9e71-d6d43a4ae18e]\n" +
				"email: barak@gmail.com\n" +
				"rpe: 239450391230\n" +
				"dni: 44201034R\n" +
				"name: Barack Obama\n";
		String pedro = "" +
				"[8885e118-537a-4628-a607-b10672103a8f]\n" +
				"email: pedrosanchez@gmail.com\n";
		Zif zif = new Zif(new File("example.zif"));
		assertThat(zif.search("44201034R").describe("identifier/")).isEqualTo(barack);
		assertThat(zif.search("role/president", isEnabled("20150101")).describe("")).isEqualTo("" +
				"[884d6028-64fd-40f4-9e71-d6d43a4ae18e]\n" +
				"identifier/email: barak@gmail.com\n" +
				"identifier/rpe: 239450391230\n" +
				"identifier/dni: 44201034R\n" +
				"identifier/name: Barack Obama\n" +
				"feature/photo: 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\n" +
				"feature/skill/english: c1\n" +
				"feature/lang: es,en\n" +
				"feature/skill/english: c2\n" +
				"feature/status: enabled\n" +
				"role/president: assigned from 20140101 until 20161231\n" +
				"role/executive: assigned from 20140101 until 20161231\n" +
				"feature/status: disabled\n" +
				"\n" +
				"[8885e118-537a-4628-a607-b10672103a8f]\n" +
				"identifier/email: pedrosanchez@gmail.com\n" +
				"feature/status: enabled\n" +
				"role/president: assigned\n");
		assertThat(zif.search("role/president", isEnabled("20150101")).describe("identifier/")).isEqualTo(barack + "\n" + pedro);
	}

	private Zif.Condition isEnabled(String on) {
		return a-> {
			String[] d = a.value().split(" ");
			if (!d[0].equals("assigned")) return false;
			int index = 1;
			boolean result = true;
			while (index < d.length) {
				if (d[index].equals("from")) result &= on.compareTo(d[index + 1]) >= 0;
				if (d[index].equals("until")) result &= d[index+1].compareTo(on) >= 0;
				index += 2;
			}
			return result;
		};
	}

	private Zif.Condition is(String v) {
		return a -> a.value().equals(v);
	}
}
