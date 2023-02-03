package io.intino.konos.builder.codegeneration.ui.resource;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.action.AccessibleDisplayActionRenderer;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.services.ui.templates.ResourceTemplate;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.Commons;
import io.intino.konos.model.Display;

import java.util.List;
import java.util.stream.Collectors;

import static io.intino.konos.builder.helpers.CodeGenerationHelper.resourceFilename;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.resourceFolder;
import static io.intino.konos.builder.helpers.Commons.javaFile;

public class AccessibleDisplayRenderer extends UIRenderer {
	private final Display.Accessible display;
	private final Target target;

	public AccessibleDisplayRenderer(CompilationContext compilationContext, Display.Accessible display, Target target) {
		super(compilationContext);
		this.display = display;
		this.target = target;
	}

	@Override
	public void render() throws KonosException {
		FrameBuilder builder = buildFrame().add("accessibleDisplay").add("name", display.name$());
		builder.add("resource");
		builder.add(display.getClass().getSimpleName());
		builder.add("render", renderFrame());

		builder.add("parameter", parameters(display));
		if (target == Target.Server) {
			Commons.writeFrame(resourceFolder(gen(target), target), resourceFilename(display.name$(), "ProxyResource"), setup(new ResourceTemplate()).render(builder.toFrame()));
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(display), javaFile(resourceFolder(gen(target), target), resourceFilename(display.name$(), "ProxyResource")).getAbsolutePath()));
		}

		new AccessibleDisplayActionRenderer(context, display, target).execute();
	}

	private FrameBuilder renderFrame() {
		FrameBuilder result = buildBaseFrame().add("render");
		if (display.confidential()) result.add("confidential");
		return result;
	}

	private FrameBuilder[] parameters(Display.Accessible display) {
		List<FrameBuilder> result = display.parameters().stream().map(parameter -> new FrameBuilder("parameter").add("name", parameter)).collect(Collectors.toList());
		result.add(new FrameBuilder("parameter").add("name", "personifiedDisplay"));
		return result.toArray(new FrameBuilder[0]);
	}
}