package withoutscheme;

import io.intino.pandora.model.PandoraApplication;
import io.intino.pandora.plugin.codegeneration.FullRenderer;
import io.intino.tara.magritte.Graph;

import java.io.File;

public class WithoutSchemaText {

	public static void main(String[] args) {
		File genFolder = new File("test-gen", "withoutschema");
		new FullRenderer(null, Graph.use(PandoraApplication.class, null).load("WithoutSchema"), genFolder, genFolder, "withoutschema").execute();
	}
}
