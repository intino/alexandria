import io.intino.konos.datalake.Ness;
import org.junit.Test;

import java.nio.file.Files;

public class NessWrapper {

	@Test
	public void shouldStartNess() throws Exception {
		final Ness ness = new Ness(Files.createTempDirectory("-").toFile(), "xoxb-162074419812-gB5oNUwzxGWQ756TrRyu1Ii9");
		ness.start();
		Thread.sleep(10000);
	}
}
