import io.intino.pandora.plugin.PandoraApplication;
import org.junit.Before;
import org.junit.Test;
import tara.magritte.Graph;

public class uiTest {

	private PandoraApplication application;

	@Before
	public void setUp() {
		Graph graph = Graph.load("ui").wrap(PandoraApplication.class);
		this.application = graph.application();
		this.application.execute();
	}

	@Test
	public void testModel() throws Exception {

	}
}
