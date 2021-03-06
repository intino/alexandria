package io.intino.alexandria.sealing;

import io.intino.alexandria.datalake.file.FileDatalake;
import org.junit.Test;

import java.io.File;
import java.time.Instant;

public class Seal_ {
	private static final File STAGE_FOLDER = new File("../temp/stage");
	private final File DATALAKE = new File("../temp/datalake");

	@Test
	public void should_create_a_session() {
		System.out.println(Instant.now());
		FileSessionSealer fileSessionManager = new FileSessionSealer(new FileDatalake(DATALAKE), STAGE_FOLDER);
		fileSessionManager.seal();
	}
}