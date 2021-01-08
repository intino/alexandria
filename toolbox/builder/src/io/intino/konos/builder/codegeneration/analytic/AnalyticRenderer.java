package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.Cube.Fact.Column;
import io.intino.magritte.framework.Concept;
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
		writeFrame(gen, "Factor", customize(template).render(new FrameBuilder("interface").add("package", context.packageName()).toFrame()));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(factorList.get(0)), javaFile(gen, "Factor").getAbsolutePath()));
		for (Factor factor : factorList) {
			FrameBuilder fb = new FrameBuilder("factor").add("package", context.packageName()).add("name", factor.name$()).add("label", factor.label());
			factor.factorList().forEach(child -> fb.add("factor", new FrameBuilder("factor").add("name", child.name$())));
			if (factor.datasource() != null) fb.add("resourceId", factor.datasource().resourceId());
			writeFrame(new File(src, "factors"), factor.name$(), customize(template).render(fb.toFrame()));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(factor), javaFile(new File(src, "factors"), firstUpperCase(factor.name$())).getAbsolutePath()));
		}
	}

	private void renderCubes(List<Cube> cubeList) {
		CubeTemplate template = new CubeTemplate();
		for (Cube cube : cubeList) {
			FrameBuilder fb = new FrameBuilder("cube").add("package", context.packageName()).add("name", cube.name$());
			cube.dimensionList().forEach(selector -> fb.add("dimension", dimensionFrame(cube, selector)));
			cube.indicatorList().forEach(indicator -> fb.add("indicator", inicatorFrame(cube, indicator)));
			int offset = 0;
			for (Column column : cube.fact().columnList()) {
				fb.add("column", columnFrame(column, offset));
				offset += column.asType().size();
			}
			fb.add("size", offset);
			writeFrame(new File(gen, "cubes"), "Abstract" + firstUpperCase(cube.name$()), customize(template).render(fb.toFrame()));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(cube), javaFile(new File(gen, "cubes"), "Abstract" + firstUpperCase(cube.name$())).getAbsolutePath()));
			writeFrame(new File(src, "cubes"), cube.name$(), customize(template).render(fb.add("src").toFrame()));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(cube), javaFile(new File(src, "cubes"), firstUpperCase(cube.name$())).getAbsolutePath()));
		}
	}

	private FrameBuilder dimensionFrame(Cube cube, Cube.Dimension dimension) {
		return new FrameBuilder("dimension", dimension.axis().core$().is(Factor.class) ? "factor" : "distribution").add("cube", cube.name$()).add("axis", dimension.axis().name$());
	}

	private FrameBuilder inicatorFrame(Cube cube, Cube.Indicator indicator) {
		return new FrameBuilder("indicator").
				add("cube", cube.name$()).
				add("name", indicator.name$()).
				add("label", indicator.label()).
				add("unit", indicator.unit());
	}

	private FrameBuilder columnFrame(Column column, int offset) {
		if (column.isCategory()) return processColumn(column.asCategory().factor(), column.name$(), offset);
		else return processColumn(column, offset);
	}

	private FrameBuilder processColumn(Column column, int offset) {
		SizedData.Type asType = column.asType();
		FrameBuilder builder = new FrameBuilder("column", column.core$().is(Cube.Fact.Attribute.class) ? "attribute" : "measure")
				.add("name", column.a$(Column.class).name$())
				.add("offset", offset)
				.add("type", isPrimitive(asType) ? asType.primitive() : asType.type());
		column.core$().conceptList().stream().filter(Concept::isAspect).map(Predicate::name).forEach(builder::add);
		if (isAligned(asType, offset)) builder.add("aligned", "Aligned");
		else builder.add("bits", asType.size());
		builder.add("size", asType.size());
		return builder;
	}

	private FrameBuilder processColumn(Factor factor, String name, int offset) {
		FrameBuilder builder = new FrameBuilder("attribute", "lookup").
				add("name", name).
				add("type", factor.name$()).
				add("offset", offset).
				add("bits", sizeOf(factor));
		if (!factor.datasource().resourceId().isEmpty()) builder.add("resource");
		return builder;
	}

	private Object sizeOf(Factor factor) {
		return null;
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
			FrameBuilder fb = new FrameBuilder("cube").add("package", context.packageName());
			for (Distribution.Range range : d.rangeList())
				if (range.isLowerBound()) fb.add("lower").add("bound", range.asLowerBound().lowerBound());
				else if (range.isUpperBound()) fb.add("upper").add("bound", range.asUpperBound().upperBound());
				else if (range.isBound())
					fb.add("lower").add("lower", range.asBound().lowerBound()).add("upper", range.asBound().upperBound());
			writeFrame(new File(src, "factors"), d.name$(), customize(template).render(fb.toFrame()));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(d), javaFile(new File(gen, "distributions"), firstUpperCase(d.name$())).getAbsolutePath()));
		}
	}
}