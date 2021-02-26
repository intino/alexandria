package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.context.CompilationContext;

import java.io.File;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class AxisInterfaceRenderer {

    public static void render(File gen, CompilationContext context) {
        writeFrame(gen, "Axis", customize(new AxisInterfaceTemplate()).render(new FrameBuilder("interface")
                .add("package", context.packageName()).toFrame()));
    }

}
