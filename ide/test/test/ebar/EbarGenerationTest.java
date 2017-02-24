package ebar;

import io.intino.konos.model.Konos;
import io.intino.konos.builder.codegeneration.FullRenderer;
import org.junit.Ignore;
import org.junit.Test;
import io.intino.tara.magritte.Graph;

import java.io.File;

@Ignore
public class EbarGenerationTest {

	private static final String EBAR = "ebar";

	@Test
	@Ignore
	public void testEbarGeneration() throws Exception {
		File gen = new File("test-gen", EBAR);
		new FullRenderer(null, Graph.use(Konos.class, null).load("Ebar"), gen, gen, gen, EBAR).execute();
	}

}
