package amidas;

import io.intino.pandora.plugin.PandoraApplication;
import io.intino.pandora.plugin.codegeneration.schema.SchemaRenderer;
import org.junit.Test;
import tara.magritte.Graph;

import java.io.File;

public class AmidasGenerationTest {

	private static final String AMIDAS = "amidas";


	@Test
	public void testAmidas() throws Exception {
		File gen = new File("test-gen", AMIDAS);
		final Graph graph = Graph.load("Amidas").wrap(PandoraApplication.class);
		new SchemaRenderer(graph, gen, AMIDAS).execute();
//		new FullRenderer(null, graph, gen, gen, AMIDAS).execute();
//		for (JMSService jmsService : graph.find(JMSService.class))
//			new JMSAccessorRenderer(jmsService).execute(gen, AMIDAS);
	}

	@Test
	public void swaggerAccessorsCreator() throws Exception {
	}
}
