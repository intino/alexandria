package io.intino.alexandria.sealing;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.file.FileDatalake;
import io.intino.alexandria.ingestion.LedSession;
import io.intino.alexandria.ingestion.SessionHandler;
import io.intino.alexandria.led.LedReader;
import io.intino.alexandria.led.PrimaryLed;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class LedSessionManager_ {
	private static final File LOCAL_STAGE = new File("temp/localstage");
	private static final File STAGE_FOLDER = new File("temp/stage");
	private static final File DATALAKE = new File("temp/datalake");

	@Test
	public void should_create_and_seal_led_session() throws IOException {
		SessionHandler handler = new SessionHandler(LOCAL_STAGE);
		LocalDateTime dateTime = LocalDateTime.of(2019, 2, 28, 16, 15);
		Timetag timetag = new Timetag(dateTime, Scale.Day);
		LedSession session = handler.createLedSession();
		List<TestItem> stored = TestItem.unsortedList();
		PrimaryLed<TestItem> testLed = PrimaryLed.load(stored.stream());
		session.put("tank1", timetag, testLed);
		handler.pushTo(STAGE_FOLDER);
		new FileSessionSealer(new FileDatalake(DATALAKE), STAGE_FOLDER).seal();
		PrimaryLed<TestItem> led = new LedReader(new File("temp/datalake/ledger/tank1/" + timetag.value() + ".led")).read(TestItem.class);
		assertThat(led.size()).isEqualTo(stored.size());
		assertThat(led.iterator().next().id()).isEqualTo(stored.iterator().next().id());
		assertThat(led.iterator().next().i()).isEqualTo(stored.iterator().next().i());
		assertThat(led.iterator().next().d()).isEqualTo(stored.iterator().next().d());

	}

	@After
	public void tearDown() {
		deleteDirectory(new File("temp"));
	}

	private void deleteDirectory(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		directoryToBeDeleted.delete();
	}
}