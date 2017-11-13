package sumus;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class Activity {

	private static final String ACTIVITY = "activity";

	@Test
	public void sumus() throws Exception {
		File gen = new File("test-gen", ACTIVITY);
		KonosGraph graph = new Graph().loadStashes("Activity").as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, ACTIVITY).execute();

	}
}