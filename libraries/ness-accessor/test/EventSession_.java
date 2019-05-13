import io.intino.alexandria.Timetag;
import io.intino.alexandria.inl.Message;
import io.intino.alexandria.zim.ZimReader;
import io.intino.ness.core.fs.FSDatalake;
import io.intino.ness.core.fs.FSStage;
import io.intino.ness.core.sessions.EventSession;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;

import static org.junit.Assert.assertTrue;


@Ignore
public class EventSession_ {
	private static boolean deleteDirectory(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		return directoryToBeDeleted.delete();
	}

	@Test
	public void should_create_event_blobs() {
		File temp = new File("temp");
		deleteDirectory(temp);
		FSStage stage = new FSStage(new File("temp"));
		EventSession session = stage.createEventSession();
		Timetag timetag = new Timetag("201809");
		Instant instant = Instant.parse("2018-09-10T00:00:00Z");
		session.put("tank1", timetag, message(instant, 0));
		session.close();

		assertTrue(temp.listFiles().length == 1);
	}

	@Test
	public void stage() throws IOException {
		File file = new File("stage/");
		Files.list(file.toPath()).filter(p -> p.toFile().getName().endsWith("event.blob")).forEach(p -> read(p.toFile()));
	}

	@Test
	@Ignore
	public void seal() {
		new FSDatalake(new File("stage/")).seal();
	}

	private void read(File file) {
		ZimReader zimReader = new ZimReader(file);
		System.out.println(file.getName());
		while (zimReader.hasNext()) zimReader.next();
	}


	private Message message(Instant instant, int index) {
		return new Message("tank1").set("ts", instant.toString()).set("index", index);
	}
}
