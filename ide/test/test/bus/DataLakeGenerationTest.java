package bus;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class DataLakeGenerationTest {

	private static final String DATALAKE = "datalake";

	@Test
	public void testChannelsGeneration() throws Exception {
		File gen = new File("test-gen", DATALAKE);
		KonosGraph graph = new Graph().loadStashes("Datalake").as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, DATALAKE).execute();
	}

}
