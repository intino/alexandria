package io.intino.konos.builder.codegeneration.action;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.accessible.AccessibleDisplay;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class AccessibleDisplayActionRenderer extends ActionRenderer {
	private final AccessibleDisplay display;
	private final Settings configuration;

	public AccessibleDisplayActionRenderer(Settings settings, AccessibleDisplay display) {
		super(settings,"accessibleDisplay");
		this.configuration = settings;
		this.display = display;
	}

	@Override
	public void render() {
		FrameBuilder builder = new FrameBuilder("action", "ui", "accessibleDisplay");
		builder.add("name", display.name$());
		builder.add("display", display.name$());
		builder.add("package", packageName());
		builder.add("box", boxName());
		builder.add("parameter", parameters());
		configuration.classes().put(display.getClass().getSimpleName() + "#" + firstUpperCase(display.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(display.name$())) + suffix());
		if (!alreadyRendered(src(), display.name$() + "Proxy"))
			writeFrame(destinyPackage(src()), display.name$() + "ProxyAction", template().render(builder.toFrame()));
	}

	private Frame[] parameters() {
		List<String> parameters = display.parameters();
		return parameters.stream().map(parameter -> new FrameBuilder("parameter")
				.add("type", "String")
				.add("name", parameter).toFrame()).toArray(Frame[]::new);
	}

	@Override
	protected File destinyPackage(File destiny) {
		return new File(destiny, UIRenderer.format(UIRenderer.Pages, UIRenderer.Target.Service));
	}

	@Override
	protected String suffix() {
		return "Page";
	}
}
