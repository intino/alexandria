package ebar;

import io.intino.pandora.plugin.PandoraApplication;
import io.intino.pandora.plugin.codegeneration.FullRenderer;
import org.junit.Test;
import tara.magritte.Graph;

import java.io.File;

public class EbarGenerationTest {

	private static final String EBAR = "ebar";

	@Test
	public void testEbarGeneration() throws Exception {
		File gen = new File("test-gen", EBAR);
		new FullRenderer(null, Graph.load("Ebar").wrap(PandoraApplication.class), gen, gen, EBAR).execute();
	}

}
