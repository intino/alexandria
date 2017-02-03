package channels;

import io.intino.konos.model.Konos;
import io.intino.konos.builder.codegeneration.FullRenderer;
import org.junit.Test;
import io.intino.tara.magritte.Graph;

import java.io.File;

public class ChannelsGenerationTest {

	private static final String CHANNELS = "channels";

	@Test
	public void testChannelsGeneration() throws Exception {
		File gen = new File("test-gen", CHANNELS);
		new FullRenderer(null, Graph.use(Konos.class, null).load("Channels"), gen, gen, CHANNELS).execute();
	}

}
