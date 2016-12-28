package channels;

import io.intino.pandora.model.PandoraApplication;
import io.intino.pandora.plugin.codegeneration.FullRenderer;
import org.junit.Test;
import io.intino.tara.magritte.Graph;

import java.io.File;

public class ChannelsGenerationTest {

	private static final String CHANNELS = "channels";

	@Test
	public void testChannelsGeneration() throws Exception {
		File gen = new File("test-gen", CHANNELS);
		new FullRenderer(null, Graph.load("Channels").wrap(PandoraApplication.class), gen, gen, CHANNELS).execute();
	}

}
