package io.intino.konos.builder.codegeneration.ui.displays;

import io.intino.itrules.Engine;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.template.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.services.ui.templates.RouteDispatcherTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.dsl.Service;
import io.intino.magritte.framework.Layer;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.intino.konos.builder.helpers.CodeGenerationHelper.*;
import static java.util.stream.Collectors.toList;

public class RouteDispatcherRenderer extends UIRenderer {
	private final List<Service.UI> serviceList;
	private final Target target;

	public RouteDispatcherRenderer(CompilationContext compilationContext, List<Service.UI> serviceList, Target target) {
		super(compilationContext);
		this.serviceList = serviceList;
		this.target = target;
	}

	public RouteDispatcherRenderer(CompilationContext compilationContext, Service.UI service, Target target) {
		super(compilationContext);
		this.serviceList = List.of(service);
		this.target = target;
	}

	@Override
	protected void render() {
		FrameBuilder builder = buildFrame();
		createIfNotExists(displaysFolder(src(target), target));
		createIfNotExists(displaysFolder(gen(target), target));
		File routeDispatcher = fileOf(displaysFolder(src(target), target), "RouteDispatcher", target);
		if (target != Target.Android && !routeDispatcher.exists()) {
			Commons.write(routeDispatcher.toPath(), new Engine(template()).addAll(Formatters.all).render(builder.toFrame()));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(serviceList.get(0)), routeDispatcher.getAbsolutePath()));
		}
		Commons.write(fileOf(displaysFolder(gen(target), target), target != Target.Android ? "AbstractRouteDispatcher" : "RouteDispatcher", target).toPath(), new Engine(template()).addAll(Formatters.all).render(builder.add("gen").toFrame()));
		if (!target.equals(Target.Service)) return;
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(serviceList.get(0)), fileOf(displaysFolder(gen(target), target), "AbstractRouteDispatcher", target).getAbsolutePath()));
	}

	@Override
	public FrameBuilder buildFrame() {
		FrameBuilder builder = super.buildFrame();
		builder.add("dispatcher");
		resources().stream().filter(Service.UI.Resource::isPage).forEach(r -> {
			if (r.isMain()) builder.add("resource", resourceFrame(r).add("main"));
			builder.add("resource", resourceFrame(r));
		});
		return builder;
	}

	private List<Service.UI.Resource> resources() {
		return serviceList.stream().map(Service.UI::resourceList).flatMap(Collection::stream).collect(toList());
	}

	private FrameBuilder resourceFrame(Service.UI.Resource resource) {
		FrameBuilder result = buildBaseFrame().add("resource");
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

	private Template template() {
		if (target == Target.Android)
			return new io.intino.konos.builder.codegeneration.accessor.ui.android.templates.RouteDispatcherTemplate();
		return new RouteDispatcherTemplate();
	}

}