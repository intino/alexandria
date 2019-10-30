package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.action.AccessibleDisplayActionRenderer;
import io.intino.konos.builder.codegeneration.services.ui.templates.ResourceTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Display;

import java.util.List;
import java.util.stream.Collectors;

import static io.intino.konos.builder.helpers.CodeGenerationHelper.resourceFilename;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.resourceFolder;

public class AccessibleDisplayRenderer extends UIRenderer {
	private final Display.Accessible display;

	public AccessibleDisplayRenderer(Settings settings, Display.Accessible display, Target target) {
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
		if (target == Target.Owner) Commons.writeFrame(resourceFolder(gen(), target), resourceFilename(display.name$(), "ProxyResource"), setup(new ResourceTemplate()).render(builder.toFrame()));

		new AccessibleDisplayActionRenderer(settings, display).execute();

		saveRendered(display);
	}

	private FrameBuilder[] parameters(Display.Accessible display) {
		List<FrameBuilder> result = display.parameters().stream().map(parameter -> new FrameBuilder("parameter").add("name", parameter)).collect(Collectors.toList());
		result.add(new FrameBuilder("parameter").add("name", "personifiedDisplay"));
		return result.toArray(new FrameBuilder[0]);
	}
}