package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.services.ui.templates.RouteDispatcherTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Service;
import io.intino.magritte.framework.Layer;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.konos.builder.helpers.CodeGenerationHelper.*;
import static java.util.stream.Collectors.toList;

public class RouteDispatcherRenderer extends UIRenderer {
	private final Service.UI service;

	public RouteDispatcherRenderer(CompilationContext compilationContext, Service.UI service, Target target) {
		super(compilationContext, target);
		this.service = service;
	}

	@Override
	protected void render() {
		FrameBuilder builder = buildFrame();
		createIfNotExists(displaysFolder(src(), target));
		createIfNotExists(displaysFolder(gen(), target));
		File routeDispatcher = fileOf(displaysFolder(src(), target), "RouteDispatcher", target);
		if (!routeDispatcher.exists())
			Commons.write(routeDispatcher.toPath(), setup(new RouteDispatcherTemplate()).render(builder.toFrame()));
		Commons.write(fileOf(displaysFolder(gen(), target), "AbstractRouteDispatcher", target).toPath(), setup(new RouteDispatcherTemplate()).render(builder.add("gen").toFrame()));
		if (target.equals(Target.Owner))
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(service), fileOf(displaysFolder(gen(), target), "AbstractRouteDispatcher", target).getAbsolutePath()));
	}

	@Override
	public FrameBuilder buildFrame() {
		FrameBuilder builder = super.buildFrame();
		builder.add("dispatcher");
		service.resourceList().stream().filter(Service.UI.Resource::isPage).forEach(r -> {
			if (r.isMain()) builder.add("resource", resourceFrame(r).add("main"));
			builder.add("resource", resourceFrame(r));
		});
		return builder;
	}

	private FrameBuilder resourceFrame(Service.UI.Resource resource) {
		FrameBuilder result = new FrameBuilder("resource");
		result.add("name", resource.name$());
		result.add("pattern", patternOf(resource));
		addResourceParams(resource, result);
		return result;
	}

	private void addResourceParams(Service.UI.Resource resource, FrameBuilder result) {
		List<String> params = pathParams(resource);
		int index = 0;
		for (String name : params) {
			result.add("param", frameOf(index, name, false));
			index++;
		}
		params = queryParams(resource);
		for (String name : params) {
			result.add("param", frameOf(index, name, true));
			index++;
		}
	}

	private Object frameOf(int index, String name, boolean optional) {
		FrameBuilder result = new FrameBuilder().add("param").add("name", name).add("index", index);
		if (optional) result.add("optional");
		return result;
	}

	private List<String> pathParams(Service.UI.Resource resource) {
		Stream<String> split = Stream.of(resource.path().split("/"));
		return split.filter(s -> s.startsWith(":")).map(s -> s.substring(1)).collect(toList());
	}

	private List<String> queryParams(Service.UI.Resource resource) {
		return resource.parameterList().stream().map(Layer::name$).collect(Collectors.toList());
	}

}