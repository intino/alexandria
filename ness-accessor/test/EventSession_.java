import io.intino.alexandria.inl.Message;
import io.intino.alexandria.nessaccessor.stages.LocalStage;
import io.intino.ness.core.Timetag;
import io.intino.ness.core.sessions.EventSession;
import org.junit.Test;

import java.io.File;
import java.time.Instant;

import static org.junit.Assert.assertTrue;

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
		LocalStage stage = new LocalStage(new File("temp"));
		EventSession session = stage.createEventSession();
		Timetag timetag = new Timetag("201809");
		Instant instant = Instant.parse("2018-09-10T00:00:00Z");
		session.put("tank1", timetag, message(instant, 0));
		session.close();

		assertTrue(temp.listFiles().length == 1);
	}

	private Message message(Instant instant, int index) {
		return new Message("tank1").set("ts", instant.toString()).set("index", index);
	}
}
