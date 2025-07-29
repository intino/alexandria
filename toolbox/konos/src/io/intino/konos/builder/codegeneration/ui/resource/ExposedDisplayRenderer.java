package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.action.ExposedDisplayActionRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.services.ui.templates.ResourceTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.dsl.Display;
import io.intino.konos.dsl.Service;

import java.util.List;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.customize;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.resourceFilename;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.resourceFolder;
import static io.intino.konos.builder.helpers.Commons.javaFile;

public class ExposedDisplayRenderer extends UIRenderer {
	private final Display.Exposed display;
	private final Target target;

	public ExposedDisplayRenderer(CompilationContext compilationContext, Display.Exposed display, Target target) {
		super(compilationContext);
		this.display = display;
		this.target = target;
	}

	@Override
	public void render() throws KonosException {

		if (target == Target.Service) {
			FrameBuilder builder = createFrame(true);
			Commons.writeFrame(resourceFolder(gen(target), target), resourceFilename(display.name$(), "ProxyResource"), new ResourceTemplate().render(builder.toFrame(), Formatters.all));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(display), javaFile(resourceFolder(gen(target), target), resourceFilename(display.name$(), "ProxyResource")).getAbsolutePath()));
		}

		new ExposedDisplayActionRenderer(context, display, target).execute();
	}

	private FrameBuilder createFrame(boolean proxy) {
		FrameBuilder builder = buildFrame().add("exposedDisplay").add("name", display.name$());
		builder.add("resource");
		builder.add(display.getClass().getSimpleName());
		builder.add("render", renderFrame());
		Service.UI service = display.graph().serviceList().stream().filter(Service::isUI).map(Service::asUI).findFirst().orElse(null);
		if (service != null && service.googleApiKey() != null) builder.add("googleApiKey", customize("googleApiKey", service.googleApiKey()));
		builder.add("parameter", parameters(display, proxy));
		return builder;
	}

	private FrameBuilder renderFrame() {
		FrameBuilder result = buildBaseFrame().add("render");
		if (display.confidential()) result.add("confidential");
		return result;
	}

	private FrameBuilder[] parameters(Display.Exposed display, boolean proxy) {
		List<FrameBuilder> result = display.parameters().stream().map(p -> frameOf(p, proxy)).collect(Collectors.toList());
		if (proxy) result.add(frameOf("personifiedDisplay", proxy));
		return result.toArray(new FrameBuilder[0]);
	}

	private FrameBuilder frameOf(String parameter, boolean proxy) {
		return new FrameBuilder("parameter").add("name", parameter).add("resource", display.name$() + (proxy ? "Proxy" : ""));
	}

}