package io.intino.konos.builder.codegeneration.accessor.analytic;

import io.intino.alexandria.logger.Logger;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.Renderer;
import io.intino.konos.builder.codegeneration.analytic.AxisInterfaceRenderer;
import io.intino.konos.builder.codegeneration.analytic.CategoricalAxisRenderer;
import io.intino.konos.builder.codegeneration.analytic.ContinuousAxisRenderer;
import io.intino.konos.builder.codegeneration.facts.FactRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.Axis;
import io.intino.konos.dsl.Cube;
import io.intino.konos.dsl.KonosGraph;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class AnalyticBuilderRenderer extends Renderer {

	private final KonosGraph graph;
	private final File srcDestination;
	private final File resDestination;
	private final String packageName;
	private final FactRenderer factRenderer;
	private final CategoricalAxisRenderer categoricalAxisRenderer;
	private final ContinuousAxisRenderer continuousAxisRenderer;

	public AnalyticBuilderRenderer(CompilationContext compilationContext, KonosGraph graph, File srcDestination, File resDestination) {
		super(compilationContext);
		this.graph = graph;
		this.srcDestination = srcDestination;
		this.resDestination = resDestination;
		this.srcDestination.mkdirs();
		this.packageName = compilationContext.packageName();
		this.factRenderer = new FactRenderer();
		final File res = context.res(Target.Server).getAbsoluteFile();
		this.categoricalAxisRenderer = new CategoricalAxisRenderer(context, srcDestination, res);
		this.continuousAxisRenderer = new ContinuousAxisRenderer(context, srcDestination);
	}

	@Override
	public void render() {
		renderAxes(graph.axisList());
		renderCubes(graph);
		renderBuilder(graph);
		copyResourcesToDestination();
	}

	private void copyResourcesToDestination() {
		final File resources = new File(context.res(Target.Server).getAbsoluteFile(), "analytic");
		try {
			FileUtils.copyDirectory(resources, new File(resDestination, resources.getName()));
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void renderAxes(List<Axis> axes) {
		if (axes.isEmpty()) return;
		axes.stream().filter(Axis::isCategorical).map(Axis::asCategorical).forEach(categoricalAxisRenderer::render);
		axes.stream().filter(Axis::isContinuous).map(Axis::asContinuous).forEach(continuousAxisRenderer::render);
		AxisInterfaceRenderer.render(srcDestination, context, axes);
	}

	private void renderCubes(KonosGraph graph) {
		graph.cubeList().stream().filter(c -> !c.isVirtual()).forEach(cube -> {
			writeFrame(new File(destinationDirectory(), "cubes"), cube.name$() + "Schema",
					cubeTemplate().render(renderCube(cube).toFrame()));
		});
	}

	private FrameBuilder renderCube(Cube cube) {
		final String className = StringUtils.capitalize(cube.name$()) + "Schema";
		FrameBuilder fb = new FrameBuilder("cube")
				.add("package", packageName)
				.add("cube", StringUtils.capitalize(cube.name$()))
				.add("name", className);
		factRenderer.render(className, cube, fb);
		return fb;
	}

	private void renderBuilder(KonosGraph graph) {
		FrameBuilder builder = new FrameBuilder("builder").add("package", packageName).add("name",
				context.boxName() + "AnalyticBuilder");
		graph.cubeList().stream().filter(c -> !c.isVirtual()).forEach(cube -> builder.add("cube",
				new FrameBuilder("cube").add("name", cube.name$())));
		writeFrame(destinationDirectory(), context.boxName() + "AnalyticBuilder", builderTemplate().render(builder.toFrame()));
	}

	private File destinationDirectory() {
		return new File(srcDestination, packageName.replace(".", "/") + "/analytic");
	}

	private Template builderTemplate() {
		return Formatters.customize(new AnalyticBuilderTemplate());
	}

	private Template cubeTemplate() {
		return Formatters.customize(new CubeWithColumnsTemplate());
	}
}
