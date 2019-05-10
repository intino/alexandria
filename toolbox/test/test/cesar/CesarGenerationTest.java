package cesar;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.codegeneration.accessor.rest.RESTAccessorRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class CesarGenerationTest {

	private static final String CESAR = "cesar.box";
	private static final String CONSUL = "consul";

	@Test
	@Ignore
	public void testCesar() {
		File gen = new File("test-gen", CESAR);
		KonosGraph graph = new Graph().loadStashes("Cesar").as(KonosGraph.class);
//		new FullRenderer(null, graph, gen, gen, gen, CESAR).execute();
		graph.rESTServiceList().forEach(a ->
				new RESTAccessorRenderer(a, new File("test-gen/accessor/" + CESAR), CESAR).execute());
	}

	@Test
	public void testConsul() {
		File gen = new File("test-gen", CONSUL);
		KonosGraph graph = new Graph().loadStashes("Consul").as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, CONSUL).execute();
	}
}
