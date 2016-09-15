package cesar;

import org.junit.Test;
import tara.magritte.Graph;
import teseo.TeseoApplication;
import teseo.codegeneration.accessor.rest.RESTAccessorRenderer;
import teseo.codegeneration.server.jmx.JMXOperationsServiceRenderer;
import teseo.codegeneration.server.jmx.JMXServerRenderer;
import teseo.codegeneration.server.rest.RESTServiceRenderer;
import teseo.codegeneration.server.scheduling.ScheduledTriggerRenderer;
import teseo.codegeneration.server.scheduling.SchedulerRenderer;
import teseo.rest.RESTService;

import java.io.File;

public class CesarTest {

	private static final String CESAR = "cesar";


	@Test
	public void testCesar() throws Exception {
		Graph graph = Graph.load("Cesar").wrap(TeseoApplication.class);
		File gen = new File("test-gen", CESAR);
		new RESTServiceRenderer(graph).execute(gen, gen, CESAR);
		new ScheduledTriggerRenderer(graph).execute(gen, gen, CESAR);
		new SchedulerRenderer(graph).execute(gen, CESAR);
		new JMXOperationsServiceRenderer(graph).execute(gen, gen, CESAR);
		new JMXServerRenderer(graph).execute(gen, CESAR);
		graph.find(RESTService.class).forEach(a -> new RESTAccessorRenderer(a).execute(new File("test-gen", CESAR), CESAR));
	}

	@Test
	public void testConsul() throws Exception {
		Graph graph = Graph.load("Consul").wrap(TeseoApplication.class);
		File gen = new File("test-gen", CESAR);
		new RESTServiceRenderer(graph).execute(gen, gen, CESAR);
		new ScheduledTriggerRenderer(graph).execute(gen, gen, CESAR);
		new SchedulerRenderer(graph).execute(gen, CESAR);
		new JMXOperationsServiceRenderer(graph).execute(gen, gen, CESAR);
		new JMXServerRenderer(graph).execute(gen, CESAR);
	}


}
