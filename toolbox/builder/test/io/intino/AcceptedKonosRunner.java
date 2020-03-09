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
	public void timbrado() {
		KonoscRunner.main(new String[]{home + "confFiles/cfe/timbrado.txt"});
	}

	@Test
	public void cuentamaestra() {
		KonoscRunner.main(new String[]{home + "confFiles/cfe/cuentamaestra.txt"});
	}

	@Test
	public void cesarDh() {
		KonoscRunner.main(new String[]{home + "confFiles/cesar/cesar-dh.txt"});
	}

	@Test
	public void cesar() {
		KonoscRunner.main(new String[]{home + "confFiles/cesar/cesar.txt"});
	}

	@Test
	public void amidas() {
		KonoscRunner.main(new String[]{home + "confFiles/amidas/amidas-team.txt"});
	}
}
