package happysense;

import org.junit.Test;
import tara.magritte.Graph;
import teseo.TeseoApplication;
import teseo.codegeneration.accessor.JavaAccessorRenderer;
import teseo.codegeneration.server.rest.JavaServerRenderer;
import teseo.framework.web.TeseoSpark;
import teseo.rest.RESTService;

import java.io.File;

public class HappySenseTest {

    @Test
    public void testCreation(String[] args) {
        Graph happysense = Graph.load("Happysense").wrap(TeseoApplication.class);
        File genFolder = new File("test-gen", "happysense");
        new JavaServerRenderer(happysense).execute(genFolder, genFolder, "happysense");
        happysense.find(RESTService.class).forEach(a -> new JavaAccessorRenderer(a).execute(new File("test-gen/happysense"), "happysense"));
    }


    public static void main(String[] args) throws Exception {
        TeseoSpark teseoSpark = new TeseoSpark(8080, false);
        RestResources.setup(teseoSpark, null);
    }
}
