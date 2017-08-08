import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class CommandsTest {

	private static final String COMMANDS = "commands";

	@Test
	public void testModel() throws Exception {
		File gen = new File("test-gen", COMMANDS);
		KonosGraph graph = new Graph().loadStashes("Command").as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, COMMANDS).execute();
	}
}
