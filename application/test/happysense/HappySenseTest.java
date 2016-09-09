package happysense;

import tara.magritte.Graph;
import teseo.TeseoApplication;
import teseo.codegeneration.server.rest.JavaServerRenderer;

import java.io.File;

public class HappySenseTest {

    public static void main(String[] args) {
        Graph happysense = Graph.load("Happysense").wrap(TeseoApplication.class);
        File genFolder = new File("test-gen", "happysense");
        new JavaServerRenderer(happysense).execute(genFolder, genFolder, "happysense");
//        happysense.find(RESTService.class).forEach(a -> new JavaAccessorRenderer(a).execute(new File("test-gen/"), "happysense"));
    }
}
