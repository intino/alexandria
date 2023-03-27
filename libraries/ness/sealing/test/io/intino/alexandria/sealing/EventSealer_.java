package io.intino.alexandria.sealing;

import io.intino.alexandria.datalake.file.FileDatalake;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.EventStream;
import io.intino.alexandria.event.EventWriter;
import io.intino.alexandria.event.resource.ResourceEvent;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Iterator;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventSealer_ {

	@Test
	public void sealResources() throws IOException {
		File destinationFile = new File("temp/datalake/resources/Log/ss/20230327.zip");
		if(destinationFile.exists()) assertSorted(destinationFile);

		createResourceFilesToSort(10);

		EventSessionSealer sealer = new EventSessionSealer(new FileDatalake(new File("temp/datalake")),
				new File("temp/stage"),
				new File("temp/stage/temp"),
				new File("temp/stage"));

		System.out.println("Sealing...");
		sealer.seal();

		assertSorted(destinationFile);
		System.out.println("Test ended. All events sealed and sorted in destination");
	}

	private void createResourceFilesToSort(int count) throws IOException {
		System.out.println("Creating files to sort...");
		File stage = new File("temp/stage/resources");
		stage.mkdirs();
		Random random = new Random();
		String[] files = {"temp/A.txt", "temp/B.txt", "temp/C.txt", "temp/D.docx", "temp/E.pdf", "temp/F.jpg"};
		for(int i = 0;i < count;i++) {
			int numEvents = random.nextInt(20) + 1;
			int[] offsets = random.ints(numEvents, 1, 365 * 3600).toArray();
			// tank + SEPARATOR + source + SEPARATOR + timetag + SEPARATOR + format
			File file = new File(stage, "Log~ss~20230327~Resource#test-" + i + ".zip.session");
			if(file.exists()) continue;
			try(EventWriter<ResourceEvent> writer = EventWriter.of(Event.Format.Resource, new BufferedOutputStream(new FileOutputStream(file)))) {
				for(int j = 0;j < numEvents;j++) {
					ResourceEvent event = new ResourceEvent("Log", "ss", new File(files[random.nextInt(files.length)]));
					int offset = offsets[j];
					event.ts(Instant.now().plusSeconds(random.nextBoolean() ? offset : -offset));
					writer.write(event);
				}
			}
		}
	}

	private void assertSorted(File file) throws IOException {
		Iterator<Event> iterator = EventStream.of(file).iterator();
		Instant ts = null;
		while(iterator.hasNext()) {
			Event event = iterator.next();
			if(ts != null) assertFalse(ts.isAfter(event.ts()));
			ts = event.ts();
		}
	}
}
