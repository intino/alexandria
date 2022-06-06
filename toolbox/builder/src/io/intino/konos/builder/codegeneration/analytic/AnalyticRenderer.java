package io.intino.konos.builder.codegeneration.analytic;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.facts.FactRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.Axis;
import io.intino.konos.model.Cube;
import io.intino.konos.model.KonosGraph;

import java.io.File;
import java.util.List;

public class AnalyticRenderer extends Renderer {

	private final File src;
	private final File gen;
	private final KonosGraph graph;
	private final FactRenderer factRenderer;
	private final CategoricalAxisRenderer categoricalAxisRenderer;
	private final ContinuousAxisRenderer continuousAxisRenderer;
	private final CubeRenderer cubeRenderer;

	public AnalyticRenderer(CompilationContext context, KonosGraph graph) {
		super(context, Target.Owner);
		this.src = new File(context.src(Target.Owner), "analytic");
		this.gen = new File(context.gen(Target.Owner), "analytic");
		final File res = context.res(Target.Owner).getAbsoluteFile();
		this.graph = graph;
		this.factRenderer = new FactRenderer();
		this.categoricalAxisRenderer = new CategoricalAxisRenderer(context, gen, res);
		this.continuousAxisRenderer = new ContinuousAxisRenderer(context, gen);
		this.cubeRenderer = new CubeRenderer(src, gen, context, factRenderer);
	}

	@Override
	protected void render() {
		renderAxes(graph.axisList());
		renderCubes(graph.cubeList());
	}

	private void renderAxes(List<Axis> axes) {
		if (axes.isEmpty()) return;
		axes.stream().filter(Axis::isCategorical).map(Axis::asCategorical).forEach(categoricalAxisRenderer::render);
		axes.stream().filter(Axis::isContinuous).map(Axis::asContinuous).forEach(continuousAxisRenderer::render);
		AxisInterfaceRenderer.render(gen, context, axes);
	}

	private void renderCubes(List<Cube> cubeList) {
		for (Cube cube : cubeList) {
			FrameBuilder fb = new FrameBuilder("cube").add("package", context.packageName()).add("name", cube.name$());
			cubeRenderer.render(cube, fb);
		}
	}


}

