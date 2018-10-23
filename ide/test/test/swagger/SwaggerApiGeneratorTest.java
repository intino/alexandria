package swagger;


import io.intino.konos.builder.codegeneration.swagger.OpenApiDescriptor;
import io.intino.konos.builder.codegeneration.swagger.SwaggerProfileGenerator;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.tara.magritte.Graph;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

@Ignore
public class SwaggerApiGeneratorTest {

	private static final File SWAGGER = new File("test-gen", "swagger");

	static {
		{
			SWAGGER.mkdirs();
		}
	}

	@Test
	public void testApiJSON() throws Exception {
		KonosGraph graph = new Graph().loadStashes("Petstore").as(KonosGraph.class);
		OpenApiDescriptor descriptor = new OpenApiDescriptor(graph.rESTServiceList().get(0));
		final String jsonDescriptor = descriptor.createJSONDescriptor();
		Commons.write(new File(SWAGGER, "petstore.json").toPath(), jsonDescriptor);
//		assertEquals(jsonDescriptor, new String(Files.readAllBytes(new File("test-res", "swagger" + File.separator + "petstore_expected.json").toPath())));
	}

	@Test
	public void testApiCreation() throws Exception {
		KonosGraph graph = new Graph().loadStashes("Petstore").as(KonosGraph.class);
		SwaggerProfileGenerator generator = new SwaggerProfileGenerator(graph.rESTServiceList(), SWAGGER);
		generator.execute();
	}
}
