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

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
		writeFrame(gen, "Axis", customize(new CategoricalAxisTemplate()).render(new FrameBuilder("interface").add("package", context.packageName()).toFrame()));
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
		fb.add("id", cube.fact().columnList().stream().filter(SizedData::isId).map(Layer::name$).findFirst().orElse(null));
		if (cube.splitted() != null) addSplit(cube, fb);
		fb.add("index", new FrameBuilder("index", cube.index() != null ? "normal" : "total").add("value", ""));
		addFact(cube, fb);
		write(cube, fb);
	}

	private void renderVirtualCube(Cube.Virtual virtualCube, FrameBuilder fb) {
		for (Cube reference : new Cube[]{virtualCube.main(), virtualCube.join()}) {
			addDimensionsAndIndicators(reference, virtualCube.asCube(), fb);
			addFact(reference, fb);
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
//			if (alreadyRendered(new File(src, "cubes"), cube.name$())) return;
		writeFrame(new File(src, "cubes"), cube.name$(), template.render(fb.add("src").toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(cube), javaFile(new File(src, "cubes"), firstUpperCase(cube.name$())).getAbsolutePath()));
	}

	private void addFact(Cube cube, FrameBuilder fb) {
		int offset = 0;
		List<Column> columns = new ArrayList<>(cube.fact().columnList());
		columns.sort(Comparator.comparingInt(a -> a.asType().size()));
		Collections.reverse(columns);
		for (Column column : columns) {
			fb.add("column", columnFrame(column, offset, cube.name$()));
			offset += sizeOf(column);
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
		FrameBuilder fb = new FrameBuilder("dimension", dimension.axis().core$().is(Axis.Categorical.class) ? "categorical" : "continuous").
				add("cube", sourceCube != null ? sourceCube.name$() : cube.name$()).
				add("name", dimension.name$()).
				add("source", dimension.attribute().name$()).
				add("axis", dimension.axis().name$());
		if (dimension.axis().i$(Axis.Categorical.class)) fb.add("type", dimension.axis().name$());
		return fb;
	}

	private FrameBuilder inicatorFrame(Cube cube, Cube sourceCube, Cube.Indicator indicator) {
		return new FrameBuilder("indicator").add(indicator.isAverage() ? "average" : "sum").
				add("cube", sourceCube != null ? sourceCube.name$() : cube.name$()).
				add("name", indicator.name$()).
				add("label", indicator.label()).
				add("index", new FrameBuilder((sourceCube == null && cube.index() != null) || (sourceCube != null && sourceCube.index() != null) ? "index" : "total")).
				add("source", indicator.source().name$()).
				add("unit", indicator.unit());
	}

	private FrameBuilder columnFrame(Column column, int offset, String cube) {
		if (column.isCategory()) return processCategoryColumn(column.asCategory(), column.name$(), offset, cube);
		else return processColumn(column, offset, cube);
	}

	private FrameBuilder processColumn(Column column, int offset, String cube) {
		SizedData.Type asType = column.asType();
		FrameBuilder builder = new FrameBuilder("column", column.core$().is(Cube.Fact.Attribute.class) ? "attribute" : "measure")
				.add("name", column.a$(Column.class).name$())
				.add("offset", offset)
				.add("cube", cube)
				.add("type", type(asType));
		column.core$().conceptList().stream().filter(Concept::isAspect).map(Predicate::name).forEach(builder::add);
		if (isAligned(asType, offset)) builder.add("aligned", "Aligned");
		else builder.add("bits", asType.size());
		builder.add("size", asType.size());
		return builder;
	}

	private FrameBuilder processCategoryColumn(SizedData.Category column, String name, int offset, String cube) {
		Axis.Categorical axis = column.axis().asCategorical();
		return new FrameBuilder("column", "categorical")
				.add("name", name)
				.add("type", axis.name$())
				.add("offset", offset)
				.add("cube", cube)
				.add("bits", sizeOf(axis));

	}

	private String type(SizedData.Type type) {
		if (type.i$(SizedData.Category.class)) return type.name$();
		return isPrimitive(type) ? type.primitive() : type.type();
	}

	private Integer sizeOf(Column column) {
		return column.isCategory() ? sizeOf(column.asCategory().axis().asCategorical()) : column.asType().size();
	}

	private Integer sizeOf(Axis.Categorical axis) {
		try {
			return (int) Math.ceil(log2(countLines(axis) + 1));
		} catch (IOException e) {
			return 0;
		}
	}

	private int countLines(Axis.Categorical axis) throws IOException {
		return (int) new BufferedReader(new InputStreamReader(resource(axis))).lines().count();
	}

	private FileInputStream resource(Axis.Categorical axis) throws FileNotFoundException {
		return new FileInputStream(this.context.res(Target.Owner).getAbsolutePath() + "/analytic/axes/" + axis.name$() + ".tsv");
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

	private void renderAxis(Axis.Categorical axis) {
		FrameBuilder fb = new FrameBuilder("axis").
				add("package", context.packageName()).add("name", axis.name$()).add("label", axis.label());
		if (axis.asAxis().isDynamic()) fb.add("dynamic", ";");
		if (axis.includeLabel() != null)
			fb.add("include", new FrameBuilder("include").add("name", "label").add("index", 2));
		int offset = offset(axis);
		if (axis.include() != null) {
			List<Axis> includes = axis.include().axes();
			for (int i = 0; i < includes.size(); i++)
				fb.add("include", new FrameBuilder("include").add("name", includes.get(i).name$()).add("index", i + offset));
		}
		writeFrame(new File(gen, "axes"), firstUpperCase(axis.name$()), customize(new CategoricalAxisTemplate()).render(fb.toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(axis), javaFile(new File(gen, "axes"), firstUpperCase(axis.name$())).getAbsolutePath()));
	}

	private int offset(Axis.Categorical axis) {
		return axis.includeLabel() != null ? 3 : 2;
	}


	private void renderAxis(Axis.Continuous axis) {
		FrameBuilder fb = new FrameBuilder("continuous").add("package", context.packageName()).add("name", axis.name$()).add("label", axis.label());
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
		writeFrame(new File(gen, "axes"), firstUpperCase(axis.name$()), customize(new ContinuousAxisTemplate()).render(fb.toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(axis), javaFile(new File(gen, "axes"), firstUpperCase(axis.name$())).getAbsolutePath()));
	}

}

