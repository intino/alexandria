package asemed;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

@Ignore
public class AsemedGenerationTest {
	private static final String ASEMED = "Asemed";
	private static final String ASEMED_LOCAL = "AsemedLocal";
	private static final String ASEMED_MIRROR = "AsemedMirror";

	@Test
	@Ignore
	public void testAsemedGeneration() {
		File gen = new File("test-gen", ASEMED.toLowerCase());
		KonosGraph graph = new Graph().loadStashes(ASEMED).as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, ASEMED.toLowerCase()).execute();
	}

	@Test
	@Ignore
	public void testAsemedMirrorGeneration() {
		File gen = new File("test-gen", ASEMED_MIRROR.toLowerCase());
		KonosGraph graph = new Graph().loadStashes(ASEMED_MIRROR).as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, ASEMED_MIRROR.toLowerCase()).execute();
	}


	@Test
	@Ignore
	public void testAsemedLocalGeneration() {
		File gen = new File("test-gen", ASEMED_LOCAL.toLowerCase());
		KonosGraph graph = new Graph().loadStashes(ASEMED_LOCAL).as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, ASEMED_LOCAL.toLowerCase()).execute();
	}
}
