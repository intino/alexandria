package io.intino.konos.builder.codegeneration.accessor.analytic;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.analytic.CategoricalAxisTemplate;
import io.intino.konos.builder.codegeneration.analytic.ContinuousAxisTemplate;
import io.intino.konos.builder.codegeneration.analytic.CubeTemplate;
import io.intino.konos.builder.codegeneration.analytic.CubeWithGettersTemplate;
import io.intino.konos.builder.codegeneration.facts.FactRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.Axis;
import io.intino.konos.model.graph.Cube;
import io.intino.konos.model.graph.Cube.Fact.Column;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.SizedData;
import io.intino.magritte.framework.Concept;
import io.intino.magritte.framework.Predicate;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.codegeneration.Formatters.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class AnalyticBuilderRenderer extends Renderer {

	private final KonosGraph graph;
	private final File destination;
	private final String packageName;
	private final FactRenderer factRenderer;

	public AnalyticBuilderRenderer(CompilationContext compilationContext, KonosGraph graph, File destination) {
		super(compilationContext, Target.Owner);
		this.graph = graph;
		this.destination = destination;
		this.destination.mkdirs();
		this.packageName = compilationContext.packageName();
		this.factRenderer = new FactRenderer();
	}

	@Override
	public void render() {
		renderAxes(graph.axisList());
		renderCubes(graph);
		renderBuilder(graph);
	}

	private void renderAxes(List<Axis> axes) {
		writeFrame(destinationDirectory(), "Axis", customize(new CategoricalAxisTemplate())
				.render(new FrameBuilder("interface").add("package", context.packageName()).toFrame()));
		for (Axis axis : axes)
			if (axis.isCategorical()) renderAxis(axis.asCategorical());
			else renderAxis(axis.asContinuous());
	}

	private void renderCubes(KonosGraph graph) {
		graph.cubeList().stream().filter(c -> !c.isVirtual()).forEach(cube -> {
			writeFrame(new File(destinationDirectory(), "cubes"), cube.name$(),
					cubeTemplate().render(renderCube(cube).toFrame()));
		});
	}

	private FrameBuilder renderCube(Cube cube) {
		FrameBuilder fb = new FrameBuilder("cube").add("package", packageName).add("name", cube.name$());
		factRenderer.render(cube, fb);
		return fb;
	}

	private void renderBuilder(KonosGraph graph) {
		FrameBuilder builder = new FrameBuilder("builder").add("package", packageName).add("name", context.boxName() + "AnalyticBuilder");
		graph.cubeList().stream().filter(c -> !c.isVirtual()).forEach(cube -> builder.add("cube", renderCube(cube)));
		writeFrame(destinationDirectory(), context.boxName() + "AnalyticBuilder", builderTemplate().render(builder.toFrame()));
	}

	private void renderAxis(Axis.Categorical axis) {
		FrameBuilder fb = new FrameBuilder("axis").
				add("package", context.packageName()).add("name", snakeCaseToCamelCase().format(axis.name$()).toString()).add("label", axis.label());
		if (axis.asAxis().isDynamic()) fb.add("dynamic", ";");
		if (axis.includeLabel() != null)
			fb.add("include", new FrameBuilder("include").add("name", "label").add("index", 2));
		int offset = offset(axis);
		if (axis.include() != null) {
			List<Axis> includes = axis.include().axes();
			for (int i = 0; i < includes.size(); i++)
				fb.add("include", new FrameBuilder("include").add("name", includes.get(i).name$()).add("index", i + offset));
		}
		writeFrame(new File(destinationDirectory(), "axes"),
				firstUpperCase(snakeCaseToCamelCase().format(axis.name$()).toString()), customize(new CategoricalAxisTemplate()).render(fb.toFrame()));
	}

	private int offset(Axis.Categorical axis) {
		return axis.includeLabel() != null ? 3 : 2;
	}

	private void renderAxis(Axis.Continuous axis) {
		FrameBuilder fb = new FrameBuilder("continuous").add("package", context.packageName()).add("name", snakeCaseToCamelCase().format(axis.name$()).toString()).add("label", axis.label());
		fb.add("rangeSize", axis.rangeList().size());
		int index = 0;
		for (Axis.Continuous.Range range : axis.rangeList()) {
			FrameBuilder rangeFb = new FrameBuilder("range").add("index", index);
			if (range.isLowerBound()) rangeFb.add("lower").add("bound", range.asLowerBound().lowerBound());
			else if (range.isUpperBound()) rangeFb.add("upper").add("bound", range.asUpperBound().upperBound());
			else if (range.isBound())
				rangeFb.add("lower", range.asBound().lowerBound()).add("upper", range.asBound().upperBound());
			fb.add("range", rangeFb);
			index++;
		}
		writeFrame(new File(destinationDirectory(), "axes"), firstUpperCase(snakeCaseToCamelCase().format(axis.name$()).toString()),
				customize(new ContinuousAxisTemplate()).render(fb.toFrame()));
	}

	private File destinationDirectory() {
		return new File(destination, packageName.replace(".", "/") + "/analytic");
	}

	private Template builderTemplate() {
		return Formatters.customize(new BuilderTemplate());
	}

	private Template cubeTemplate() {
		return Formatters.customize(new CubeWithSettersTemplate());
	}
}
