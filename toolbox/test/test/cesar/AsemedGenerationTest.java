package cesar;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Test;
import utils.TestUtil;

import java.io.File;

public class AsemedGenerationTest {

	private static final String ASEMED = "asemed";

	@Test
	public void testAsemedGeneration() {
		File gen = new File("test-gen", ASEMED);
		KonosGraph graph = new Graph().loadStashes("Asemed").as(KonosGraph.class);
		new FullRenderer(graph, TestUtil.settings(gen, ASEMED), compiledFiles).execute();
	}

}
