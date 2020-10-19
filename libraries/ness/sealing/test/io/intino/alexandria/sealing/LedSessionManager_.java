package io.intino.alexandria.sealing;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.file.FileDatalake;
import io.intino.alexandria.ingestion.LedSession;
import io.intino.alexandria.ingestion.SessionHandler;
import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.LedReader;
import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.leds.ListLed;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class LedSessionManager_ {
	private static final File LOCAL_STAGE = new File("temp/localstage");
	private static final File STAGE_FOLDER = new File("temp/stage");
	private static final File DATALAKE = new File("temp/datalake");

	@Test
	public void should_create_and_seal_led_session() {
		SessionHandler handler = new SessionHandler(LOCAL_STAGE);
		LocalDateTime dateTime = LocalDateTime.of(2019, 2, 28, 16, 15);
		Timetag timetag = new Timetag(dateTime, Scale.Day);
		LedSession session = handler.createLedSession();
		List<TestSchema> stored = TestSchema.unsortedList();
		stored.sort(Comparator.comparingLong(Schema::id));
		Led<TestSchema> testLed = new ListLed<>(stored);
		session.put("tank1", timetag, testLed);
		handler.pushTo(STAGE_FOLDER);
		new FileSessionSealer(new FileDatalake(DATALAKE), STAGE_FOLDER).seal();
		LedStream<TestSchema> led = new LedReader(new File("temp/datalake/ledger/tank1/" + timetag.value() + ".led")).read(TestSchema::new);
		assertThat(led.iterator().next().id()).isEqualTo(testLed.iterator().next().id());
		assertThat(led.iterator().next().e()).isEqualTo(testLed.iterator().next().e());
		assertThat(led.iterator().next().d()).isEqualTo(testLed.iterator().next().d());
	}

	@After
	public void tearDown() {
		deleteDirectory(new File("temp"));
	}

	private void deleteDirectory(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) for (File file : allContents) deleteDirectory(file);
		directoryToBeDeleted.delete();
	}
}