import org.junit.Test;

public class TomlLoader {
	@Test
	public void assertLoadToml() throws Exception {
		final Builder from = Builder.from(Builder.class.getResourceAsStream("pandora-test.toml"));
		System.out.println(from.name);
	}
}
