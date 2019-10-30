import org.junit.Before;
import org.junit.Test;
import io.intino.tara.magritte.Graph;
import io.intino.konos.builder.graph.Graph;

public class cobranzaTest {

	private Graph graph;

	@Before
	public void setUp() {
		Graph graph = new Graph().loadStashes("cobranza").as(Graph.class);
	}

	@Test
	public void testModel() throws Exception {

	}
}
