package withoutscheme;

import tara.magritte.Graph;
import teseo.TeseoApplication;
import teseo.codegeneration.accessor.JavaAccessorRenderer;
import teseo.codegeneration.server.web.JavaServerRenderer;
import teseo.rest.RESTService;

import java.io.File;

public class WithoutSchemaText {

    public static void main(String[] args) {
        Graph withoutSchema = Graph.load("WithoutSchema").wrap(TeseoApplication.class);
        File genFolder = new File("test-gen", "withoutSchema");
        new JavaServerRenderer(withoutSchema).execute(genFolder, genFolder, "withoutSchema");
        withoutSchema.find(RESTService.class).forEach(rs -> new JavaAccessorRenderer(rs).execute(new File("test-gen/" + rs.name(), "withoutSchema"), "withoutSchema"));
    }
}
