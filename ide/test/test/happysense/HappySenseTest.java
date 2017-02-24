package happysense;

import io.intino.konos.model.Konos;
import io.intino.konos.model.rest.RESTService;
import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.builder.codegeneration.accessor.rest.RESTAccessorRenderer;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class HappySenseTest {

	private static final String HAPPYSENSE = "happysense";

	@Test
	public void testCreation() {
		Graph happysense = Graph.use(Konos.class, null).load("Happysense");
		File gen = new File("test-gen", HAPPYSENSE);
		new FullRenderer(null, happysense, gen, gen, gen, HAPPYSENSE).execute();
		happysense.find(RESTService.class).forEach(a -> new RESTAccessorRenderer(a, new File("test-gen/happysense"), HAPPYSENSE).execute());
	}
}
