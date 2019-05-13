package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.templates.ResourceTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.action.AccessibleDisplayActionRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.accessible.AccessibleDisplay;
import org.siani.itrules.model.Frame;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class AccessibleDisplayRenderer extends UIRenderer {
	private final AccessibleDisplay display;

	public AccessibleDisplayRenderer(Settings settings, AccessibleDisplay display, Target target) {
		super(settings, target);
		this.display = display;
	}

	public void execute() {
		Frame frame = buildFrame().addSlot("name", display.name$()).addTypes("resource", display.getClass().getSimpleName());

		frame.addSlot("parameter", parameters(display));
		Commons.writeFrame(new File(gen(), format(Resources)), snakeCaseToCamelCase(display.name$() + "ProxyResource"), setup(ResourceTemplate.create()).format(frame));

		new AccessibleDisplayActionRenderer(settings, display).execute();
	}

	private Frame[] parameters(AccessibleDisplay display) {
		return display.parameters().stream().map(parameter -> new Frame().addTypes("parameter")
				.addSlot("name", parameter)).toArray(Frame[]::new);
	}
}