package ebar;

import io.intino.pandora.model.PandoraApplication;
import io.intino.pandora.plugin.codegeneration.FullRenderer;
import org.junit.Ignore;
import org.junit.Test;
import tara.magritte.Graph;

import java.io.File;

@Ignore
public class EbarGenerationTest {

	private static final String EBAR = "ebar";

	@Test
	@Ignore
	public void testEbarGeneration() throws Exception {
		File gen = new File("test-gen", EBAR);
		new FullRenderer(null, Graph.load("Ebar").wrap(PandoraApplication.class), gen, gen, EBAR).execute();
	}

}
