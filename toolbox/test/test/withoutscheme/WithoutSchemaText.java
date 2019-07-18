package withoutscheme;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import utils.TestUtil;

import java.io.File;

public class WithoutSchemaText {

	public static void main(String[] args) {
		File gen = new File("test-gen", "withoutschema");
		KonosGraph graph = new Graph().loadStashes("WithoutSchema").as(KonosGraph.class);
		new FullRenderer(graph, TestUtil.settings(gen, "withoutschema")).execute();
	}
}
