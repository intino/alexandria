import io.intino.alexandria.ness.Scale;
import io.intino.alexandria.ness.setstore.session.SessionFileReader;
import io.intino.alexandria.ness.setstore.session.SessionFileWriter;
import io.intino.alexandria.nessaccesor.NessAccessor.SetStore.Timetag;
import io.intino.sezzet.operators.SetStream;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SessionTest {

	@Test
	public void should_write_and_read_several_records() {
		File file = new File("temp/test.setfs");
		file.getParentFile().mkdirs();
		Timetag timetag = new Timetag(Instant.now(), Scale.Month);
		try {
			SessionFileWriter writer = new SessionFileWriter(new FileOutputStream(file));
			for (int i = 0; i < 20; i++) writer.add("tank", timetag.toString(), "set", i);
			for (int i = 0; i < 20; i++) writer.add("tank2", timetag.toString(), "set2", i);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			SessionFileReader reader = new SessionFileReader(file);
			assertEquals(2, reader.chunks().size());
			assertEquals(1, reader.chunks("tank/" + timetag.toString() + "/set").size());
			SetStream stream = reader.chunks("tank/" + timetag.toString() + "/set").get(0).stream();
			for (int i = 0; i < 20; i++) assertEquals((long) i, stream.next());
			assertFalse(stream.hasNext());
			assertEquals(1, reader.chunks("tank2/" + timetag.toString() + "/set2").size());
			stream = reader.chunks("tank2/" + timetag.toString() + "/set2").get(0).stream();
			for (int i = 0; i < 20; i++) assertEquals((long) i, stream.next());
			assertFalse(stream.hasNext());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		new File("temp/test.setfs").delete();
		new File("temp").delete();
	}
}