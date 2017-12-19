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
	private static final String konos = "testkonos";
	private static final String ebar = "testebar";
	private static final String octana = "octana";
	private static final String cesarPackage = "io.intino.cesar.box";


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
	}

	@Test
	public void testKonos() throws Exception {
		execute(new File("test-gen", konos), konos, konos);
	}


	@Test
	public void testEbar() throws Exception {
		execute(new File("test-gen", ebar), ebar, ebar);
	}


	@Test
	public void testOctana() throws Exception {
		execute(new File("test-gen", octana), octana, octana);
	}


	@Test
	public void testCesar() throws Exception {
		execute(new File("test-gen", cesarPackage.replace(".", File.separator)), "testCesar", cesarPackage);
	}

	private void execute(File gen, String stash, String workingPackage) {
		cottons.utils.Files.removeDir(gen);
		gen.mkdirs();
		KonosGraph graph = new Graph().loadStashes(stash).as(KonosGraph.class);
//		new FullRenderer(null, graph, gen, gen, gen, workingPackage.toLowerCase()).execute();
		for (Activity activity : graph.activityList()) new ActivityAccessorRenderer(gen, activity).execute();
	}

}
