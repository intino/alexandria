package happysense;

import teseo.TeseoApplication;
import teseo.codegeneration.server.JavaServerRenderer;
import tara.magritte.Graph;

import java.io.File;

public class HappySenseTest {

    public static void main(String[] args) {
        Graph happysense = Graph.load("Happysense").wrap(TeseoApplication.class);
        File genFolder = new File("test-gen", "happysense");
        new JavaServerRenderer(happysense).execute(genFolder, genFolder, "happysense");
//        happysense.find(Application.class).forEach(a -> new JavaAccessorRenderer(a).execute(new File("test-gen/" + a.name(), "happysense"), "happysense"));
    }
}
