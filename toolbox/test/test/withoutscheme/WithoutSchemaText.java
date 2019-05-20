package withoutscheme;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.codegeneration.cache.ElementCache;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;

import java.io.File;

public class WithoutSchemaText {

	public static void main(String[] args) {
		File gen = new File("test-gen", "withoutschema");
		KonosGraph graph = new Graph().loadStashes("WithoutSchema").as(KonosGraph.class);
		new FullRenderer(null, graph, gen, gen, gen, "withoutschema", new ElementCache()).execute();
	}
}
