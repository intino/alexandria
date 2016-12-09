package ui;

import io.intino.pandora.plugin.PandoraApplication;
import io.intino.pandora.plugin.codegeneration.BoxConfigurationRenderer;
import io.intino.pandora.plugin.codegeneration.BoxRenderer;
import io.intino.pandora.plugin.codegeneration.server.activity.SchemaAdaptersRenderer;
import io.intino.pandora.plugin.codegeneration.server.activity.display.DisplayRenderer;
import io.intino.pandora.plugin.codegeneration.server.activity.web.ActivityRenderer;
import io.intino.pandora.plugin.codegeneration.server.activity.web.ResourceRenderer;
import org.junit.Test;
import tara.magritte.Graph;

import java.io.File;

public class UIGenerationTest {

	private static final String UI = "ui";
	private static final String TEST = "test";


	@Test
	public void testUI() throws Exception {
		File gen = new File("test-gen", UI);
		final Graph graph = Graph.load("Ui").wrap(PandoraApplication.class);
		new DisplayRenderer(graph, gen, gen, UI, "System").execute();
		new ResourceRenderer(null, graph, gen, gen, UI, "System").execute();
		new ActivityRenderer(graph, gen, UI, "System").execute();
		new SchemaAdaptersRenderer(graph, gen, UI).execute();
		new BoxRenderer(graph, gen, UI, null, false).execute();
		new BoxConfigurationRenderer(graph, gen, UI, null, false).execute();
//		new ActivityAccessorCreator(null, graph).execute();
//		new FullRenderer(null, graph, gen, gen, UI).execute();
	}

	@Test
	public void testTest() throws Exception {
		File gen = new File("test-gen", TEST);
		final Graph graph = Graph.load("Ui").wrap(PandoraApplication.class);
		new DisplayRenderer(graph, gen, gen, TEST, "System").execute();
		new ResourceRenderer(null, graph, gen, gen, TEST, "System").execute();
		new ActivityRenderer(graph, gen, TEST, "System").execute();
		new SchemaAdaptersRenderer(graph, gen, TEST).execute();
		new BoxRenderer(graph, gen, TEST, null, false).execute();
		new BoxConfigurationRenderer(graph, gen, TEST, null, false).execute();
//		new ActivityAccessorCreator(null, graph).execute();
//		new FullRenderer(null, graph, gen, gen, TEST).execute();
	}


	@Test
	public void swaggerAccessorsCreator() throws Exception {
	}
}
