import io.intino.pandora.builder.codegeneration.FullRenderer;
import io.intino.pandora.model.PandoraApplication;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class CommandsTest {

	private static final String COMMANDS = "commands";

	@Test
	public void testModel() throws Exception {
		File gen = new File("test-gen", COMMANDS);
		new FullRenderer(null, Graph.use(PandoraApplication.class, null).load("Command"), gen, gen, COMMANDS).execute();
	}
}
