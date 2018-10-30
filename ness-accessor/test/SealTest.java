import io.intino.alexandria.ness.Scale;
import io.intino.alexandria.nessaccesor.NessAccessor;
import io.intino.alexandria.nessaccesor.NessAccessor.SetStore.Timetag;
import io.intino.alexandria.nessaccesor.SetSession;
import io.intino.alexandria.nessaccesor.fs.FSStage;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SealTest {

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
	public void create_session_and_seal_it() {
		try {
			Timetag timetag = new Timetag(Instant.parse("2018-09-01T00:00:00Z"), Scale.Month);
			NessAccessor accessor = new NessAccessor("file://temp", "", "", "");
			FSStage stage = new FSStage(new File("temp"));
			SetSession session = NessAccessor.createSetSession(stage);
			for (int i = 0; i < 20; i++) session.chunk("tank1", timetag, "set1").put(i);
			for (int i = 0; i < 20; i++) session.chunk("tank1", timetag, "set2").put(i);
			for (int i = 0; i < 20; i++) session.chunk("tank2", timetag, "set1").put(i);
			for (int i = 0; i < 20; i++) session.chunk("tank2", timetag, "set2").put(i);
			session.close();
			accessor.commit(stage);
			assertTrue(new File("temp/set/tank1/201809/set1.set").exists());
			assertTrue(new File("temp/set/tank1/201809/set2.set").exists());
			assertTrue(new File("temp/set/tank2/201809/set1.set").exists());
			assertTrue(new File("temp/set/tank2/201809/set2.set").exists());
			assertEquals(8 * 20, new File("temp/set/tank1/201809/set1.set").length());
			assertEquals(8 * 20, new File("temp/set/tank1/201809/set2.set").length());
			assertEquals(8 * 20, new File("temp/set/tank2/201809/set1.set").length());
			assertEquals(8 * 20, new File("temp/set/tank2/201809/set2.set").length());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	@After
	public void tearDown() {
		deleteDirectory(new File("temp"));
	}
}