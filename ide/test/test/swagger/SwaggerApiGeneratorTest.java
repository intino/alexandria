package swagger;


import io.intino.konos.builder.codegeneration.swagger.OpenApiDescriptor;
import io.intino.konos.builder.codegeneration.swagger.SwaggerGenerator;
import io.intino.konos.model.Konos;
import io.intino.konos.model.rest.RESTService;
import io.intino.tara.magritte.Graph;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.Collections;

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

		final Graph petstore = Graph.use(Konos.class).load("Petstore");
		OpenApiDescriptor descriptor = new OpenApiDescriptor(petstore.find(RESTService.class).get(0));
		final String jsonDescriptor = descriptor.createJSONDescriptor();
		Files.write(new File(SWAGGER, "petstore.json").toPath(), jsonDescriptor.getBytes());
//		assertEquals(jsonDescriptor, new String(Files.readAllBytes(new File("test-res", "swagger" + File.separator + "petstore_expected.json").toPath())));
	}

	@Test
	public void testApiCreation() throws Exception {
		final Graph petstore = Graph.use(Konos.class).load("Petstore");
		SwaggerGenerator generator = new SwaggerGenerator(petstore.wrapper(Konos.class).rESTServiceList(), SWAGGER);
		generator.execute(Collections.singletonList("dynamic-html"));
	}
}
