package io.test;

import io.intino.alexandria.sumus.Attribute;
import io.intino.alexandria.sumus.ledgers.columnar.Column;
import org.junit.Test;

import static io.intino.alexandria.sumus.Attribute.Type.*;
import static org.assertj.core.api.Assertions.assertThat;

public class Column_ {

	@Test
	public void should_contain_texts() {
		Column column = new Column(attribute("id", label), "1 ","  "," 3 ","4",null,"A");
		assertThat(column.name()).isEqualTo("id");
		assertThat(column.size()).isEqualTo(6);
		assertThat(column.hasNA()).isTrue();
		assertThat(column.value(0)).isEqualTo("1");
		assertThat(column.value(1)).isEqualTo(null);
		assertThat(column.value(2)).isEqualTo("3");
		assertThat(column.value(3)).isEqualTo("4");
		assertThat(column.value(4)).isEqualTo(null);
		assertThat(column.value(5)).isEqualTo("A");
	}

	@Test
	public void should_contain_integers() {
		Column column = new Column(attribute("id", integer), "1 ","  "," 3 ","4",null,"A");
		assertThat(column.name()).isEqualTo("id");
		assertThat(column.size()).isEqualTo(6);
		assertThat(column.hasNA()).isTrue();
		assertThat(column.value(0)).isEqualTo(1L);
		assertThat(column.value(1)).isEqualTo(null);
		assertThat(column.value(2)).isEqualTo(3L);
		assertThat(column.value(3)).isEqualTo(4L);
		assertThat(column.value(4)).isEqualTo(null);
		assertThat(column.value(5)).isEqualTo(null);
	}

	@Test
	public void should_contain_numbers() {
		Column column = new Column(attribute("id",number), "1.0 "," 0.5", "","3","4.5");
		assertThat(column.name()).isEqualTo("id");
		assertThat(column.size()).isEqualTo(5);
		assertThat(column.hasNA()).isTrue();
		assertThat(column.value(0)).isEqualTo(1.);
		assertThat(column.value(1)).isEqualTo(0.5);
		assertThat(column.value(2)).isEqualTo(null);
		assertThat(column.value(3)).isEqualTo(3.);
		assertThat(column.value(4)).isEqualTo(4.5);
	}

	@Test
	public void should_contain_categories() {
		Column column = new Column(attribute("type", category), "A","B","A","C","B ",null, "","  ");
		assertThat(column.name()).isEqualTo("type");
		assertThat(column.size()).isEqualTo(8);
		assertThat(column.hasNA()).isTrue();
		assertThat(column.value(0).toString()).isEqualTo("1:A");
		assertThat(column.value(1).toString()).isEqualTo("2:B");
		assertThat(column.value(2).toString()).isEqualTo("1:A");
		assertThat(column.value(3).toString()).isEqualTo("3:C");
		assertThat(column.value(4).toString()).isEqualTo("2:B");
		assertThat(column.value(5)).isEqualTo(null);
		assertThat(column.value(6)).isEqualTo(null);
		assertThat(column.value(7)).isEqualTo(null);

		Column column2 = new Column(attribute("type", category), "","B");
		assertThat(column2.size()).isEqualTo(2);
		assertThat(column2.value(0)).isEqualTo(null);
		assertThat(column2.value(1).toString()).isEqualTo("1:B");

	}

	@Test
	public void should_contain_dates() {
		Column column = new Column(attribute("date", date), "20200101","2020/01/01","2020-01-01","2022 10 10","2020.05.01");
		assertThat(column.name()).isEqualTo("date");
		assertThat(column.size()).isEqualTo(5);
		assertThat(column.value(0)).isEqualTo(18262L);
		assertThat(column.value(1)).isEqualTo(18262L);
		assertThat(column.value(2)).isEqualTo(18262L);
		assertThat(column.value(3)).isEqualTo(19275L);
		assertThat(column.value(4)).isEqualTo(18383L);
	}

	private Attribute attribute(String name, Attribute.Type type) {
		return new Attribute(name,type);
	}

}
