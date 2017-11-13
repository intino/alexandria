import org.junit.Before;
import org.junit.Test;
import io.intino.tara.magritte.Graph;
import io.intino.konos.builder.graph.Konos;

public class activityTest {

	private Konos application;

	@Before
	public void setUp() {
		Graph graph = Graph.use(Konos.class).load("activity");
		this.application = graph.wrapper(Konos.class);
	}

	@Test
	public void testModel() throws Exception {

	}
}
