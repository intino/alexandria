package monet;

import tara.magritte.Graph;
import teseo.TeseoApplication;
import teseo.codegeneration.accessor.rest.RESTAccessorRenderer;
import teseo.codegeneration.server.rest.RESTServiceRenderer;
import teseo.rest.RESTService;

import java.io.File;

public class MonetTest {

    public static void main(String[] args) {
        Graph monet = Graph.load("Monet").wrap(TeseoApplication.class);
        File genFolder = new File("test-gen", "monet");
		new RESTServiceRenderer(monet).execute(genFolder, genFolder, "monet");
		monet.find(RESTService.class).forEach(rs -> new RESTAccessorRenderer(rs).execute(new File("test-gen", "monet"), "monet"));
	}
}
