package amidas;

import io.intino.pandora.plugin.PandoraApplication;
import io.intino.pandora.plugin.codegeneration.FullRenderer;
import io.intino.pandora.plugin.codegeneration.accessor.jms.JMSAccessorRenderer;
import io.intino.pandora.plugin.jms.JMSService;
import org.junit.Test;
import tara.magritte.Graph;

import java.io.File;

public class AmidasGenerationTest {

	private static final String AMIDAS = "amidas";


	@Test
	public void testAmidas() throws Exception {
		File gen = new File("test-gen", AMIDAS);
		final Graph graph = Graph.load("Amidas").wrap(PandoraApplication.class);
		new FullRenderer(null, graph, gen, gen, AMIDAS).execute();
		for (JMSService jmsService : graph.find(JMSService.class))
			new JMSAccessorRenderer(jmsService).execute(gen, AMIDAS);
	}

	@Test
	public void swaggerAccessorsCreator() throws Exception {
	}
}
