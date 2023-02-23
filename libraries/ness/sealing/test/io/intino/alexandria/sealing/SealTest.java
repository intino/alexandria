package io.intino.alexandria.sealing;

import io.intino.alexandria.datalake.file.FileDatalake;
import org.junit.Test;

import java.io.File;
import java.time.Instant;

public class SealTest {
	private final File stageDir = new File("temp/stage");
	private final File datalakeDir = new File("temp/datalake");
	private final File treatedDir = new File("temp/treated");


	@Test
	public void should_create_a_session() {
		System.out.println(Instant.now());
		FileSessionSealer fileSessionManager = new FileSessionSealer(new FileDatalake(datalakeDir), stageDir, treatedDir);
		fileSessionManager.seal();
	}
}