package amidas;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Test;
import utils.TestUtil;

import java.io.File;

public class AmidasGenerationTest {

	private static final String AMIDAS = "amidas";
	private static final String PROCESSES = "processes";

	@Test
	public void testAmidas() {
		File gen = new File("test-gen", AMIDAS);
		KonosGraph graph = new Graph().loadStashes("Amidas").as(KonosGraph.class);
		new FullRenderer(graph, TestUtil.settings(gen, AMIDAS)).execute();
//		graph.rESTServiceList().forEach(a ->
//				new RESTAccessorRenderer(a, new File("test-gen/" + AMIDAS), AMIDAS).execute());
	}

	@Test
	public void testProcesses() {
		File gen = new File("test-gen", PROCESSES);
		KonosGraph graph = new Graph().loadStashes("Processes").as(KonosGraph.class);
		new FullRenderer(graph, TestUtil.settings(gen, PROCESSES)).execute();
	}

}