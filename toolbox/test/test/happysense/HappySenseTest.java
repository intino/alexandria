package happysense;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Test;
import utils.TestUtil;

import java.io.File;

public class HappySenseTest {

	private static final String HAPPYSENSE = "happysense";

	@Test
	public void testCreation() {
		KonosGraph graph = new Graph().loadStashes("Happysense").as(KonosGraph.class);
		File gen = new File("test-gen", HAPPYSENSE);
		new FullRenderer(graph, TestUtil.settings(gen, HAPPYSENSE)).execute();
//		graph.rESTServiceList().forEach(a -> new RESTAccessorRenderer(a, new File("test-gen/happysense"), HAPPYSENSE).execute());
	}
}