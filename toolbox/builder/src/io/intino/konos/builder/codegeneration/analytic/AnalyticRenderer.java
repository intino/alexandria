package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Axis;
import io.intino.konos.model.graph.Cube;
import io.intino.konos.model.graph.KonosGraph;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.codegeneration.Formatters.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.*;

public class AnalyticRenderer extends Renderer {

	private final File src;
	private final File gen;
	private final KonosGraph graph;
	private final FactRenderer factRenderer;

	public AnalyticRenderer(CompilationContext context, KonosGraph graph) {
		super(context, Target.Owner);
		this.src = new File(context.src(Target.Owner), "analytic");
		this.gen = new File(context.gen(Target.Owner), "analytic");
		this.graph = graph;
		this.factRenderer = new FactRenderer();
	}

	@Override
	protected void render() {
		renderAxes(graph.axisList());
		renderCubes(graph.cubeList());
	}

	private void renderAxes(List<Axis> axes) {
		if (axes.isEmpty()) return;
		writeFrame(gen, "Axis", customize(new CategoricalAxisTemplate()).render(new FrameBuilder("interface")
				.add("package", context.packageName()).toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(axes.get(0)), javaFile(gen, "Axis").getAbsolutePath()));
		for (Axis axis : axes)
			if (axis.isCategorical()) renderAxis(axis.asCategorical());
			else renderAxis(axis.asContinuous());
	}

	private void renderCubes(List<Cube> cubeList) {
		for (Cube cube : cubeList) {
			FrameBuilder fb = new FrameBuilder("cube").add("package", context.packageName()).add("name", cube.name$());
			if (cube.isVirtual()) renderVirtualCube(cube.asVirtual(), fb);
			else renderCube(cube, fb);
		}
	}

	private void renderCube(Cube cube, FrameBuilder fb) {
		addDimensionsAndIndicators(cube, null, fb);
		fb.add("id", factRenderer.idOf(cube.fact()).name$());
		if (cube.splitted() != null) addSplit(cube, fb);
		fb.add("index", new FrameBuilder("index", cube.index() != null ? "normal" : "total").add("value", ""));
		factRenderer.addFact(cube, fb);
		write(cube, fb);
	}

	private void renderVirtualCube(Cube.Virtual virtualCube, FrameBuilder fb) {
		for (Cube reference : new Cube[]{virtualCube.main(), virtualCube.join()}) {
			addDimensionsAndIndicators(reference, virtualCube.asCube(), fb);
			factRenderer.addFact(reference, fb);
		}
		fb.add("index", new FrameBuilder("index", virtualCube.index() != null ? "index" : "total").add("value", ""));
		fb.add("mainCube", virtualCube.main().name$());
		fb.add("joinCube", virtualCube.join().name$());
		fb.add("cube", new String[]{virtualCube.main().name$(), virtualCube.join().name$()});
		if (virtualCube.main().splitted() != null) addSplit(virtualCube.main(), fb);
		write(virtualCube.asCube(), fb);
	}

	private void write(Cube cube, FrameBuilder fb) {
		Template template = customize(template(cube));
		writeFrame(new File(gen, "cubes"), "Abstract" + firstUpperCase(cube.name$()), template.render(fb.toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(cube), javaFile(new File(gen, "cubes"), "Abstract" + firstUpperCase(cube.name$())).getAbsolutePath()));
		if (alreadyRendered(new File(src, "cubes"), cube.name$())) return;
		writeFrame(new File(src, "cubes"), cube.name$(), template.render(fb.add("src").toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(cube), javaFile(new File(src, "cubes"), firstUpperCase(cube.name$())).getAbsolutePath()));
	}

	private void addSplit(Cube cube, FrameBuilder fb) {
		fb.add("split", new FrameBuilder("split").add("name", cube.splitted().name$()).add("value", cube.splitted().splits().toArray(new String[0])));
	}

	private Template template(Cube cube) {
		return cube.isVirtual() ? new VirtualCubeTemplate() : new CubeTemplate();
	}

	private void addDimensionsAndIndicators(Cube cube, Cube sourceCube, FrameBuilder fb) {
		cube.dimensionList().forEach(selector -> fb.add("dimension", dimensionFrame(cube, sourceCube, selector)));
		cube.indicatorList().forEach(indicator -> fb.add("indicator", indicatorFrame(cube, sourceCube, indicator)));
		for (Cube.CustomFilter customFilter : cube.customFilterList())
			fb.add("customFilter", new FrameBuilder("customFilter")
					.add("cube", sourceCube != null ? sourceCube.name$() : cube.name$())
					.add("name", customFilter.name$()));
	}

	private FrameBuilder dimensionFrame(Cube cube, Cube sourceCube, Cube.Dimension dimension) {
		FrameBuilder fb = new FrameBuilder("dimension", dimension.axis().core$().is(Axis.Categorical.class) ? "categorical" : "continuous").
				add("cube", sourceCube != null ? sourceCube.name$() : cube.name$()).
				add("name", dimension.name$()).
				add("source", dimension.attribute().name$()).
				add("axis", dimension.axis().name$());
		if (dimension.axis().i$(Axis.Categorical.class)) {
			fb.add("type", dimension.axis().name$());
			if (!dimension.attribute().asCategory().axis().equals(dimension.axis()))
				fb.add("child", dimension.axis().name$());
		}
		return fb;
	}

	private FrameBuilder indicatorFrame(Cube cube, Cube sourceCube, Cube.Indicator indicator) {
		return new FrameBuilder("indicator").add(indicator.isAverage() ? "average" : "sum").
				add("cube", sourceCube != null ? sourceCube.name$() : cube.name$()).
				add("name", indicator.name$()).
				add("label", indicator.label()).
				add("index", new FrameBuilder((sourceCube == null && cube.index() != null) || (sourceCube != null && sourceCube.index() != null) ? "index" : "total")).
				add("source", indicator.source().name$()).
				add("unit", indicator.unit());
	}

	private void renderAxis(Axis.Categorical axis) {
		FrameBuilder fb = new FrameBuilder("axis").
				add("package", context.packageName()).add("name", axis.name$()).add("label", axis.label());
		if (axis.asAxis().isDynamic()) fb.add("dynamic", ";");
		fb.add("resource", axisResource(axis.tsv().getPath()));
		if (axis.includeLabel() != null)
			fb.add("include", new FrameBuilder("include").add("name", "label").add("index", 2));
		int offset = offset(axis);
		if (axis.include() != null) {
			List<Axis> includes = axis.include().axes();
			for (int i = 0; i < includes.size(); i++)
				fb.add("include", new FrameBuilder("include").add("name", includes.get(i).name$()).add("index", i + offset));
		}
		writeFrame(new File(gen, "axes"), firstUpperCase(snakeCaseToCamelCase().format(axis.name$()).toString()), customize(new CategoricalAxisTemplate()).render(fb.toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(axis), javaFile(new File(gen, "axes"), firstUpperCase(snakeCaseToCamelCase().format(axis.name$()).toString())).getAbsolutePath()));
	}

	private String axisResource(String resource) {
		Path res = context.res(Target.Owner).toPath();
		return res.relativize(new File(resource).toPath().toAbsolutePath()).toFile().getPath().replace("\\", "/");
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
			if (range.label() != null) rangeFb.add("label", range.label());
			index++;
		}
		writeFrame(new File(gen, "axes"), firstUpperCase(snakeCaseToCamelCase().format(axis.name$()).toString()), customize(new ContinuousAxisTemplate()).render(fb.toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(axis), javaFile(new File(gen, "axes"), firstUpperCase(snakeCaseToCamelCase().format(axis.name$()).toString())).getAbsolutePath()));
	}

	private boolean alreadyRendered(File destination, String name) {
		return Commons.javaFile(destination, name).exists();
	}
}

