package io.intino.konos.builder.codegeneration.action;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.helpers.CodeGenerationHelper;
import io.intino.konos.model.graph.Display;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.format;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class AccessibleDisplayActionRenderer extends ActionRenderer {
	private final Display.Accessible display;
	private final Settings configuration;

	public AccessibleDisplayActionRenderer(Settings settings, Display.Accessible display) {
		super(settings,"accessibleDisplay");
		this.configuration = settings;
		this.display = display;
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
		configuration.classes().put(display.getClass().getSimpleName() + "#" + firstUpperCase(display.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(display.name$())) + suffix());
		if (!alreadyRendered(src(), display.name$() + "Proxy"))
			writeFrame(destinyPackage(src()), display.name$() + "ProxyPage", template().render(builder.toFrame()));
	}

	@Override
	protected ContextType contextType() {
		return ContextType.Spark;
	}

	private Frame[] parameters() {
		List<String> parameters = display.parameters();
		return parameters.stream().map(parameter -> new FrameBuilder("parameter")
				.add("type", "String")
				.add("name", parameter).toFrame()).toArray(Frame[]::new);
	}

	@Override
	protected File destinyPackage(File destiny) {
		return new File(destiny, format(CodeGenerationHelper.Pages, Target.Owner));
	}

	@Override
	protected String suffix() {
		return "Page";
	}
}
