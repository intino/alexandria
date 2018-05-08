package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Dialog;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.ui.UIService;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.List;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.model.graph.Display.Request.ResponseType.Asset;
import static io.intino.konos.model.graph.KonosGraph.dialogsOf;
import static io.intino.konos.model.graph.KonosGraph.displaysOf;

public class UIRenderer {
	private final File src;
	private final File gen;
	private final String packageName;
	private final String boxName;
	private final List<UIService> uiServiceList;

	public UIRenderer(KonosGraph graph, File src, File gen, String packageName, String boxName) {
		this.src = src;
		this.gen = gen;
		this.packageName = packageName;
		this.boxName = boxName;
		this.uiServiceList = graph.uIServiceList();
	}

	public void execute() {
		uiServiceList.forEach(this::processUIService);
	}

	private void processUIService(UIService service) {
		final List<Dialog> dialogs = dialogsOf(service);
		final List<Display> displays = displaysOf(service);
		Frame frame = new Frame().addTypes("ui").
				addSlot("package", packageName).
				addSlot("name", service.name$()).
				addSlot("box", boxName).addSlot("resource", resourcesFrame(service.resourceList()));
		if (service.userHome() != null) frame.addSlot("userHome", service.userHome().name$());
		if (!dialogs.isEmpty())
			frame.addSlot("dialog", dialogsFrame(dialogs)).addSlot("dialogsImport", packageName);
		if (!displays.isEmpty())
			frame.addSlot("display", displaysFrame(displays)).addSlot("displaysImport", packageName);
		if (service.authentication() != null) frame.addSlot("auth", service.authentication().by());
		writeFrame(gen, snakeCaseToCamelCase(service.name$()), template().format(frame));
	}

	private Frame[] resourcesFrame(List<UIService.Resource> resourceList) {
		return resourceList.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame[] displaysFrame(List<Display> displays) {
		return displays.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame[] dialogsFrame(List<Dialog> dialogs) {
		return dialogs.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame frameOf(UIService.Resource resource) {
		final Frame frame = new Frame().addTypes("resource", "abstractResource");
		frame.addSlot("name", resource.name$());
		final UIService service = resource.core$().ownerAs(UIService.class);
		String path = resource.path();
		Set<String> custom = Commons.extractParameters(path);
		Frame pathFrame = new Frame().addSlot("value", path).addSlot("name", resource.name$());
		if (service.userHome() != null) pathFrame.addSlot("userHome", service.userHome().name$());
		if (!custom.isEmpty()) pathFrame.addSlot("custom", custom.toArray(new String[custom.size()]));
		frame.addSlot("path", pathFrame);
		return frame;
	}

	private Frame frameOf(Display display) {
		final Frame frame = new Frame().addTypes("display");
		frame.addSlot("name", display.name$()).addSlot("package", packageName);
		if (display.requestList().stream().anyMatch(r -> r.responseType().equals(Asset)))
			frame.addSlot("asset", display.name$());
		return frame;
	}

	private Frame frameOf(Dialog dialog) {
		return new Frame().addTypes("dialog").addSlot("name", dialog.name$()).addSlot("package", packageName);
	}

	private Template template() {
		Template template = UITemplate.create();
		addFormats(template);
		return template;
	}

	private void addFormats(Template template) {
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
	}
}
