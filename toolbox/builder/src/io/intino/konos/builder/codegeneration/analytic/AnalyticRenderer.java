package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
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
		renderFactors(graph.core$().find(Factor.class));
		renderCubes(graph.cubeList());
		renderDistributions(graph.distributionList());
	}

	private void renderFactors(List<Factor> factorList) {
		if (factorList.isEmpty()) return;
		FactorTemplate template = new FactorTemplate();
		writeFrame(gen, "Axis", customize(template).render(new FrameBuilder("interface").add("package", context.packageName()).toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(factorList.get(0)), javaFile(gen, "Factor").getAbsolutePath()));
		for (Factor factor : factorList) {
			FrameBuilder fb = new FrameBuilder("factor").
					add("package", context.packageName()).add("name", factor.name$()).add("label", factor.label());
			if (factor.isDynamic()) fb.add("dynamic", ";");
			for (int i = 0; i < factor.factorList().size(); i++)
				fb.add("factor", new FrameBuilder("factor").add("name", factor.factorList().get(i).name$()).add("index", i + 3));
			if (factor.datasource() != null) fb.add("resourceId", factor.datasource().resourceId());
			if (!alreadyRendered(new File(src, "factors"), factor.name$())) {
				writeFrame(new File(src, "factors"), factor.name$(), customize(template).render(fb.toFrame()));
				context.compiledFiles().add(new OutputItem(context.sourceFileOf(factor), javaFile(new File(src, "factors"), firstUpperCase(factor.name$())).getAbsolutePath()));
			}
			writeFrame(new File(gen, "factors"), "Abstract" + firstUpperCase(factor.name$()), customize(template).render(fb.add("abstract").toFrame()));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(factor), javaFile(new File(gen, "factors"), "Abstract" + firstUpperCase(factor.name$())).getAbsolutePath()));
		}
	}

	private void renderCubes(List<Cube> cubeList) {

		for (Cube cube : cubeList) {
			FrameBuilder fb = new FrameBuilder("cube").add("package", context.packageName()).add("name", cube.name$());
			if (cube.isJoin()) {
				addDimensionsAndIndicators(cube.asJoin().cube(), cube, fb);
				fb.add("join", cube.asJoin().cube().name$());
				if (cube.asJoin().cube().split() != null) addSplit(cube.asJoin().cube(), fb);
			}
			addDimensionsAndIndicators(cube, null, fb);
			fb.add("id", cube.fact().columnList().stream().filter(SizedData::isId).map(Layer::name$).findFirst().orElse(null));
			if (cube.split() != null) addSplit(cube, fb);
			int offset = 0;
			for (Column column : cube.fact().columnList()) {
				fb.add("column", columnFrame(column, offset));
				offset += column.asType().size();
			}
			fb.add("size", offset);
			writeFrame(new File(gen, "cubes"), "Abstract" + firstUpperCase(cube.name$()), customize(template(cube)).render(fb.toFrame()));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(cube), javaFile(new File(gen, "cubes"), "Abstract" + firstUpperCase(cube.name$())).getAbsolutePath()));
//			if (!alreadyRendered(new File(src, "cubes"), cube.name$()))
			writeFrame(new File(src, "cubes"), cube.name$(), customize(template(cube)).render(fb.add("src").toFrame()));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(cube), javaFile(new File(src, "cubes"), firstUpperCase(cube.name$())).getAbsolutePath()));
		}
	}

	private void addSplit(Cube cube, FrameBuilder fb) {
		fb.add("split", new FrameBuilder("split").add("name", cube.split().name$()).add("value", cube.split().splits().toArray(new String[0])));
	}

	private Template template(Cube cube) {
		return cube.isJoin() ? new JoinCubeTemplate() : new CubeTemplate();
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
		FrameBuilder fb = new FrameBuilder("dimension", dimension.axis().core$().is(Factor.class) ? "factor" : "distribution").
				add("cube", sourceCube != null ? sourceCube.name$() : cube.name$()).
				add("name", dimension.name$()).
				add("source", dimension.source().name$()).
				add("axis", dimension.axis().name$());
		if (dimension.axis().i$(Factor.class)) {
			fb.add("type", dimension.axis().name$());
			if (!dimension.source().asCategory().factor().equals(dimension.axis()))
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
		Factor factor = category.factor();
		FrameBuilder builder = new FrameBuilder("column", "factor").
				add("name", name).
				add("type", factor.name$()).
				add("offset", offset).
				add("bits", category.size());
		if (!factor.datasource().resourceId().isEmpty()) builder.add("resource");
		return builder;
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

	private void renderDistributions(List<Distribution> distributions) {
		DistributionTemplate template = new DistributionTemplate();
		for (Distribution d : distributions) {
			FrameBuilder fb = new FrameBuilder("distribution").add("package", context.packageName()).add("name", d.name$()).add("label", d.label());
			fb.add("rangeSize", d.rangeList().size());
			int index = 0;
			for (Distribution.Range range : d.rangeList()) {
				FrameBuilder rangeFb = new FrameBuilder("range").add("index", index);
				if (range.isLowerBound()) rangeFb.add("lower").add("bound", range.asLowerBound().lowerBound());
				else if (range.isUpperBound()) rangeFb.add("upper").add("bound", range.asUpperBound().upperBound());
				else if (range.isBound())
					rangeFb.add("lower", range.asBound().lowerBound()).add("upper", range.asBound().upperBound());
				fb.add("range", rangeFb);
				index++;
			}
			writeFrame(new File(gen, "distributions"), firstUpperCase(d.name$()), customize(template).render(fb.toFrame()));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(d), javaFile(new File(gen, "distributions"), firstUpperCase(d.name$())).getAbsolutePath()));
		}
	}

	private boolean alreadyRendered(File destination, String name) {
		return Commons.javaFile(destination, name).exists();
	}
}