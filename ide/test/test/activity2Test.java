import org.junit.Before;
import org.junit.Test;
import io.intino.tara.magritte.Graph;
import io.intino.konos.builder-test.graph.Builder-test;

public class activity2Test {

	private Builder-
	test application;

	@Before
	public void setUp() {
		Graph graph = Graph.use(Builder - test.class).load("activity2");
		this.application = graph.wrapper(Builder - test.class);
	}

	@Test
	public void testModel() throws Exception {

	}
}
