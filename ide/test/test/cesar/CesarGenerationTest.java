package cesar;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.codegeneration.accessor.jms.JMSAccessorRenderer;
import io.intino.konos.model.Konos;
import io.intino.konos.model.jms.JMSService;
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
		Graph graph = Graph.use(Konos.class, null).load("Cesar");
		new FullRenderer(null, graph, gen, gen, gen, gen, CESAR).execute();
//		graph.find(RESTService.class).forEach(a ->
//				new RESTAccessorRenderer(a, new File("test-gen/" + CESAR), CESAR).execute());
	}

	@Test
	public void testConsul() throws Exception {
		File gen = new File("test-gen", CONSUL);
		Graph graph = Graph.use(Konos.class, null).load("Consul");
//		new FullRenderer(null, graph, gen, gen, gen, gen, CONSUL).execute();
		graph.find(JMSService.class).forEach(a -> new JMSAccessorRenderer(a, gen, CONSUL).execute());
	}
}
