import io.intino.alexandria.Timetag;
import io.intino.alexandria.nessaccessor.stages.LocalStage;
import io.intino.ness.core.Datalake;
import io.intino.ness.core.sessions.SetSession;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class SetSession_ {

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
	public void should_create_set_blobs() {
		File temp = new File("temp");
		deleteDirectory(temp);
		LocalStage stage = new LocalStage(temp);
		SetSession session = stage.createSetSession();
		Timetag timetag = new Timetag("201809");
		session.define("tank1", timetag, "set1", new Datalake.SetStore.Variable("var", 10));

		for (int i = 0; i < 20; i++) session.put("tank1", timetag, "set1", i);
		for (int i = 0; i < 20; i++) session.put("tank1", timetag, "set2", i);
		for (int i = 0; i < 20; i++) session.put("tank2", timetag, "set1", i);
		for (int i = 0; i < 20; i++) session.put("tank2", timetag, "set2", i);
		session.close();

		assertTrue(temp.listFiles().length == 2);
	}

}
