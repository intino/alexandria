package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.Cube.Fact.Column;
import io.intino.magritte.framework.Concept;
import io.intino.magritte.framework.Layer;
import io.intino.magritte.framework.Predicate;

import java.io.File;
import java.util.List;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.Commons.*;

public class AnalyticRenderer extends Renderer {
	private final File src;
	private final File gen;
	private final KonosGraph graph;

	public AnalyticRenderer(CompilationContext context, KonosGraph graph) {
		super(context, Target.Owner);
		this.src = new File(context.src(Target.Owner), "analytic");
		this.gen = new File(context.gen(Target.Owner), "analytic");
		this.graph = graph;
	}

	@Override
	protected void render() {
		renderAxes(graph.axisList());
		renderCubes(graph.cubeList());
	}

	private void renderAxes(List<Axis> axes) {
		writeFrame(gen, "Axis", customize(new AxisTemplate()).render(new FrameBuilder("interface").add("package", context.packageName()).toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(axes.get(0)), javaFile(gen, "Axis").getAbsolutePath()));
		for (Axis axis : graph.core$().find(Axis.class))
			if (axis.isCategorical()) renderCategorical(axis.asCategorical());
			else renderDistribution(axis.asContinuous());
	}

	private void renderCategorical(Axis.Categorical axis) {
		FrameBuilder fb = new FrameBuilder("axis").
				add("package", context.packageName()).add("name", axis.name$()).add("label", axis.label());
		if (axis.asAxis().isDynamic()) fb.add("dynamic", ";");
		if (axis.includeLabel() != null)
			fb.add("include", new FrameBuilder("include").add("name", "label").add("index", 3));
		for (int i = 0; i < axis.includeList().size(); i++)
			fb.add("include", new FrameBuilder("include").add("name", axis.includeList().get(i).name$()).add("index", i + offset(axis)));
		writeFrame(new File(gen, "axes"), firstUpperCase(axis.name$()), customize(new AxisTemplate()).render(fb.add("abstract").toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(axis), javaFile(new File(gen, "axes"), "Abstract" + firstUpperCase(axis.name$())).getAbsolutePath()));
	}

	private void renderCubes(List<Cube> cubeList) {
		for (Cube cube : cubeList) {
			FrameBuilder fb = new FrameBuilder("cube").add("package", context.packageName()).add("name", cube.name$());
			if (cube.isVirtual()) renderVirtualCube(cube.asVirtual(), fb);
			else renderCube(cube, fb);
		}
	}

	private int offset(Axis.Categorical axis) {
		return axis.includeLabel() != null ? 3 : 2;
	}

	private void renderCube(Cube cube, FrameBuilder fb) {
		addDimensionsAndIndicators(cube, null, fb);
		fb.add("id", cube.fact().columnList().stream().filter(SizedData::isId).map(Layer::name$).findFirst().orElse(null));
		if (cube.splitted() != null) addSplit(cube, fb);
		addFact(cube, fb);
		write(cube, fb);
	}

	private void renderVirtualCube(Cube.Virtual virtualCube, FrameBuilder fb) {
		for (Cube reference : virtualCube.cubes()) {
			addDimensionsAndIndicators(reference, virtualCube.asCube(), fb);
			addFact(reference, fb);
			write(virtualCube.asCube(), fb);
		}
	}

	private void write(Cube cube, FrameBuilder fb) {
		writeFrame(new File(gen, "cubes"), "Abstract" + firstUpperCase(cube.name$()), customize(template(cube)).render(fb.toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(cube), javaFile(new File(gen, "cubes"), "Abstract" + firstUpperCase(cube.name$())).getAbsolutePath()));
//			if (!alreadyRendered(new File(src, "cubes"), cube.name$()))
		writeFrame(new File(src, "cubes"), cube.name$(), customize(template(cube)).render(fb.add("src").toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(cube), javaFile(new File(src, "cubes"), firstUpperCase(cube.name$())).getAbsolutePath()));
	}

	private void addFact(Cube cube, FrameBuilder fb) {
		int offset = 0;
		for (Column column : cube.fact().columnList()) {
			fb.add("column", columnFrame(column, offset));
			offset += column.asType().size();
		}
		fb.add("size", offset);
	}

	private void addSplit(Cube cube, FrameBuilder fb) {
		fb.add("split", new FrameBuilder("split").add("name", cube.splitted().name$()).add("value", cube.splitted().splits().toArray(new String[0])));
	}

	private Template template(Cube cube) {
		return cube.isVirtual() ? new VirtualCubeTemplate() : new CubeTemplate();
	}

	private void addDimensionsAndIndicators(Cube cube, Cube sourceCube, FrameBuilder fb) {
		cube.dimensionList().forEach(selector -> fb.add("dimension", dimensionFrame(cube, sourceCube, selector)));
		cube.indicatorList().forEach(indicator -> fb.add("indicator", inicatorFrame(cube, sourceCube, indicator)));
		for (Cube.CustomFilter customFilter : cube.customFilterList())
			fb.add("customFilter", new FrameBuilder("customFilter")
					.add("cube", sourceCube != null ? sourceCube.name$() : cube.name$())
					.add("name", customFilter.name$()));
	}

	private FrameBuilder dimensionFrame(Cube cube, Cube sourceCube, Cube.Dimension dimension) {
		FrameBuilder fb = new FrameBuilder("dimension", dimension.axis().core$().is(Axis.Categorical.class) ? "categorical" : "distribution").
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

	private FrameBuilder inicatorFrame(Cube cube, Cube sourceCube, Cube.Indicator indicator) {
		return new FrameBuilder("indicator").add(indicator.isAverage() ? "average" : "sum").
				add("cube", sourceCube != null ? sourceCube.name$() : cube.name$()).
				add("name", indicator.name$()).
				add("label", indicator.label()).
				add("source", indicator.source().name$()).
				add("unit", indicator.unit());
	}

	private FrameBuilder columnFrame(Column column, int offset) {
		if (column.isCategory()) return processCategoryColumn(column.asCategory(), column.name$(), offset);
		else return processColumn(column, offset);
	}

	private FrameBuilder processColumn(Column column, int offset) {
		SizedData.Type asType = column.asType();
		FrameBuilder builder = new FrameBuilder("column", column.core$().is(Cube.Fact.Attribute.class) ? "attribute" : "measure")
				.add("name", column.a$(Column.class).name$())
				.add("offset", offset)
				.add("type", type(asType));
		column.core$().conceptList().stream().filter(Concept::isAspect).map(Predicate::name).forEach(builder::add);
		if (isAligned(asType, offset)) builder.add("aligned", "Aligned");
		else builder.add("bits", asType.size());
		builder.add("size", asType.size());
		return builder;
	}

	private FrameBuilder processCategoryColumn(SizedData.Category category, String name, int offset) {
		Axis.Categorical factor = category.axis().asCategorical();
		return new FrameBuilder("column", "categorical").
				add("name", name).
				add("type", factor.name$()).
				add("offset", offset).
				add("bits", category.size());
	}

	private String type(SizedData.Type type) {
		if (type.i$(SizedData.Category.class)) return type.name$();
		return isPrimitive(type) ? type.primitive() : type.type();
	}

	private boolean isAligned(SizedData.Type attribute, int offset) {
		return (offset == 0 || log2(offset) % 1 == 0) && attribute.maxSize() == attribute.size();
	}

	private boolean isPrimitive(SizedData.Type attribute) {
		SizedData data = attribute.asSizedData();
		return data.isBool() || data.isInteger() || data.isLongInteger() || data.isId() || data.isReal();
	}

	public static double log2(int N) {
		return (Math.log(N) / Math.log(2));
	}

	private void renderDistribution(Axis.Continuous axis) {
		FrameBuilder fb = new FrameBuilder("distribution").add("package", context.packageName()).add("name", axis.name$()).add("label", axis.label());
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
		writeFrame(new File(gen, "distributions"), firstUpperCase(axis.name$()), customize(new DistributionTemplate()).render(fb.toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(axis), javaFile(new File(gen, "distributions"), firstUpperCase(axis.name$())).getAbsolutePath()));
	}

}

