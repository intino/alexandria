package bus;

import io.intino.konos.builder.codegeneration.FullRenderer;
import io.intino.konos.model.Konos;
import io.intino.tara.magritte.Graph;
import org.junit.Test;

import java.io.File;

public class DataLakeGenerationTest {

	private static final String DATALAKE = "datalake";

	@Test
	public void testChannelsGeneration() throws Exception {
		File gen = new File("test-gen", DATALAKE);
		new FullRenderer(null, Graph.use(Konos.class, null).load("DataLake"), gen, gen, gen, DATALAKE).execute();
	}

}
