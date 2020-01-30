package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.AppTemplate;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.PassiveViewTemplate;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.WebPackTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Service;

import java.io.File;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.createIfNotExists;

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
		service.resourceList().forEach(r -> {
			FrameBuilder builder = new FrameBuilder("app");
			builder.add("page", new FrameBuilder("page").add("value", r.name$()).toFrame());
			builder.add("webModuleName", compilationContext.webModuleDirectory() != null ? compilationContext.webModuleDirectory().getName() : "");
			if (!patternOf(r).isEmpty()) builder.add("pattern", patternOf(r));
			Commons.write(new File(gen() + File.separator + "apps" + File.separator + firstUpperCase(r.name$()) + ".js").toPath(), setup(new AppTemplate()).render(builder.toFrame()));
		});
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
		builder.add("outDirectory", compilationContext.configuration().outDirectory());
		builder.add("exclude", alexandriaFrame("exclude"));
		builder.add("alias", alexandriaFrame("alias"));
		builder.add("webModuleName", compilationContext.webModuleDirectory() != null ? compilationContext.webModuleDirectory().getName() : "");
		if (isAlexandria(project())) builder.add("alexandriaProject", "true");
		service.resourceList().forEach(r -> builder.add("page", new FrameBuilder("page").add("value", r.name$()).toFrame()));
		Commons.write(new File(root() + File.separator + "webpack.config.js").toPath(), setup(template).render(builder.toFrame()));
	}

	private Object normalize(String parent) {
		return parent != null ? parent.replaceAll("\\\\", "/").replaceAll("^.:", "") : parent;
	}

	private FrameBuilder alexandriaFrame(String name) {
		FrameBuilder result = new FrameBuilder(name);
		if (isAlexandria(project())) result.add("alexandriaProject");
		return result;
	}

	public static boolean isAlexandria(String project) {
		return project != null && project.equalsIgnoreCase("alexandria");
	}
}
