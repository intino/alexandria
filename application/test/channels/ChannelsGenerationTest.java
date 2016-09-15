package channels;

import org.junit.Test;
import tara.magritte.Graph;
import teseo.TeseoApplication;
import teseo.codegeneration.accessor.jms.JMSAccessorRenderer;
import teseo.codegeneration.server.jms.JMSResourceRenderer;
import teseo.jms.JMSService;

import java.io.File;

public class ChannelsGenerationTest {

	private static final String CHANNELS = "channels";


	@Test
	public void testChannelsGeneration() throws Exception {
		Graph graph = Graph.load("Channels").wrap(TeseoApplication.class);
		File gen = new File("test-gen", CHANNELS);
		graph.find(JMSService.class).forEach(a -> new JMSAccessorRenderer(a).execute(gen, CHANNELS));
		new JMSResourceRenderer(graph).execute(gen, gen, CHANNELS);
	}

}
