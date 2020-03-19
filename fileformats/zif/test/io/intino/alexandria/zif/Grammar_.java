package io.intino.alexandria.zif;

import io.intino.alexandria.zif.grammar.PropertyChecker;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Grammar_ {

	@Test
	public void should_parse_sequence_of_tokens() {
		assertThat(new PropertyChecker.Grammar("A | B").parse("A")).isEqualTo(null);
		assertThat(new PropertyChecker.Grammar("A | B").parse("B")).isEqualTo(null);
		assertThat(new PropertyChecker.Grammar("A | B").parse("C")).isEqualTo("'C' is an invalid constant");
		assertThat(new PropertyChecker.Grammar("A | B").parse("")).isEqualTo("Expected A | B");
		assertThat(new PropertyChecker.Grammar("A C | A B D").parse("A")).isEqualTo("Expected C | B" );
		assertThat(new PropertyChecker.Grammar("A C | B D").parse("B")).isEqualTo("Expected D" );
		assertThat(new PropertyChecker.Grammar("A C | B D").parse("A E")).isEqualTo("'E' is an invalid constant");
		assertThat(new PropertyChecker.Grammar("A C | B D").parse("A C E")).isEqualTo("'E' is an invalid constant");
	}

	@Test
	public void should_parse_variables() {
		assertThat(new PropertyChecker.Grammar("#email").parse("j@gmail.com")).isEqualTo(null);
		assertThat(new PropertyChecker.Grammar("#email").parse("")).isEqualTo("Expected #email");
		assertThat(new PropertyChecker.Grammar("#email").parse("2019X010")).isEqualTo("'2019X010' is an invalid email");
		assertThat(new PropertyChecker.Grammar("#date").parse("2019-010")).isEqualTo("'2019-010' is an invalid date");
		assertThat(new PropertyChecker.Grammar("desde #date").parse("desde 20191010")).isEqualTo(null);
		assertThat(new PropertyChecker.Grammar("desde #date | #email").parse("desde")).isEqualTo("Expected #date");
		assertThat(new PropertyChecker.Grammar("desde #date | desde #email").parse("desde")).isEqualTo("Expected #date | #email");
		assertThat(new PropertyChecker.Grammar("desde #date | desde #email").parse("desde 20191010")).isEqualTo(null);
		assertThat(new PropertyChecker.Grammar("desde #date | desde #email").parse("desde j@gmail.com")).isEqualTo(null);
		assertThat(new PropertyChecker.Grammar("desde #date").parse("desde 20191010 h")).isEqualTo("'h' is an invalid constant");
	}
}
