package cesar;

import org.junit.Before;
import org.junit.Test;
import tara.magritte.Graph;
import teseo.TeseoApplication;
import teseo.codegeneration.server.action.ActionRenderer;
import teseo.codegeneration.server.jmx.JMXServerRenderer;
import teseo.codegeneration.server.jmx.JMXTriggerRenderer;
import teseo.codegeneration.server.scheduling.ScheduledTriggerRenderer;
import teseo.codegeneration.server.scheduling.SchedulerRenderer;

import java.io.File;

public class CesarTest {

	private static final String CESAR = "cesar";

	@Before
	public void setUp() {
		Graph graph = Graph.load("Cesar").wrap(TeseoApplication.class);
		File gen = new File("test-gen", CESAR);
		new ScheduledTriggerRenderer(graph).execute(gen, CESAR);
		new SchedulerRenderer(graph).execute(gen, CESAR);
		new ActionRenderer(graph).execute(gen, CESAR);
		new JMXTriggerRenderer(graph).execute(gen, CESAR);
		new JMXServerRenderer(graph).execute(gen, CESAR);
	}

	@Test
	public void testModel() throws Exception {

	}
}
