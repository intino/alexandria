package io.intino.konos.builder.codegeneration.accessor.ui;

import cottons.utils.StringHelper;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.AppTemplate;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.PassiveViewTemplate;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.WebPackTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.createIfNotExists;
import static java.util.stream.Collectors.toList;

public class AppRenderer extends UIRenderer {
	private final Service.UI service;

	protected AppRenderer(CompilationContext compilationContext, Service.UI service) {
		super(compilationContext, Target.Accessor);
		this.service = service;
	}

	@Override
	public void render() {
		writeApp();
		writePassiveView();
		writeWebPack();
	}

	private void writeApp() {
		createIfNotExists(new File(gen() + File.separator + "apps"));
		resourcesWithTemplate().forEach(r -> {
			FrameBuilder builder = new FrameBuilder("app");
			builder.add("page", new FrameBuilder("page").add("templateName", r.asPage().template().name$()).toFrame());
			builder.add("webModuleName", context.webModuleDirectory() != null ? context.webModuleDirectory().getName() : "");
			addPatterns(r.asPage().template(), builder);
			Commons.write(new File(gen() + File.separator + "apps" + File.separator + firstUpperCase(r.asPage().template().name$()) + ".js").toPath(), setup(new AppTemplate()).render(builder.toFrame()));
		});
	}

	private void addPatterns(io.intino.konos.model.Template template, FrameBuilder builder) {
		List<String> patterns = service.resourceList().stream().filter(r -> r.isPage() && r.asPage().template() == template).map(this::patternOf).filter(p -> !p.isEmpty()).collect(toList());
		patterns.stream().sorted((o1, o2) -> Integer.compare(o2.length(), o1.length())).forEach(p -> builder.add("pattern", new FrameBuilder("pattern").add("value", p)));
	}

	private void writePassiveView() {
		Template template = new PassiveViewTemplate();
		File notifiersFile = createIfNotExists(new File(gen() + "/displays/notifiers"));
		File requestersFile = createIfNotExists(new File(gen() + "/displays/requesters"));
		Commons.write(new File(notifiersFile + File.separator + "Notifier.js").toPath(), template.render(new FrameBuilder("notifier")));
		Commons.write(new File(requestersFile + File.separator + "Requester.js").toPath(), template.render(new FrameBuilder("requester")));
	}

	private void writeWebPack() {
		Template template = new WebPackTemplate();
		FrameBuilder builder = new FrameBuilder("webpack");
		builder.add("outDirectory", normalize(context.configuration().outDirectory().getParentFile().getAbsolutePath()));
		builder.add("exclude", alexandriaFrame("exclude"));
		builder.add("alias", alexandriaFrame("alias"));
		builder.add("webModuleName", context.webModuleDirectory() != null ? context.webModuleDirectory().getName() : "");
		if (isAlexandria(project())) builder.add("alexandriaProject", "true");
		resourcesWithTemplate().forEach(r -> builder.add("page", new FrameBuilder("page").add("templateName", r.asPage().template().name$()).toFrame()));
		Commons.write(new File(root(), "webpack.config.js").toPath(), setup(template).render(builder.toFrame()));
	}

	private Object normalize(String path) {
		return path != null ? path.replaceAll("\\\\", "/").replaceAll("^.:", "") : path;
	}

	private FrameBuilder alexandriaFrame(String name) {
		FrameBuilder result = new FrameBuilder(name);
		if (isAlexandria(project())) result.add("alexandriaProject");
		result.add("use", "alexandria-ui-elements");
		service.useList().forEach(use -> result.add("use", StringHelper.camelCaseToSnakeCase(use.service())));
		return result;
	}

	public static boolean isAlexandria(String project) {
		return project != null && project.equalsIgnoreCase("alexandria");
	}

	public List<Service.UI.Resource> resourcesWithTemplate() {
		Map<io.intino.konos.model.Template, List<Service.UI.Resource>> result = service.resourceList().stream().filter(Service.UI.Resource::isPage).collect(Collectors.groupingBy(r -> r.asPage().template()));
		return result.values().stream().map(r -> r.get(0)).collect(toList());
	}
}
