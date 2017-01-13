package amidas;

import io.intino.pandora.model.PandoraApplication;
import io.intino.pandora.plugin.codegeneration.schema.SchemaRenderer;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class AmidasGenerationTest {

	private static final String AMIDAS = "amidas";


	@Test
	public void testAmidas() throws Exception {
		File gen = new File("test-gen", AMIDAS);
		final Graph graph = Graph.use(PandoraApplication.class, null).load("Amidas");
		new SchemaRenderer(graph, gen, AMIDAS).execute();
//		new FullRenderer(null, graph, gen, gen, AMIDAS).execute();
//		for (JMSService jmsService : graph.find(JMSService.class))
//			new JMSAccessorRenderer(jmsService).execute(gen, AMIDAS);
	}

	@Test
	public void swaggerAccessorsCreator() throws Exception {
	}
}
