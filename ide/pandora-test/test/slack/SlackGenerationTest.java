package slack;

import io.intino.pandora.model.PandoraApplication;
import io.intino.pandora.builder.codegeneration.FullRenderer;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class SlackGenerationTest {

	private static final String SLACK = "slack";

	@Test
	public void testSlack() throws Exception {
		File gen = new File("test-gen", SLACK);
		new FullRenderer(null, Graph.use(PandoraApplication.class, null).load("Slack"), gen, gen, SLACK).execute();
	}

}
