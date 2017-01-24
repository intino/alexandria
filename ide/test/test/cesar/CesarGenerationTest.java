package cesar;

import io.intino.konos.model.KonosApplication;
import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.tara.magritte.Graph;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class CesarGenerationTest {

	private static final String CESAR = "cesar";
	private static final String CONSUL = "consul";

	@Test
	@Ignore
	public void testCesar() throws Exception {
		File gen = new File("test-gen", CESAR);
		Graph graph = Graph.use(KonosApplication.class, null).load("Cesar");
		new FullRenderer(null, graph, gen, gen, CESAR).execute();
//		graph.find(RESTService.class).forEach(a ->
//				new RESTAccessorRenderer(a, new File("test-gen/" + CESAR), CESAR).execute());
	}

	@Test
	public void testConsul() throws Exception {
		File gen = new File("test-gen", CONSUL);
		Graph graph = Graph.use(KonosApplication.class, null).load("Consul");
		new FullRenderer(null, graph, gen, gen, CONSUL).execute();
//		new BoxConfigurationRenderer(graph, gen, CONSUL, null, false).execute();
//		graph.find(JMXService.class).forEach(a -> new JMXAccessorRenderer(a, gen, CONSUL).execute());
	}

	@Test
	public void swaggerAccessorsCreator() throws Exception {
	}
}
