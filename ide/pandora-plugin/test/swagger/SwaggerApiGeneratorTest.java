package swagger;


import io.intino.pandora.model.PandoraApplication;
import io.intino.pandora.model.rest.RESTService;
import io.intino.pandora.plugin.codegeneration.accessor.rest.swagger.OpenApiDescriptor;
import io.intino.pandora.plugin.codegeneration.accessor.rest.swagger.SwaggerGenerator;
import org.junit.Ignore;
import org.junit.Test;
import tara.magritte.Graph;

import java.io.File;
import java.util.Collections;
@Ignore
public class SwaggerApiGeneratorTest {

	@Test
	public void testApiJSON() throws Exception {
		final Graph petstore = Graph.load("Petstore").wrap(PandoraApplication.class);
		OpenApiDescriptor descriptor = new OpenApiDescriptor(petstore.find(RESTService.class).get(0));
		final String jsonDescriptor = descriptor.createJSONDescriptor();
//		assertEquals(jsonDescriptor, new String(Files.readAllBytes(new File("test-res", "swagger" + File.separator + "petstore_expected.json").toPath())));
	}

	@Test
	public void testApiCreation() throws Exception {
		final Graph petstore = Graph.load("Petstore").wrap(PandoraApplication.class);
		SwaggerGenerator generator = new SwaggerGenerator(petstore.application(), new File("test-gen", "swagger"));
		generator.execute(Collections.singletonList("java"));
	}
}
