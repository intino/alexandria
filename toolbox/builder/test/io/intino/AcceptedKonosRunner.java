package io.intino;

import io.intino.konos.KonoscRunner;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class AcceptedKonosRunner {
	private String home;

	@Before
	public void setUp() {
		home = new File("test-res").getAbsolutePath() + File.separator;
	}

	@Test
	public void timbradoM2() {
		KonoscRunner.main(new String[]{home + "confFiles/cfe/timbrado.txt"});
	}
}
