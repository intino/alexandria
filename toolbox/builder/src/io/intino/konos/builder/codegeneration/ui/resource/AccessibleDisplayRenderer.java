package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.action.AccessibleDisplayActionRenderer;
import io.intino.konos.builder.codegeneration.services.ui.templates.ResourceTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.accessible.AccessibleDisplay;

import java.io.File;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;

public class AccessibleDisplayRenderer extends UIRenderer {
	private final AccessibleDisplay display;

	public AccessibleDisplayRenderer(Settings settings, AccessibleDisplay display, Target target) {
		super(settings, target);
		this.display = display;
	}

	public void execute() {
		if (isRendered(display)) return;

		FrameBuilder builder = frameBuilder().add("name", display.name$());
		builder.add("resource");
		builder.add(display.getClass().getSimpleName());

		builder.add("parameter", parameters(display));
		Commons.writeFrame(new File(gen(), format(Resources)), snakeCaseToCamelCase(display.name$() + "ProxyResource"), setup(new ResourceTemplate()).render(builder.toFrame()));

		new AccessibleDisplayActionRenderer(settings, display).execute();

		saveRendered(display);
	}

	private FrameBuilder[] parameters(AccessibleDisplay display) {
		return display.parameters().stream().map(parameter -> new FrameBuilder().add("parameter")
				.add("name", parameter)).toArray(FrameBuilder[]::new);
	}
}