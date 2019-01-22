package io.intino.konos.builder.codegeneration.action;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.UIRenderer;
import io.intino.konos.model.graph.accessible.AccessibleDisplay;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;

public class AccessibleDisplayActionRenderer extends ActionRenderer {
	private final AccessibleDisplay display;
	private final Settings configuration;

	public AccessibleDisplayActionRenderer(Settings settings, AccessibleDisplay display) {
		super(settings.project(), settings.src(), settings.packageName(), settings.boxName(),"accessibleDisplay");
		this.configuration = settings;
		this.display = display;
	}

	public void execute() {
		Frame frame = new Frame().addTypes("action", "ui", "accessibleDisplay");
		frame.addSlot("name", display.name$());
		frame.addSlot("display", display.name$());
		frame.addSlot("package", packageName);
		frame.addSlot("box", boxName);
		frame.addSlot("parameter", parameters());
		configuration.classes().put(display.getClass().getSimpleName() + "#" + firstUpperCase(display.core$().name()), "actions" + "." + firstUpperCase(snakeCaseToCamelCase(display.name$())) + suffix());
		if (!alreadyRendered(destiny, display.name$() + "Proxy"))
			writeFrame(destinyPackage(destiny), display.name$() + "ProxyAction", template().format(frame));
	}

	private Frame[] parameters() {
		List<String> parameters = display.parameters();
		return parameters.stream().map(parameter -> new Frame().addTypes("parameter")
				.addSlot("type", "String")
				.addSlot("name", parameter)).toArray(Frame[]::new);
	}

	@Override
	protected File destinyPackage(File destiny) {
		return new File(destiny, UIRenderer.Pages);
	}

	@Override
	protected String suffix() {
		return "Page";
	}
}
