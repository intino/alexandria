package sumus;

import io.intino.konos.model.Konos;
import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class Sumus {

	private static final String SUMUS = "sumus";

	@Test
	public void sumus() throws Exception {
		File gen = new File("test-gen", SUMUS);
		new FullRenderer(null, Graph.use(Konos.class, null).load("Sumus"), gen, gen, gen, gen, SUMUS).execute();

	}
}
