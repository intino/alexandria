package cesar;

import io.intino.pandora.plugin.PandoraApplication;
import io.intino.pandora.plugin.codegeneration.FullRenderer;
import org.junit.Test;
import tara.magritte.Graph;

import java.io.File;
public class CesarGenerationTest {

	private static final String CESAR = "cesar";
	private static final String CONSUL = "consul";


	@Test
	public void testCesar() throws Exception {
		File gen = new File("test-gen", CESAR);
		new FullRenderer(null, Graph.load("Cesar").wrap(PandoraApplication.class), gen, gen, CESAR).execute();

	}

	@Test
	public void testConsul() throws Exception {
		File gen = new File("test-gen", CONSUL);
		new FullRenderer(null, Graph.load("Consul").wrap(PandoraApplication.class), gen, gen, CONSUL).execute();
	}


	@Test
	public void swaggerAccessorsCreator() throws Exception {
	}
}
