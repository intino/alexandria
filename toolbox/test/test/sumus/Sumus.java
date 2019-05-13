package sumus;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class Sumus {

	private static final String SUMUS = "sumus";

	@Test
	public void sumus() throws Exception {
		File gen = new File("test-gen", SUMUS);
		KonosGraph graph = new Graph().loadStashes("Sumus").as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, SUMUS).execute();

	}
}
