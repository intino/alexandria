package withoutscheme;

import teseo.Application;
import teseo.TeseoApplication;
import teseo.codegeneration.accessor.JavaAccessorRenderer;
import teseo.codegeneration.server.JavaServerRenderer;
import tara.magritte.Graph;

import java.io.File;

public class WithoutSchemaText {

    public static void main(String[] args) {
        Graph withoutSchema = Graph.load("WithoutSchema").wrap(TeseoApplication.class);
        File genFolder = new File("test-gen", "withoutSchema");
        new JavaServerRenderer(withoutSchema).execute(genFolder, genFolder, "withoutSchema");
        withoutSchema.find(Application.class).forEach(a -> new JavaAccessorRenderer(a).execute(new File("test-gen/" + a.name(), "withoutSchema"), "withoutSchema"));
    }
}
