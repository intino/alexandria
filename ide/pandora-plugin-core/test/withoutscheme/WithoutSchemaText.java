package withoutscheme;

import io.intino.pandora.plugin.PandoraApplication;
import io.intino.pandora.plugin.codegeneration.FullRenderer;
import tara.magritte.Graph;

import java.io.File;

public class WithoutSchemaText {

	public static void main(String[] args) {
		File genFolder = new File("test-gen", "withoutschema");
		new FullRenderer(Graph.load("WithoutSchema").wrap(PandoraApplication.class), genFolder, genFolder, "withoutschema").execute();
	}
}
