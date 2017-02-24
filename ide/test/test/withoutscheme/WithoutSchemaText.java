package withoutscheme;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.model.Konos;
import io.intino.tara.magritte.Graph;

import java.io.File;

public class WithoutSchemaText {

	public static void main(String[] args) {
		File gen = new File("test-gen", "withoutschema");
		new FullRenderer(null, Graph.use(Konos.class, null).load("WithoutSchema"), gen, gen, gen, "withoutschema").execute();
	}
}
