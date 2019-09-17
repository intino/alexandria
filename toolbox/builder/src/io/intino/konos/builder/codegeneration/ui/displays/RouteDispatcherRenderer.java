package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.services.ui.templates.RouteDispatcherTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.ui.AbstractUIService;
import io.intino.konos.model.graph.ui.UIService;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.konos.builder.helpers.CodeGenerationHelper.displaysFolder;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.fileOf;
import static java.util.stream.Collectors.toList;

public class RouteDispatcherRenderer extends UIRenderer {
	private final UIService service;

	public RouteDispatcherRenderer(Settings settings, UIService service, Target target) {
		super(settings, target);
		this.service = service;
	}

	@Override
	protected void render() {
		FrameBuilder builder = buildFrame();
		File routeDispatcher = fileOf(displaysFolder(src(), target), "RouteDispatcher", target);
		if (!routeDispatcher.exists()) Commons.write(routeDispatcher.toPath(), setup(new RouteDispatcherTemplate()).render(builder.toFrame()));
		Commons.write(fileOf(displaysFolder(gen(), target), "AbstractRouteDispatcher", target).toPath(), setup(new RouteDispatcherTemplate()).render(builder.add("gen").toFrame()));
	}

	@Override
	public FrameBuilder buildFrame() {
		FrameBuilder builder = super.buildFrame();
		builder.add("dispatcher");
		service.resourceList().forEach(r -> {
			if (r.isMain()) builder.add("resource", resourceFrame(r).add("main"));
			builder.add("resource", resourceFrame(r));
		});
		return builder;
	}

	private FrameBuilder resourceFrame(AbstractUIService.Resource resource) {
		FrameBuilder result = new FrameBuilder("resource");
		result.add("name", resource.name$());
		result.add("subPath", subPath(resource));
		result.add("paramsCount", paramsOf(resource).size());
		addResourceParams(resource, result);
		return result;
	}

	private void addResourceParams(AbstractUIService.Resource resource, FrameBuilder result) {
		List<String> params = paramsOf(resource);
		for (int i=0; i<params.size(); i++) result.add("param", new FrameBuilder().add("param").add("name", params.get(i)).add("index", i));
	}

	private String subPath(AbstractUIService.Resource resource) {
		Stream<String> split = Stream.of(resource.path().split("/"));
		return split.filter(s -> !s.startsWith(":")).collect(Collectors.joining("/"));
	}

	private List<String> paramsOf(AbstractUIService.Resource resource) {
		Stream<String> split = Stream.of(resource.path().split("/"));
		return split.filter(s -> s.startsWith(":")).map(s -> s.substring(1)).collect(toList());
	}
}