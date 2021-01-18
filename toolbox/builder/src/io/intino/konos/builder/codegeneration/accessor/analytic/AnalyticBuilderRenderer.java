package io.intino.konos.builder.codegeneration.accessor.analytic;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Cube;
import io.intino.konos.model.graph.Cube.Fact.Column;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.SizedData;
import io.intino.magritte.framework.Concept;
import io.intino.magritte.framework.Predicate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnalyticBuilderRenderer extends Renderer {
	private final KonosGraph graph;
	private final File destination;
	private final String packageName;

	public AnalyticBuilderRenderer(CompilationContext compilationContext, KonosGraph graph, File destination) {
		super(compilationContext, Target.Owner);
		this.graph = graph;
		this.destination = destination;
		this.destination.mkdirs();
		this.packageName = compilationContext.packageName() + ".box";
	}

	@Override
	public void render() {
		createCube(graph);
		createBuilder(graph);
	}

	private void createCube(KonosGraph graph) {
		for (Cube cube : graph.cubeList()) {
			FrameBuilder builder = new FrameBuilder("cube").add("package", packageName + ".schemas").add("name", cube.name$());
			builder.add("cube", cube(cube));
			Commons.writeFrame(destinationPackage(), cube.name$(), builderTemplate().render(builder.toFrame()));
		}
	}

	private void createBuilder(KonosGraph graph) {
		FrameBuilder builder = new FrameBuilder("builder").add("package", packageName).add("name", context.boxName() + "AnalyticBuilder");
		for (Cube cube : graph.cubeList()) builder.add("cube", cube(cube));
		Commons.writeFrame(destinationPackage(), context.boxName() + "AnalyticBuilder", builderTemplate().render(builder.toFrame()));
	}

	private FrameBuilder cube(Cube cube) {
		FrameBuilder fb = new FrameBuilder("cube");
		List<FrameBuilder> list = columns(cube.fact());
		return fb.add("column", list.toArray(new FrameBuilder[0]));
	}

	private List<FrameBuilder> columns(Cube.Fact fact) {
		int offset = 0;
		List<FrameBuilder> list = new ArrayList<>();
		List<Column> columns = fact.columnList();
		columns.sort(Comparator.comparingInt(a -> a.asType().size()));
		Collections.reverse(columns);
		for (Column c : columns) {
			FrameBuilder b = process(c, offset);
			if (b != null) {
				offset += c.asType().size();
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

	private FrameBuilder processCategoryAttribute(SizedData.Category category, String name, int offset) {
		return new FrameBuilder("column", "factor", "resource").
				add("name", name).
				add("type", category.axis().name$()).
				add("offset", offset).
				add("bits", category.size());
	}

	public static double log2(int N) {
		return (Math.log(N) / Math.log(2));
	}


	private File destinationPackage() {
		return new File(destination, "jmx");
	}

	private Template builderTemplate() {
		return Formatters.customize(new BuilderTemplate());
	}
}
