package ui;

import io.intino.konos.builder.codegeneration.BoxConfigurationRenderer;
import io.intino.konos.builder.codegeneration.BoxRenderer;
import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.codegeneration.server.activity.SchemaAdaptersRenderer;
import io.intino.konos.builder.codegeneration.server.activity.display.DisplayRenderer;
import io.intino.konos.builder.codegeneration.server.activity.web.ActivityRenderer;
import io.intino.konos.builder.codegeneration.server.activity.web.ResourceRenderer;
import io.intino.konos.model.Konos;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class UIGenerationTest {

	private static final String UI = "ui";
	private static final String TEST = "test";


	@Test
	public void testUI() throws Exception {
		File gen = new File("test-gen", UI);
		final Graph graph = Graph.use(Konos.class, null).load("Ui");
//		new DisplayRenderer(graph, gen, gen, UI, "System").execute();
//		new ResourceRenderer(null, graph, gen, gen, UI, "System").execute();
//		new ActivityRenderer(graph, gen, UI, "System").execute();
//		new SchemaAdaptersRenderer(graph, gen, UI).execute();
//		new BoxRenderer(graph, gen, UI, null, false).execute();
//		new BoxConfigurationRenderer(graph, gen, UI, null, false).execute();
//		new ActivityAccessorCreator(null, graph).execute();
		new FullRenderer(null, graph, gen, gen, UI).execute();
	}

	@Test
	public void testTest() throws Exception {
		File gen = new File("test-gen", TEST);
		final Graph graph = Graph.use(Konos.class, null).load("Ui");
		new DisplayRenderer(null, graph, gen, gen, TEST, "System").execute();
		new ResourceRenderer(null, graph, gen, gen, TEST, "System").execute();
		new ActivityRenderer(graph, gen, TEST, "System").execute();
		new SchemaAdaptersRenderer(graph, gen, TEST).execute();
		new BoxRenderer(graph, gen, TEST, null, null, false).execute();
		new BoxConfigurationRenderer(graph, gen, TEST, null, null).execute();
//		new ActivityAccessorCreator(null, graph).execute();
//		new FullRenderer(null, graph, gen, gen, TEST).execute();
	}

	@Test
	public void swaggerAccessorsCreator() throws Exception {
	}
}
