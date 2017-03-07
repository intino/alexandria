package bus;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.model.Konos;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class ChannelsGenerationTest {

	private static final String EXAMPLE = "example";

	@Test
	public void testChannelsGeneration() throws Exception {
		File gen = new File("test-gen", EXAMPLE);
		new FullRenderer(null, Graph.use(Konos.class, null).load("EventHandlers"), gen, gen, gen, gen, EXAMPLE).execute();
	}

}
