package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.templates.ServiceTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.model.graph.KonosGraph.displaysOf;
import static io.intino.konos.model.graph.PassiveView.Request.ResponseType.Asset;

public class ServiceRenderer extends UIRenderer {
	private final UIService service;

	public ServiceRenderer(Settings settings, UIService service) {
		super(settings, Target.Service);
		this.service = service;
	}

	public void execute() {
		final List<Display> displays = displaysOf(service);
		Frame frame = buildFrame().addTypes("ui").
				addSlot("name", service.name$()).
				addSlot("resource", resourcesFrame(service.resourceList()));
		if (service.userHome() != null) frame.addSlot("userHome", service.userHome().name$());
		if (!displays.isEmpty())
			frame.addSlot("display", displaysFrame(displays)).addSlot("displaysImport", packageName());
		if (service.authentication() != null) frame.addSlot("auth", service.authentication().by());
		writeFrame(new File(gen(), UI), snakeCaseToCamelCase(service.name$() + "Service"), template().format(frame));
	}

	private Frame[] resourcesFrame(List<UIService.Resource> resourceList) {
		return resourceList.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame[] displaysFrame(List<Display> displays) {
		return displays.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame frameOf(UIService.Resource resource) {
		final Frame frame = new Frame().addTypes("resource", "abstractResource");
		frame.addSlot("name", resource.name$());
		final UIService service = resource.core$().ownerAs(UIService.class);
		String path = resource.path();
		Set<String> custom = Commons.extractParameters(path);
		Frame pathFrame = new Frame("path").addSlot("value", path).addSlot("name", resource.name$());
		if (service.userHome() != null) pathFrame.addSlot("userHome", service.userHome().name$());
		if (!custom.isEmpty()) pathFrame.addSlot("custom", custom.toArray(new String[0]));
		frame.addSlot("path", pathFrame);
		return frame;
	}

	private Frame frameOf(Display display) {
		final Frame frame = newDisplayFrame(display, new Frame("display"));
		String type = typeOf(display);
		if (!type.equalsIgnoreCase("display")) frame.addSlot("type", typeOf(display).toLowerCase());
		if (display.isAccessible())
			frame.addTypes("accessible").addSlot("display", newDisplayFrame(display, new Frame("display", "proxy")));
		return frame;
	}

	private Frame newDisplayFrame(Display display, Frame frame) {
		frame.addSlot("name", clean(display.name$())).addSlot("package", packageName());
		if (display.requestList().stream().anyMatch(r -> r.responseType().equals(Asset)))
			frame.addSlot("asset", display.name$());
		return frame;
	}

	private Template template() {
		return addFormats(ServiceTemplate.create());
	}

}
