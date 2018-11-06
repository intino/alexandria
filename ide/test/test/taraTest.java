import io.intino.tara.magritte.Graph;
import org.junit.Before;
import org.junit.Test;

public class taraTest {

	private Graph graph;

	@Before
	public void setUp() {
		Graph graph = new Graph().loadStashes("tara").as(Graph.class);
	}

	@Test
	public void testModel() throws Exception {

	}
}
