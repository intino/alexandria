package ui;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.codegeneration.accessor.ui.ActivityAccessorRenderer;
import io.intino.konos.model.graph.Activity;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class UIGenerationTest {

	private static final String UI = "ui";
	private static final String DIALOG = "dialog";
	private static final String ACTIVITY = "activity";


	@Test
	public void testUI() throws Exception {
		File gen = new File("test-gen", UI);
		KonosGraph graph = new Graph().loadStashes("ui").as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, UI).execute();
	}

	@Test
	public void testUI2() throws Exception {
		File gen = new File("test-gen", UI + "2");
		KonosGraph graph = new Graph().loadStashes("ui").as(KonosGraph.class);
		for (Activity activity : graph.activityList()) {
			new ActivityAccessorRenderer(gen, activity).execute();
		}
//		new FullRenderer(null, graph, gen, gen, gen, UI).execute();
	}

	@Test
	public void testTest() throws Exception {
		File gen = new File("test-gen", DIALOG);
		KonosGraph graph = new Graph().loadStashes(DIALOG).as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, DIALOG).execute();
//		new ActivityAccessorCreator(null, graph).execute();
	}

	@Test
	public void swaggerAccessorsCreator() throws Exception {
	}



	@Test
	public void sumus() throws Exception {
		File gen = new File("test-gen", ACTIVITY);
		KonosGraph graph = new Graph().loadStashes("Activity").as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, ACTIVITY).execute();

	}
}
