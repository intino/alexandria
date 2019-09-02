package processes;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.accessor.messaging.MessagingAccessorRenderer;
import io.intino.konos.model.graph.KonosGraph;
import org.junit.Test;

import java.io.File;

import static utils.TestUtil.graph;
import static utils.TestUtil.settings;

public class ProcessesGenerationTest {

	private static final String PROCESSES = "processes";
	private static final String ACCESSOR_PROCESSES = "acessor.processes";

	@Test
	public void testProcesses() {
		KonosGraph graph = graph(PROCESSES);
		Settings settings = settings(new File("test-gen", PROCESSES), PROCESSES);
//		new FullRenderer(graph, settings).execute();
		graph.messagingServiceList().forEach(a ->
				new MessagingAccessorRenderer(settings, a, graph.workflow(), new File("test-gen/accessor/" + ACCESSOR_PROCESSES)).execute());
	}
}