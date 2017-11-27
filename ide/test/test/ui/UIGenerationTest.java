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
	private static final String testKonos = "testKonos";


	@Test
	public void testActivityAndDisplays() throws Exception {
		File gen = new File("test-gen", UI);
		KonosGraph graph = new Graph().loadStashes("ui").as(KonosGraph.class);
		for (Activity activity : graph.activityList()) new ActivityAccessorRenderer(gen, activity).execute();
		new FullRenderer(null, graph, gen, gen, gen, UI).execute();
	}

	@Test
	public void testDialog() throws Exception {
		File gen = new File("test-gen", DIALOG);
		KonosGraph graph = new Graph().loadStashes(DIALOG).as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, DIALOG).execute();
//		new ActivityAccessorCreator(null, graph).execute();
	}

	@Test
	public void testKonos() throws Exception {
		File gen = new File("test-gen", testKonos);
		cottons.utils.Files.removeDir(gen);
		gen.mkdirs();
		KonosGraph graph = new Graph().loadStashes(testKonos).as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, testKonos).execute();
	}
}
