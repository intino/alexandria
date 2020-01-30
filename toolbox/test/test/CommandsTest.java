import com.google.gson.GsonBuilder;
import utils.TestUtil;
import io.intino.konos.builder.Manifest;
import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class CommandsTest {

	private static final String COMMANDS = "commands";

	@Test
	public void testModel() throws Exception {
		File gen = new File("test-gen", COMMANDS);
		KonosGraph graph = new Graph().loadStashes("Command").as(KonosGraph.class);
		new FullRenderer(graph, TestUtil.settings(gen, COMMANDS), compiledFiles).execute();
	}

	@Test
	public void name() {
		Manifest load = Manifest.load();
		System.out.println(load.dependencies.keySet().iterator().next());
	}


	@Test
	public void nameS() {
		Manifest manifest = new Manifest();
		Manifest.Action action = new Manifest.Action();
		action.id = "aaaa";
		action.aClass = "aannn";
		manifest.actions.add(action);
		manifest.dependencies.put("bbb", "aaa");
		System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(manifest));
	}
}
