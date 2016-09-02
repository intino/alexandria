package monet;

import tara.magritte.Graph;
import teseo.TeseoApplication;
import teseo.codegeneration.accessor.JavaAccessorRenderer;
import teseo.codegeneration.server.web.JavaServerRenderer;
import teseo.rest.RESTService;

import java.io.File;

public class MonetTest {

    public static void main(String[] args) {
        Graph monet = Graph.load("Monet").wrap(TeseoApplication.class);
        File genFolder = new File("test-gen", "monet");
        new JavaServerRenderer(monet).execute(genFolder, genFolder, "monet");
        monet.find(RESTService.class).forEach(rs -> new JavaAccessorRenderer(rs).execute(new File("test-gen", "monet"), "monet"));
    }
}
