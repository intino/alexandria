package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.Axis;

import java.io.File;
import java.util.Collection;

import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class AxisInterfaceRenderer {

	public static void render(File gen, CompilationContext context, Collection<Axis> axes) {
		FrameBuilder fb = new FrameBuilder("interface");
		fb.add("package", context.packageName());
		setAxes(fb, axes);
		writeFrame(gen, "Axis", new AxisInterfaceTemplate().render(fb.toFrame(), Formatters.all));
	}

	private static void setAxes(FrameBuilder fb, Collection<Axis> axes) {
		for (Axis axis : axes) {
			fb.add("axis", new FrameBuilder("axis")
					.add("axis", axis.name$()));
		}
	}

}
