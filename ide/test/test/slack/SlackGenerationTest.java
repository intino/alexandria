package slack;

import io.intino.konos.model.Konos;
import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class SlackGenerationTest {

	private static final String SLACK = "slack";

	@Test
	public void testSlack() throws Exception {
		File gen = new File("test-gen", SLACK);
		new FullRenderer(null, Graph.use(Konos.class, null).load("Slack"), gen, gen, SLACK).execute();
	}

}
