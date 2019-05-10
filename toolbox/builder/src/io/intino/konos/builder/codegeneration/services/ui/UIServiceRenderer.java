package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Dialog;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.KonosGraph;
import io.intino.konos.model.graph.ui.UIService;

import java.io.File;
import java.util.List;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.model.graph.Display.Request.ResponseType.Asset;
import static io.intino.konos.model.graph.KonosGraph.dialogsOf;
import static io.intino.konos.model.graph.KonosGraph.displaysOf;

public class UIServiceRenderer extends UIRenderer {
	private final File gen;
	private final List<UIService> uiServiceList;

	public UIServiceRenderer(KonosGraph graph, File gen, String packageName, String boxName) {
		super(boxName, packageName);
		this.gen = gen;
		this.uiServiceList = graph.uIServiceList();
	}

	public void execute() {
		uiServiceList.forEach(this::processUIService);
	}

	private void processUIService(UIService service) {
		final List<Dialog> dialogs = dialogsOf(service);
		final List<Display> displays = displaysOf(service);
		FrameBuilder builder = frameBuilder().add("ui").
				add("name", service.name$()).
				add("resource", resourcesFrame(service.resourceList()));
		if (service.userHome() != null) builder.add("userHome", service.userHome().name$());
		if (!dialogs.isEmpty())
			builder.add("dialog", dialogsFrame(dialogs)).add("dialogsImport", packageName);
		if (!displays.isEmpty())
			builder.add("display", displaysFrame(displays)).add("displaysImport", packageName);
		if (service.authentication() != null) builder.add("auth", service.authentication().by());
		writeFrame(gen, snakeCaseToCamelCase(service.name$() + "Service"), template().render(builder.toFrame()));
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
		final UIService service = resource.core$().ownerAs(UIService.class);
		String path = resource.path();
		Set<String> custom = Commons.extractParameters(path);
		final FrameBuilder builder = new FrameBuilder("resource", "abstractResource").add("name", resource.name$());
		FrameBuilder pathFrameBuilder = new FrameBuilder("path").add("value", path).add("name", resource.name$());
		if (resource.isEditorPage()) pathFrameBuilder.add("editor");
		if (service.userHome() != null) pathFrameBuilder.add("userHome", service.userHome().name$());
		if (!custom.isEmpty()) pathFrameBuilder.add("custom", custom.toArray(new String[0]));
		builder.add("path", pathFrameBuilder.toFrame());
		return builder.toFrame();
	}

	private Frame frameOf(Display display) {
		final FrameBuilder builder = newDisplayFrame(display, new FrameBuilder("display"));
		if (display.isAccessible())
			builder.add("accessible").add("display", newDisplayFrame(display, new FrameBuilder("display", "proxy")).toFrame());
		return builder.toFrame();
	}

	private FrameBuilder newDisplayFrame(Display display, FrameBuilder builder) {
		builder.add("name", display.name$()).add("package", packageName);
		if (display.requestList().stream().anyMatch(r -> r.responseType().equals(Asset)))
			builder.add("asset", display.name$());
		return builder;
	}

	private Frame frameOf(Dialog dialog) {
		return new FrameBuilder("dialog").add("name", dialog.name$()).add("package", packageName).toFrame();
	}

	private Template template() {
		return customize(new UIServiceTemplate());
	}

	private Template customize(Template template) {
		template.add("SnakeCaseToCamelCase", value -> snakeCaseToCamelCase(value.toString()));
		template.add("ReturnTypeFormatter", (value) -> value.equals("Void") ? "void" : value);
		template.add("validname", value -> value.toString().replace("-", "").toLowerCase());
		return template;
	}
}
