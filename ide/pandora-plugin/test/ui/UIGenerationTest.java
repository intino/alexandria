package ui;

import io.intino.pandora.plugin.PandoraApplication;
import io.intino.pandora.plugin.codegeneration.BoxConfigurationRenderer;
import io.intino.pandora.plugin.codegeneration.BoxRenderer;
import io.intino.pandora.plugin.codegeneration.server.ui.SchemaAdaptersRenderer;
import io.intino.pandora.plugin.codegeneration.server.ui.display.DisplayRenderer;
import io.intino.pandora.plugin.codegeneration.server.ui.web.ActivityRenderer;
import io.intino.pandora.plugin.codegeneration.server.ui.web.ResourceRenderer;
import org.junit.Test;
import tara.magritte.Graph;

import java.io.File;

public class UIGenerationTest {

	private static final String UI = "ui";


	@Test
	public void testUI() throws Exception {
		File gen = new File("test-gen", UI);
		final Graph graph = Graph.load("Ui").wrap(PandoraApplication.class);
		new DisplayRenderer(graph, gen, gen, UI, "System").execute();
		new ResourceRenderer(null, graph, gen, gen, UI, "System").execute();
		new ActivityRenderer(graph, gen, UI, "System").execute();
		new SchemaAdaptersRenderer(graph, gen, UI).execute();
		new BoxRenderer(graph, gen, UI, null).execute();
		new BoxConfigurationRenderer(graph, gen, UI, null).execute();
//		new ActivityAccessorCreator(null, graph).execute();
//		new FullRenderer(null, graph, gen, gen, UI).execute();
	}

	@Test
	public void swaggerAccessorsCreator() throws Exception {
	}
}
