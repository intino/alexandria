package asemed;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class AsemedGenerationTest {

	private static final String ASEMED = "asemed";

	@Test
	public void testDatalakeGeneration() throws Exception {
		File gen = new File("test-gen", ASEMED);
		KonosGraph graph = new Graph().loadStashes("Datalake").as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, ASEMED).execute();
	}

}
