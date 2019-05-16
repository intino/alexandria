package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.AppTemplate;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.PassiveViewTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.ui.UIService;

import java.io.File;

public class AppRenderer extends UIRenderer {
	private final UIService service;

	protected AppRenderer(Settings settings, UIService service) {
		super(settings, Target.Accessor);
		this.service = service;
	}

	@Override
	public void render() {
		writeApp();
		writePassiveView();
	}

	private void writeApp() {
		FrameBuilder builder = new FrameBuilder("app");
		service.resourceList().forEach(r -> builder.add("page", new FrameBuilder("page").add("value", r.name$()).toFrame()));
		Commons.write(new File(accessorGen() + File.separator + "App.js").toPath(), setup(new AppTemplate()).render(builder.toFrame()));
	}

	private void writePassiveView() {
		Template template = new PassiveViewTemplate();
		File notifiersFile = createIfNotExists(new File(accessorGen() + "/displays/notifiers"));
		File requestersFile = createIfNotExists(new File(accessorGen() + "/displays/requesters"));
		Commons.write(new File(notifiersFile + File.separator + "Notifier.js").toPath(), template.render(new FrameBuilder("notifier")));
		Commons.write(new File(requestersFile + File.separator + "Requester.js").toPath(), template.render(new FrameBuilder("requester")));
	}
}
