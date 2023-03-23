package io.intino.alexandria.datalake.tests;

import io.intino.alexandria.Resource;
import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.datalake.file.FileDatalake;
import io.intino.alexandria.event.EventWriter;
import io.intino.alexandria.event.resource.ResourceEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ResourceStore_ {

	public static void main(String[] args) throws IOException {
		writeResources();

		Datalake datalake = new FileDatalake(new File("temp/datalake"));

		Datalake.ResourceStore resources = datalake.resourceStore();

		// List all the events in this store. At this point, NO resources are loaded into memory and no files are opened
		List<ResourceEvent> events = resources.content().collect(Collectors.toList());
		events.forEach(System.out::println);

		// Read resource
		ResourceEvent event = events.get(1);
		try(InputStream inputStream = event.resource().open()) {
			// Open the resource on demand
			System.out.println("\n");
		}

		// Find directly by REI (Resource Event Identifier)
		//Optional<ResourceEvent> result = resources.find("<type>/<ss>/<ts>/<resource-name>");
		Optional<ResourceEvent> result = resources.find(event.getREI());

		if(result.isEmpty()) return;

		System.out.println("findByREI = " + result.get().resource().safeReader().ofEmpty().readAsString());
	}

	private static void writeResources() throws IOException {
		LocalDateTime ts = LocalDateTime.of(2023, 1, 1, 12, 0);
		List<ResourceEvent> events = List.of(
				new ResourceEvent("Log", "ss", new File("temp/C.txt")).ts(ts.plusMinutes(50).toInstant(ZoneOffset.UTC)),
				new ResourceEvent("Log", "ss", new File("temp/B.txt")).ts(ts.plusMinutes(30).toInstant(ZoneOffset.UTC)),
				new ResourceEvent("Log", "ss", new File("temp/A.txt")).ts(ts.plusMinutes(10).toInstant(ZoneOffset.UTC)),
				new ResourceEvent("Log", "ss", new File("temp/A.txt")).ts(ts.plusMinutes(0).toInstant(ZoneOffset.UTC))
		);

		File session = new File("temp/resources_20230101_session.zip");
		EventWriter.write(session, events.stream());

		Files.move(session.toPath(), new File("temp/datalake/resources/Log/ss/20230101.zip").toPath(), REPLACE_EXISTING);
	}
}
