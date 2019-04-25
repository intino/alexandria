package ebar;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

@Ignore
public class EbarGenerationTest {

	private static final String EBAR = "ebar";

	@Test
	@Ignore
	public void testEbarGeneration() {
		File gen = new File("test-gen", EBAR);
		KonosGraph graph = new Graph().loadStashes("Ebar").as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, EBAR).execute();
	}

}
