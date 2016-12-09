package slack;

import io.intino.pandora.model.PandoraApplication;
import io.intino.pandora.model.codegeneration.FullRenderer;
import org.junit.Test;
import tara.magritte.Graph;

import java.io.File;

public class SlackGenerationTest {

	private static final String SLACK = "slack";

	@Test
	public void testSlack() throws Exception {
		File gen = new File("test-gen", SLACK);
		new FullRenderer(null, Graph.load("Slack").wrap(PandoraApplication.class), gen, gen, SLACK).execute();
	}

}
