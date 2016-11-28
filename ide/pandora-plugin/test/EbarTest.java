import org.junit.Before;
import org.junit.Test;
import tara.magritte.Graph;
import io.intino.pandora.plugin.PandoraApplication;

public class EbarTest {

	private PandoraApplication application;

	@Before
	public void setUp() {
		Graph graph = Graph.load("ebar").wrap(PandoraApplication.class);
		this.application = graph.application();
		this.application.execute();
	}

	@Test
	public void testModel() throws Exception {

	}
}
