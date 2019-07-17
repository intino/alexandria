package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.services.ui.templates.ServiceTemplate;
import io.intino.konos.builder.codegeneration.ui.I18nRenderer;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.ui.UIService;

import java.util.List;
import java.util.Set;

import static io.intino.konos.builder.helpers.CodeGenerationHelper.serviceFilename;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.serviceFolder;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.model.graph.PassiveView.Request.ResponseType.Asset;

public class ServiceRenderer extends UIRenderer {
	private final UIService service;

	public ServiceRenderer(Settings settings, UIService service) {
		super(settings, Target.Owner);
		this.service = service;
	}

	@Override
	public void render() {
		createUi();
		new I18nRenderer(settings, service, target).execute();
	}

	private void createUi() {
		final List<Display> displays = service.graph().rootDisplays();
		FrameBuilder builder = buildFrame().add("ui").add("name", service.name$()).add("resource", resourcesFrame(service.resourceList()));
		if (service.userHome() != null) builder.add("userHome", service.userHome().name$());
		if (!displays.isEmpty())
			builder.add("display", displaysFrame(displays)).add("displaysImport", packageName());
		if (service.authentication() != null) builder.add("auth", service.authentication().by());
		writeFrame(serviceFolder(gen()), serviceFilename(service.name$()), template().render(builder.toFrame()));
	}

	private Frame[] resourcesFrame(List<UIService.Resource> resourceList) {
		return resourceList.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame[] displaysFrame(List<Display> displays) {
		return displays.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame frameOf(UIService.Resource resource) {
		final FrameBuilder result = new FrameBuilder("resource").add("abstractResource");
		result.add("name", resource.name$());
		final UIService service = resource.core$().ownerAs(UIService.class);
		String path = resource.path();
		Set<String> custom = Commons.extractParameters(path);
		FrameBuilder pathBuilder = new FrameBuilder("path").add("value", path).add("name", resource.name$());
		if (service.userHome() != null) pathBuilder.add("userHome", service.userHome().name$());
		if (!custom.isEmpty()) pathBuilder.add("custom", custom.toArray(new String[0]));
		result.add("path", pathBuilder);
		return result.toFrame();
	}

	private Frame frameOf(Display display) {
		final FrameBuilder result = newDisplayFrame(display, new FrameBuilder("display"));
		String type = typeOf(display);
		if (!type.equalsIgnoreCase("display")) result.add("type", typeOf(display).toLowerCase());
		if (display.isAccessible())
			result.add("accessible").add("display", newDisplayFrame(display, new FrameBuilder("display").add("proxy")));
		return result.toFrame();
	}

	private FrameBuilder newDisplayFrame(Display display, FrameBuilder builder) {
		builder.add("name", nameOf(display)).add("package", packageName());
		if (display.requestList().stream().anyMatch(r -> r.responseType().equals(Asset)))
			builder.add("asset", display.name$());
		return builder;
	}

	private Template template() {
		return addFormats(new ServiceTemplate());
	}

}
