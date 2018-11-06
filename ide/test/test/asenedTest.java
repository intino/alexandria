import io.intino.tara.magritte.Graph;
import org.junit.Before;
import org.junit.Test;

public class asenedTest {

	private Graph graph;

	@Before
	public void setUp() {
		Graph graph = new Graph().loadStashes("asened").as(Graph.class);
	}

	@Test
	public void testModel() throws Exception {

	}
}
