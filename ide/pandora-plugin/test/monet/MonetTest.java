package monet;

import io.intino.pandora.model.PandoraApplication;
import io.intino.pandora.plugin.codegeneration.FullRenderer;
import org.junit.Test;
import tara.magritte.Graph;

import java.io.File;

public class MonetTest {

	private static final String MONET = "monet";

	@Test
	public void test() {
		final File gen = new File("test-gen", MONET);
		new FullRenderer(null, Graph.load("Monet").wrap(PandoraApplication.class), gen, gen, MONET).execute();
	}
}
