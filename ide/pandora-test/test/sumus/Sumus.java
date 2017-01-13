package sumus;

import io.intino.pandora.model.PandoraApplication;
import io.intino.pandora.plugin.codegeneration.FullRenderer;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class Sumus {

	private static final String SUMUS = "sumus";

	@Test
	public void sumus() throws Exception {
		File gen = new File("test-gen", SUMUS);
		new FullRenderer(null, Graph.use(PandoraApplication.class, null).load("Sumus"), gen, gen, SUMUS).execute();

	}
}
