package io.intino.konos.builder.codegeneration.accessor.ui;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.AppTemplate;
import io.intino.konos.builder.codegeneration.accessor.ui.templates.PassiveViewTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;

import static io.intino.konos.builder.helpers.Commons.write;

public class AppRenderer extends UIRenderer {
	private final UIService service;

	protected AppRenderer(Settings settings, UIService service) {
		super(settings, Target.Accessor);
		this.service = service;
	}

	@Override
	public void execute() {
		writeApp();
		writePassiveView();
	}

	private void writeApp() {
		Frame frame = new Frame().addTypes("app");
		service.resourceList().forEach(r -> frame.addSlot("page", new Frame().addTypes("page").addSlot("value", r.name$())));
		write(new File(accessorGen() + File.separator + "App.js").toPath(), setup(AppTemplate.create()).format(frame));
	}

	private void writePassiveView() {
		Template template = PassiveViewTemplate.create();
		File notifiersFile = createIfNotExists(new File(accessorGen() + "/displays/notifiers"));
		File requestersFile = createIfNotExists(new File(accessorGen() + "/displays/requesters"));
		write(new File(notifiersFile + File.separator + "Notifier.js").toPath(), template.format(new Frame().addTypes("notifier")));
		write(new File(requestersFile + File.separator + "Requester.js").toPath(), template.format(new Frame().addTypes("requester")));
	}
}
