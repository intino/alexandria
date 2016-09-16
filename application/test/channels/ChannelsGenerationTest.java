package channels;

import org.junit.Test;
import tara.magritte.Graph;
import teseo.TeseoApplication;
import teseo.codegeneration.FullRenderer;

import java.io.File;

public class ChannelsGenerationTest {

	private static final String CHANNELS = "channels";

	@Test
	public void testChannelsGeneration() throws Exception {
		File gen = new File("test-gen", CHANNELS);
		new FullRenderer(Graph.load("Channels").wrap(TeseoApplication.class), gen, gen, CHANNELS).execute();
	}

}
