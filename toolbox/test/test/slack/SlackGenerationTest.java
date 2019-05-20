package slack;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.codegeneration.cache.ElementCache;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class SlackGenerationTest {

	private static final String SLACK = "slack";

	@Test
	public void testSlack() throws Exception {
		File gen = new File("test-gen", SLACK);
		KonosGraph graph = new Graph().loadStashes("Slack").as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen,SLACK, new ElementCache()).execute();
	}

}
