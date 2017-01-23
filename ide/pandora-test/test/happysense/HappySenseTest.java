package happysense;

import io.intino.pandora.model.PandoraApplication;
import io.intino.pandora.model.rest.RESTService;
import io.intino.pandora.builder.codegeneration.FullRenderer;
import io.intino.pandora.builder.codegeneration.accessor.rest.RESTAccessorRenderer;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class HappySenseTest {

	private static final String HAPPYSENSE = "happysense";

	@Test
	public void testCreation() {
		Graph happysense = Graph.use(PandoraApplication.class, null).load("Happysense");
		File gen = new File("test-gen", HAPPYSENSE);
		new FullRenderer(null, happysense, gen, gen, HAPPYSENSE).execute();
		happysense.find(RESTService.class).forEach(a -> new RESTAccessorRenderer(a, new File("test-gen/happysense"), HAPPYSENSE).execute());
	}
}
