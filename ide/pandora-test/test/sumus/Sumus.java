package sumus;

import io.intino.pandora.model.PandoraApplication;
import io.intino.pandora.plugin.codegeneration.FullRenderer;
import org.junit.Test;
import tara.magritte.Graph;

import java.io.File;

public class Sumus {

	private static final String SUMUS = "sumus";

	@Test
	public void sumus() throws Exception {
		File gen = new File("test-gen", SUMUS);
		new FullRenderer(null, Graph.load("Sumus").wrap(PandoraApplication.class), gen, gen, SUMUS).execute();

	}
}
