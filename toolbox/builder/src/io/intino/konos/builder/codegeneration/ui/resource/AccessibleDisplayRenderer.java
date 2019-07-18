package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.action.AccessibleDisplayActionRenderer;
import io.intino.konos.builder.codegeneration.services.ui.templates.ResourceTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.accessible.AccessibleDisplay;

import static io.intino.konos.builder.helpers.CodeGenerationHelper.resourceFilename;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.resourceFolder;

public class AccessibleDisplayRenderer extends UIRenderer {
	private final AccessibleDisplay display;

	public AccessibleDisplayRenderer(Settings settings, AccessibleDisplay display, Target target) {
		super(settings, target);
		this.display = display;
	}

	@Override
	public void render() {
		if (isRendered(display)) return;

		FrameBuilder builder = buildFrame().add("name", display.name$());
		builder.add("resource");
		builder.add(display.getClass().getSimpleName());

		builder.add("parameter", parameters(display));
		Commons.writeFrame(resourceFolder(gen(), target), resourceFilename(display.name$(), "ProxyResource"), setup(new ResourceTemplate()).render(builder.toFrame()));

		new AccessibleDisplayActionRenderer(settings, display).execute();

		saveRendered(display);
	}

	private FrameBuilder[] parameters(AccessibleDisplay display) {
		return display.parameters().stream().map(parameter -> new FrameBuilder().add("parameter")
				.add("name", parameter)).toArray(FrameBuilder[]::new);
	}
}