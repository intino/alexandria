import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.zit.ItsReader;
import io.intino.alexandria.zit.ZitBuilder;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ItsReaderTest {
	@Test
	public void should_read_its() throws FileNotFoundException {
		final ItsReader reader = new ItsReader(new FileInputStream("test-res/kraken.its"));
		System.out.println(reader.sensor());
		reader.data().limit(50)
				.forEach(d -> System.out.println(String.join("\t", d.ts().toString(), Arrays.stream(d.values()).mapToObj(Double::toString).collect(Collectors.joining(", ")))));
	}


	@Test
	public void should_write_zit() throws IOException {
		final ItsReader reader = new ItsReader(new FileInputStream("test-res/kraken.its"));
		System.out.println(reader.sensor());
		final Path tempFile = Files.createTempFile("test", "zit");
		tempFile.toFile().deleteOnExit();
		try (ZitBuilder builder = new ZitBuilder(tempFile.toFile(), reader.sensor())) {
			reader.data().forEach(d -> {
				builder.put(d.values());
			});

		} catch (Exception e) {
			Logger.error(e);
		}
	}
}
