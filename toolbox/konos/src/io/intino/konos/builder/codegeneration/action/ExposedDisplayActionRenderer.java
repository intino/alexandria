package io.intino.konos.builder.codegeneration.action;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.CodeGenerationHelper;
import io.intino.konos.dsl.Display;
import io.intino.konos.dsl.Service;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.format;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class ExposedDisplayActionRenderer extends ActionRenderer {
	private final Display.Exposed display;
	private final CompilationContext configuration;
	private final Target target;

	public ExposedDisplayActionRenderer(CompilationContext compilationContext, Display.Exposed display, Target target) {
		super(compilationContext, "exposedDisplay");
		this.configuration = compilationContext;
		this.display = display;
		this.target = target;
	}

	@Override
	public void render() {
		FrameBuilder builder = buildFrame();

		configuration.classes().put(display.getClass().getSimpleName() + "#" + firstUpperCase(display.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(display.name$())) + suffix(target));

		if (!alreadyRendered(src(target), display.name$() + "ProxyPage"))
			writeFrame(destinationPackage(src(target)), display.name$() + "ProxyPage", new ActionTemplate().render(builder.toFrame(), Formatters.all));
	}

	@Override
	protected ContextType contextType() {
		return ContextType.Server;
	}

	private Frame[] parameters() {
		List<String> parameters = display.parameters();
		return parameters.stream().map(parameter -> new FrameBuilder("parameter")
				.add("type", "String")
				.add("name", parameter)
				.add("resource", display.name$() + "Proxy").toFrame()).toArray(Frame[]::new);
	}

	@Override
	protected File destinationPackage(File destiny) {
		return new File(destiny, format(CodeGenerationHelper.Pages, target));
	}

	@Override
	protected String suffix(Target target) {
		return "Page";
	}

	private FrameBuilder buildFrame() {
		FrameBuilder builder = new FrameBuilder("action", "ui", "exposedDisplay");
		String type = elementHelper.typeOf(display.a$(Display.class));
		builder.add("name", display.name$());
		builder.add("display", display.name$());
		if (!type.equalsIgnoreCase("display")) builder.add("packageType", type.toLowerCase());
		builder.add("package", packageName());
		builder.add("box", boxName());
		builder.add("parameter", parameters());
		builder.add("contextProperty", contextPropertyFrame());
		Service.UI service = display.graph().serviceList().stream().filter(Service::isUI).map(Service::asUI).findFirst().orElse(null);
		if (service == null) return builder;
		builder.add("uiService", service.name$());
		if (service.googleApiKey() != null) builder.add("googleApiKey", customize("googleApiKey", service.googleApiKey()));
		if (service.title() != null) builder.add("title", titleFrame(service));
		if (service.favicon() != null) builder.add("favicon", service.favicon());
		return builder;
	}

	private FrameBuilder titleFrame(Service.UI service) {
		FrameBuilder result = new FrameBuilder("title");
		String title = service.title();
		if (title.startsWith("{") && title.endsWith("}")) {
			title = title.substring(1, title.length() - 1);
			result.add("configuration");
		}
		result.add("title", title);
		return result;
	}

}
