import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.zit.ItsReader;
import io.intino.alexandria.zit.ZitStream;
import io.intino.alexandria.zit.ZitWriter;
import io.intino.alexandria.zit.model.Data;
import io.intino.alexandria.zit.model.Period;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.lang.String.join;
import static java.util.stream.Collectors.joining;

public class ItsReaderTest {
	private ByteArrayInputStream inputStream;

	@Before
	public void setUp() throws Exception {
		inputStream = readSource(new File("test-res/kraken.its.zip"));
	}

	@Test
	public void should_read_its() {
		System.out.println(read(inputStream));
	}

	private String read(InputStream inputStream) {
		final ItsReader reader = new ItsReader(inputStream);
		StringBuilder builder = new StringBuilder();
		builder.append(reader.sensor()).append("\n");
		reader.data().limit(50)
				.forEach(d -> print(builder, d));
		return builder.toString();
	}

	private static void print(StringBuilder builder, Data d) {
		builder.append(join("\t", d.ts().toString(), join(", ", d.measurements()), Arrays.stream(d.values()).mapToObj(Double::toString).collect(joining(", "))))
				.append("\n");
	}

	@Test
	public void should_write_read_zit() throws IOException {
		StringBuilder strBuilder = new StringBuilder();
		final ItsReader reader = new ItsReader(inputStream);
		final File tempFile = Files.createTempFile("test", ".zit").toFile();
		tempFile.deleteOnExit();
		try (ZitWriter writer = new ZitWriter(tempFile, reader.sensor(), Period.of(5, ChronoUnit.MINUTES), new String[]{"price", "volume"})) {
			reader.data().forEach(d -> writer.put(d.ts(), d.values()));
		} catch (Exception e) {
			Logger.error(e);
		}
		ZitStream zitStream = ZitStream.of(tempFile);
		strBuilder.append(zitStream.sensor()).append("\n");
		zitStream.limit(50)
				.forEach(d -> print(strBuilder, d));
		inputStream.reset();
		Assert.assertEquals(read(inputStream), strBuilder.toString());
	}

	private static ByteArrayInputStream readSource(File file) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		ZipEntry zipEntry = zipFile.entries().nextElement();
		InputStream inputStream = zipFile.getInputStream(zipEntry);
		ByteArrayInputStream stream = new ByteArrayInputStream(inputStream.readAllBytes());
		zipFile.close();
		return stream;
	}
}
