package cesar;

import org.junit.Before;
import org.junit.Test;
import tara.magritte.Graph;
import teseo.TeseoApplication;
import teseo.codegeneration.server.action.ActionRenderer;
import teseo.codegeneration.server.scheduling.ScheduledTriggerRenderer;
import teseo.codegeneration.server.scheduling.SchedulerRenderer;

import java.io.File;

public class CesarTest {

	@Before
	public void setUp() {
		Graph graph = Graph.load("Cesar").wrap(TeseoApplication.class);
		File genFolder = new File("test-gen", "cesar");
		new ScheduledTriggerRenderer(graph).execute(genFolder, "cesar");
		new SchedulerRenderer(graph).execute(genFolder, "cesar");
		new ActionRenderer(graph).execute(genFolder, "cesar");
	}

	@Test
	public void testModel() throws Exception {

	}
}
