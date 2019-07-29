package processes;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class ProcessesGenerationTest {

	private static final String PROCESSES = "processes";

	@Test
	public void testProcesses() {
		File gen = new File("test-gen", PROCESSES);
		KonosGraph graph = new Graph().loadStashes("Processes").as(KonosGraph.class);
		Settings settings = new Settings();
		new FullRenderer(null, settings).execute();
	}
}