package ui;

import io.intino.konos.builder.codegeneration.AbstractBoxRenderer;
import io.intino.konos.builder.codegeneration.BoxConfigurationRenderer;
import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.codegeneration.accessor.ui.ActivityAccessorRenderer;
import io.intino.konos.builder.codegeneration.server.activity.SchemaAdaptersRenderer;
import io.intino.konos.builder.codegeneration.server.activity.dialog.DialogRenderer;
import io.intino.konos.builder.codegeneration.server.activity.display.DisplayRenderer;
import io.intino.konos.builder.codegeneration.server.activity.web.ActivityRenderer;
import io.intino.konos.builder.codegeneration.server.activity.web.ResourceRenderer;
import io.intino.konos.model.Activity;
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
		final Graph graph = Graph.use(Konos.class, null).load("ui");
		new FullRenderer(null, graph, gen, gen, gen, UI).execute();
	}

	@Test
	public void testUI2() throws Exception {
		File gen = new File("test-gen", UI + "2");
		final Graph graph = Graph.use(Konos.class, null).load("ui2");
		for (Activity activity : graph.wrapper(Konos.class).activityList()) {
			new ActivityAccessorRenderer(gen, activity).execute();
		}
//		new FullRenderer(null, graph, gen, gen, gen, UI).execute();
	}

	@Test
	public void testTest() throws Exception {
		File gen = new File("test-gen", TEST);
		final Graph graph = Graph.use(Konos.class, null).load("test");

		new DisplayRenderer(null, graph, gen, gen, TEST, "System").execute();
		new DialogRenderer(null, graph, gen, gen, TEST, "System").execute();
		new ResourceRenderer(null, graph, gen, gen, TEST, "System").execute();
		new ActivityRenderer(graph, gen, gen, TEST, "System").execute();
		new SchemaAdaptersRenderer(graph, gen, TEST).execute();
		new AbstractBoxRenderer(graph, gen, TEST, null, null, false).execute();
		new BoxConfigurationRenderer(graph, gen, TEST, null, null, false).execute();
//		new ActivityAccessorCreator(null, graph).execute();
//		new FullRenderer(null, graph, gen, gen, TEST).execute();
	}

	@Test
	public void swaggerAccessorsCreator() throws Exception {
	}
}
