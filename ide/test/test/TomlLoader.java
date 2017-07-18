import io.intino.tara.io.Stash;
import io.intino.tara.io.StashDeserializer;
import org.junit.Test;

import java.io.File;

public class TomlLoader {
	@Test
	public void assertLoadToml() throws Exception {
		final Builder from = Builder.from(Builder.class.getResourceAsStream("konos-test.toml"));
		System.out.println(from.name);
	}


	@Test
	public void stashRead() throws Exception {
		final Stash stash = StashDeserializer.stashFrom(new File(this.getClass().getResource("Konos.stash").toURI().toURL().getFile()));
		System.out.println(stash.language);
	}

	@Test
	public void name() throws Exception {
		String regex = "([a-z])([A-Z]+)";
		String replacement = "$1-$2";
		System.out.println("CamelCaseToSomethingElse"
				.replaceAll(regex, replacement)
				.toLowerCase());
	}
}
