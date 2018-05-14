package amidas;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.codegeneration.accessor.rest.RESTAccessorRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class AmidasGenerationTest {

	private static final String AMIDAS = "amidas";


	@Test
	public void testAmidas() throws Exception {
		File gen = new File("test-gen", AMIDAS);
		KonosGraph graph = new Graph().loadStashes("Amidas").as(KonosGraph.class);
//		new FullRenderer(null, graph, gen, gen, gen, AMIDAS).execute();
		graph.rESTServiceList().forEach(a ->
				new RESTAccessorRenderer(a, new File("test-gen/" + AMIDAS), AMIDAS).execute());
	}

	@Test
	public void swaggerAccessorsCreator() throws Exception {
	}
}
