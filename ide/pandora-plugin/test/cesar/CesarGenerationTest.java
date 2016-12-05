package cesar;

import io.intino.pandora.plugin.PandoraApplication;
import io.intino.pandora.plugin.codegeneration.BoxConfigurationRenderer;
import io.intino.pandora.plugin.codegeneration.BoxRenderer;
import io.intino.pandora.plugin.codegeneration.accessor.rest.RESTAccessorRenderer;
import io.intino.pandora.plugin.rest.RESTService;
import org.junit.Ignore;
import org.junit.Test;
import tara.magritte.Graph;

import java.io.File;

public class CesarGenerationTest {

	private static final String CESAR = "cesar";
	private static final String CONSUL = "consul";


	@Test
	@Ignore
	public void testCesar() throws Exception {
		File gen = new File("test-gen", CESAR);
		Graph graph = Graph.load("Cesar").wrap(PandoraApplication.class);
//		new FullRenderer(null, graph, gen, gen, CESAR).execute();
		graph.find(RESTService.class).forEach(a ->
				new RESTAccessorRenderer(a, new File("test-gen/" + CESAR), CESAR).execute());
	}

	@Test
	public void testConsul() throws Exception {
		File gen = new File("test-gen", CONSUL);
		Graph graph = Graph.load("Consul").wrap(PandoraApplication.class);
		new BoxRenderer(graph, gen, CONSUL, null, false).execute();
		new BoxConfigurationRenderer(graph, gen, CONSUL, null, false).execute();
	}

	@Test
	public void swaggerAccessorsCreator() throws Exception {
	}
}
