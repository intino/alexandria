package cesar;

import org.junit.Test;
import tara.magritte.Graph;
import teseo.TeseoApplication;
import teseo.codegeneration.action.ActionRenderer;
import teseo.codegeneration.server.jmx.JMXOperationsServiceRenderer;
import teseo.codegeneration.server.jmx.JMXServerRenderer;
import teseo.codegeneration.server.scheduling.ScheduledTriggerRenderer;
import teseo.codegeneration.server.scheduling.SchedulerRenderer;
import teseo.codegeneration.server.web.JavaServerRenderer;

import java.io.File;

public class ConsulTest {

	private static final String CESAR = "cesar";


	@Test
	public void testModel() throws Exception {
		Graph graph = Graph.load("Consul").wrap(TeseoApplication.class);
		File gen = new File("test-gen", CESAR);
		new JavaServerRenderer(graph).execute(gen, gen, CESAR);
		new ScheduledTriggerRenderer(graph).execute(gen, CESAR);
		new SchedulerRenderer(graph).execute(gen, CESAR);
		new ActionRenderer(graph).execute(gen, CESAR);
		new JMXOperationsServiceRenderer(graph).execute(gen, CESAR);
		new JMXServerRenderer(graph).execute(gen, CESAR);
	}
}
