import io.intino.tara.io.Stash;
import io.intino.tara.io.StashDeserializer;
import org.junit.Test;

import java.io.File;

public class TomlLoader {
	@Test
	public void assertLoadToml() throws Exception {
		final Builder from = Builder.from(Builder.class.getResourceAsStream("pandora-test.toml"));
		System.out.println(from.name);
	}


	@Test
	public void stashRead() throws Exception {
		final Stash stash = StashDeserializer.stashFrom(new File(this.getClass().getResource("Pandora.stash").toURI().toURL().getFile()));
		System.out.println(stash.language);
	}
}
