package amidas;

import io.intino.konos.builder.codegeneration.accessor.jms.JMSAccessorRenderer;
import io.intino.konos.model.Konos;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class AmidasGenerationTest {

	private static final String AMIDAS = "amidas";


	@Test
	public void testAmidas() throws Exception {
		File gen = new File("test-gen", AMIDAS);
		final Graph graph = Graph.use(Konos.class, null).load("Amidas");
//		new FullRenderer(null, graph, gen, gen, gen, gen, AMIDAS).execute();
		new JMSAccessorRenderer(graph.wrapper(Konos.class).jMSService(0), gen, AMIDAS).execute();
	}

	@Test
	public void swaggerAccessorsCreator() throws Exception {
	}
}
