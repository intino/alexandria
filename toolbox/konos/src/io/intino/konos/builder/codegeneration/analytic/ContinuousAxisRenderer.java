package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.Axis;

import java.io.File;
import java.util.List;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.codegeneration.Formatters.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.*;

public class ContinuousAxisRenderer {

	private final CompilationContext context;
	private final File gen;

	public ContinuousAxisRenderer(CompilationContext context, File gen) {
		this.context = context;
		this.gen = gen;
	}

	public void render(Axis.Continuous axis) {
		FrameBuilder fb = new FrameBuilder("continuous");
		addAxisBasicInfo(fb, axis);
		setRanges(fb, axis);
		writeFrame(new File(gen, "axes"), firstUpperCase(snakeCaseToCamelCase().format(axis.name$()).toString()), customize(new ContinuousAxisTemplate()).render(fb.toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(axis), javaFile(new File(gen, "axes"), firstUpperCase(snakeCaseToCamelCase().format(axis.name$()).toString())).getAbsolutePath()));
	}

	private void setRanges(FrameBuilder fb, Axis.Continuous axis) {
		final List<Axis.Continuous.Range> ranges = axis.rangeList();
		for (int i = 0; i < ranges.size(); i++) {
			addRange(fb, i, ranges.get(i));
		}
	}

	private void addRange(FrameBuilder fb, int index, Axis.Continuous.Range range) {
		FrameBuilder rangeFb = new FrameBuilder("range").add("index", index);

		if (range.isLowerBound())
			rangeFb.add("lower").add("bound", range.asLowerBound().lowerBound());
		else if (range.isUpperBound())
			rangeFb.add("upper").add("bound", range.asUpperBound().upperBound());
		else if (range.isBound())
			rangeFb.add("lower", range.asBound().lowerBound()).add("upper", range.asBound().upperBound());

		fb.add("range", rangeFb);

		if (range.label() != null)
			rangeFb.add("label", range.label());
	}

	private void addAxisBasicInfo(FrameBuilder fb, Axis.Continuous axis) {
		fb.add("package", context.packageName())
				.add("name", snakeCaseToCamelCase().format(axis.name$()).toString())
				.add("label", axis.label())
				.add("rangeSize", axis.rangeList().size());
	}

}
