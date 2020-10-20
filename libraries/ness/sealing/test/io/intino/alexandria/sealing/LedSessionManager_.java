package io.intino.alexandria.sealing;

import io.intino.alexandria.Scale;
import io.intino.alexandria.Timetag;
import io.intino.alexandria.datalake.file.FileDatalake;
import io.intino.alexandria.ingestion.LedSession;
import io.intino.alexandria.ingestion.SessionHandler;
import io.intino.alexandria.led.Led;
import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.LedReader;
import io.intino.alexandria.led.Transaction;
import io.intino.alexandria.led.allocators.SchemaAllocator;
import io.intino.alexandria.led.allocators.stack.StackAllocators;
import io.intino.alexandria.led.leds.ListLed;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
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
		List<TestSchema> stored = unsortedList();
		stored.sort(Comparator.comparingLong(Transaction::id));
		Led<TestSchema> testLed = new ListLed<>(stored);
		session.put("tank1", timetag, testLed);
		handler.pushTo(STAGE_FOLDER);
		new FileSessionSealer(new FileDatalake(DATALAKE), STAGE_FOLDER).seal();
		LedStream<TestSchema> stream = new LedReader(new File("temp/datalake/transactions/tank1/" + timetag.value() + ".led")).read(TestSchema::new);
		Iterator<TestSchema> iterator = testLed.iterator();
		assertThat(stream.next().id()).isEqualTo(iterator.next().id());
		assertThat(stream.next().e()).isEqualTo(iterator.next().e());
		assertThat(stream.next().d()).isEqualTo(iterator.next().d());
	}

	private List<TestSchema> unsortedList() {
		SchemaAllocator<TestSchema> allocator = StackAllocators.newManaged(TestSchema.SIZE, 1000, TestSchema::new);
		List<TestSchema> result = new ArrayList<>();
		for (int i = 10; i <= 1000; i += 5) {
			int id = (int) Math.cos(i / 20 * Math.PI) * i;
			result.add(allocator.calloc().id(id).b(i - 500).f(i * 100.0 / 20.0));
		}
		return result;
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