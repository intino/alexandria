package io.intino.konos.builder.codegeneration.accessor.analytic;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.analytic.CategoricalAxisTemplate;
import io.intino.konos.builder.codegeneration.analytic.ContinuousAxisTemplate;
import io.intino.konos.builder.codegeneration.analytic.FactRenderer;
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
		writeFrame(destinationDirectory(), "Axis", customize(new CategoricalAxisTemplate()).render(new FrameBuilder("interface").add("package", context.packageName()).toFrame()));
		for (Axis axis : axes)
			if (axis.isCategorical()) renderAxis(axis.asCategorical());
			else renderAxis(axis.asContinuous());
	}

	private void renderCubes(KonosGraph graph) {
		graph.cubeList().stream().filter(c -> !c.isVirtual()).forEach(cube -> {
			writeFrame(new File(destinationDirectory(), "cubes"), cube.name$(), cubeTemplate().render(renderCube(cube).toFrame()));
		});
	}


	private FrameBuilder renderCube(Cube cube) {
		FrameBuilder fb = new FrameBuilder("cube").add("package", packageName).add("name", cube.name$());
		factRenderer.addFact(cube, fb);
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
		writeFrame(new File(destinationDirectory(), "axes"), firstUpperCase(snakeCaseToCamelCase().format(axis.name$()).toString()), customize(new CategoricalAxisTemplate()).render(fb.toFrame()));
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
		writeFrame(new File(destinationDirectory(), "axes"), firstUpperCase(snakeCaseToCamelCase().format(axis.name$()).toString()), customize(new ContinuousAxisTemplate()).render(fb.toFrame()));
	}


	private List<FrameBuilder> columns(Cube.Fact fact) {
		int offset = 0;
		List<FrameBuilder> list = new ArrayList<>();
		List<Column> columns = new ArrayList<>(fact.columnList());
		columns.sort(Comparator.comparingInt(a -> a.asType().size()));
		Collections.reverse(columns);
		for (Column c : columns) {
			FrameBuilder b = process(c, offset);
			if (b != null) {
				offset += sizeOf(c);
				list.add(b.add("owner", fact.core$().owner().name()));
			}
		}
		return list;
	}

	private FrameBuilder process(Column column, int offset) {
		if (column.isCategory())
			return processCategoryAttribute(column.asCategory(), column.name$(), offset);
		else return processAttribute(column, offset);
	}

	private FrameBuilder processAttribute(Column column, int offset) {
		SizedData.Type type = column.asType();
		FrameBuilder builder = new FrameBuilder("column")
				.add("name", column.a$(Column.class).name$())
				.add("offset", offset)
				.add("type", isPrimitive(type) ? type.primitive() : type.type());
		column.core$().conceptList().stream().filter(Concept::isAspect).map(Predicate::name).forEach(builder::add);
		if (isAligned(type, offset)) builder.add("aligned", "Aligned");
		else builder.add("bits", type.size());
		builder.add("size", type.size());
		return builder;
	}

	private boolean isAligned(SizedData.Type column, int offset) {
		return (offset == 0 || log2(offset) % 1 == 0) && column.maxSize() == column.size();
	}

	private boolean isPrimitive(SizedData.Type column) {
		SizedData data = column.asSizedData();
		return data.isBool() || data.isInteger() || data.isLongInteger() || data.isReal();
	}

	private FrameBuilder processCategoryAttribute(SizedData.Category column, String name, int offset) {
		return new FrameBuilder("column", "categorical").
				add("name", name).
				add("type", column.axis().name$()).
				add("offset", offset).
				add("bits", sizeOf(column.a$(Column.class)));
	}

	public static double log2(int N) {
		return (Math.log(N) / Math.log(2));
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

	private File destinationDirectory() {
		return new File(destination, packageName.replace(".", "/") + "/analytic");
	}

	private Template builderTemplate() {
		return Formatters.customize(new BuilderTemplate());
	}

	private Template cubeTemplate() {
		return Formatters.customize(new CubeTemplate());
	}
}
