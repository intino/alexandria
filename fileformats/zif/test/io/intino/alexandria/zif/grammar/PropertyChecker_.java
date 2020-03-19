package io.intino.alexandria.zif.grammar;

import io.intino.alexandria.zif.Grammar;
import io.intino.alexandria.zif.Zif;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyChecker_ {
	private Grammar grammar;

	public static final String ZifFile = "test-res/example.zif";

	@Before
	public void setup() throws IOException {
		grammar = new Grammar(new File(ZifFile));
	}

	@Test
	public void should_check_without_zif_file() {
		assertThat(propertyCheckerOf("id/rpe").check("11 11")).isEqualTo("Identifier can not contain blank spaces");
		assertThat(propertyCheckerOf("id/email").check("11 11")).isEqualTo("Identifier can not contain blank spaces");
		assertThat(propertyCheckerOf("id/email").check("ff")).isEqualTo("'ff' is an invalid email");
		assertThat(propertyCheckerOf("id/email").check("a@ho.com")).isEqualTo(null);
		assertThat(propertyCheckerOf("feature/division").check("da")).isEqualTo(null);
		assertThat(propertyCheckerOf("feature/division").check("da\ndb")).isEqualTo(null);
		assertThat(propertyCheckerOf("feature/division").check("da\ndb\nee")).isEqualTo("'ee' is an invalid constant in line 3");
	}

	@Test
	public void should_check_with_zif_file() throws IOException {
		assertThat(propertyCheckerOf("id/rpe").set(zif()).check("12345")).isEqualTo(null);
	}

	private Zif zif() throws IOException {
		return new Zif(new File("test-res/example.zif"));
	}

	private PropertyChecker propertyCheckerOf(String name) {
		return new PropertyChecker(propertyOf(name));
	}

	private Property propertyOf(String name) {
		return grammar.get(name);
	}
}
