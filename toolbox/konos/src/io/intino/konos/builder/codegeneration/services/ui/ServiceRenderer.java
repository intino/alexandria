package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.templates.ServiceTemplate;
import io.intino.konos.builder.codegeneration.ui.I18nRenderer;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.dsl.Display;
import io.intino.konos.dsl.Service;

import java.io.File;
import java.util.List;
import java.util.Set;

import static io.intino.konos.builder.helpers.CodeGenerationHelper.serviceFilename;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.serviceFolder;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.dsl.PassiveView.Request.ResponseType.Asset;

public class ServiceRenderer extends UIRenderer {
	private final Service.UI service;
	private final Target target;
	private final File destination;

	public ServiceRenderer(CompilationContext compilationContext, Service.UI service, Target target) {
		this(compilationContext, service, target, null);
	}

	public ServiceRenderer(CompilationContext compilationContext, Service.UI service, Target target, File destination) {
		super(compilationContext);
		this.service = service;
		this.target = target;
		this.destination = destination;
	}

	@Override
	public void render() throws KonosException {
		createUi();
		if (target == Target.Service) new I18nRenderer(context, service, target).execute();
	}

	private void createUi() {
		final List<Display> displays = service.graph().rootDisplays(context.graphName());
		FrameBuilder builder = buildFrame().add("ui").add("name", service.name$()).add("resource", resourcesFrame(service.resourceList()));
		if (userHome(service) != null) builder.add("userHome", userHome(service).name$());
		if (target == Target.ExposedAccessor) builder.add("exposedAccessor");
		if (!displays.isEmpty()) {
			builder.add("display", displaysFrame(displays)).add("displaysImport", packageName());
			boolean hasNotifiers = displays.stream().anyMatch(this::hasConcreteNotifier);
			if (hasNotifiers) builder.add("notifiersImport", packageName()).add("requestersImport", packageName());
		}
		if (service.authentication() != null) builder.add("auth", service.authentication().by());
		writeFrame(serviceFolder(destination()), serviceFilename(service.name$()), new ServiceTemplate().render(builder.toFrame(), Formatters.all));
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(service), javaFile(serviceFolder(gen(Target.Service)), serviceFilename(service.name$())).getAbsolutePath()));
	}

	private Frame[] resourcesFrame(List<Service.UI.Resource> resourceList) {
		return resourceList.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame[] displaysFrame(List<Display> displays) {
		return displays.stream().distinct().map(this::frameOf).toArray(Frame[]::new);
	}

	public Service.UI.Resource userHome(Service.UI service) {
		return service.homeList().stream().filter(Service.UI.Resource::isConfidential).findFirst().orElse(null);
	}

	private Frame frameOf(Service.UI.Resource resource) {
		final FrameBuilder result = new FrameBuilder("resource").add("abstractResource");
		if (resource.isExposed()) result.add("exposed");
		result.add("name", resource.name$());
		final Service.UI service = resource.core$().ownerAs(Service.UI.class);
		String path = resource.path();
		Set<String> custom = Commons.extractParameters(path);
		FrameBuilder pathBuilder = new FrameBuilder("path").add("value", path).add("name", resource.name$());
		if (userHome(service) != null) pathBuilder.add("userHome", userHome(service).name$());
		if (!custom.isEmpty()) pathBuilder.add("custom", custom.toArray(new String[0]));
		result.add("path", pathBuilder);
		return result.toFrame();
	}

	private Frame frameOf(Display display) {
		final FrameBuilder result = newDisplayFrame(display, new FrameBuilder("display"));
		if (display.isExposed())
			result.add("exposed").add("display", newDisplayFrame(display, new FrameBuilder("display").add("proxy")));
		if (!hasConcreteNotifier(display)) {
			result.add("genericNotifier");
			result.add("generic", notifierName(display));
		}
		return result.toFrame();
	}

	private FrameBuilder newDisplayFrame(Display display, FrameBuilder builder) {
		String type = typeOf(display);
		if (!type.equalsIgnoreCase("display")) {
			builder.add(typeOf(display).toLowerCase());
			builder.add("type", typeOf(display).toLowerCase());
		}
		builder.add("name", nameOf(display)).add("package", packageName());
		builder.add("requesterType", requesterTypeOf(display));
		if (display.requestList().stream().anyMatch(r -> r.responseType().equals(Asset)))
			builder.add("asset", display.name$());
		return builder;
	}

	private File destination() {
		return destination != null ? destination : gen(Target.Service);
	}

}
