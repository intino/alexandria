package io.intino.konos.builder.codegeneration.action;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.CodeGenerationHelper;
import io.intino.konos.dsl.Display;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.format;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class AccessibleDisplayActionRenderer extends ActionRenderer {
	private final Display.Accessible display;
	private final CompilationContext configuration;
	private final Target target;

	public AccessibleDisplayActionRenderer(CompilationContext compilationContext, Display.Accessible display, Target target) {
		super(compilationContext, "accessibleDisplay");
		this.configuration = compilationContext;
		this.display = display;
		this.target = target;
	}

	@Override
	public void render() {
		FrameBuilder builder = new FrameBuilder("action", "ui", "accessibleDisplay");
		String type = elementHelper.typeOf(display.a$(Display.class));
		builder.add("name", display.name$());
		builder.add("display", display.name$());
		if (!type.equalsIgnoreCase("display")) builder.add("packageType", type.toLowerCase());
		builder.add("package", packageName());
		builder.add("box", boxName());
		builder.add("parameter", parameters());
		builder.add("contextProperty", contextPropertyFrame());
		configuration.classes().put(display.getClass().getSimpleName() + "#" + firstUpperCase(display.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(display.name$())) + suffix(target));
		if (!alreadyRendered(src(target), display.name$() + "ProxyPage"))
			writeFrame(destinationPackage(src(target)), display.name$() + "ProxyPage", new ActionTemplate().render(builder.toFrame(), Formatters.all));
	}

	@Override
	protected ContextType contextType() {
		return ContextType.Spark;
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
}
