package io.intino.konos.builder.codegeneration.accessor.ui.web;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.accessor.ui.web.templates.*;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.services.ui.UiRendererWriter;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.PassiveView;
import io.intino.magritte.framework.Layer;

import java.io.File;

import static io.intino.konos.builder.helpers.CodeGenerationHelper.*;
import static io.intino.konos.builder.helpers.Commons.javaFile;

public class WebRendererWriter extends UiRendererWriter {

	public WebRendererWriter(CompilationContext context) {
		super(context, Target.Accessor);
	}

	public boolean write(Layer element, String type, FrameBuilder builder) {
		if (hasAbstractClass(element)) writeSrc(element, type, builder);
		writeGen(element, type, builder);
		return true;
	}

	@Override
	public boolean writeNotifier(PassiveView element, FrameBuilder builder) {
		File notifierFile = javaFile(displayNotifierFolder(gen(), target), nameOfPassiveViewFile(element, builder.toFrame(), "Notifier"));
		if (!context.cache().isModified(element) && notifierFile.exists()) return true;
		Frame frame = builder.toFrame();
		String name = nameOfPassiveViewFile(element, frame, "Notifier");
		if (!hasConcreteNotifier(element)) return false;
		writeFrame(displayNotifierFolder(gen(), target), element, name, notifierTemplate(element, builder).render(frame));
		return true;
	}

	@Override
	public boolean writeRequester(PassiveView element, FrameBuilder builder) {
		File requesterFile = javaFile(displayRequesterFolder(gen(), target), nameOfPassiveViewFile(element, builder.toFrame(), "Requester"));
		if (!context.cache().isModified(element) && requesterFile.exists()) return true;
		Frame frame = builder.toFrame();
		String name = nameOfPassiveViewFile(element, frame, "Requester");
		if (!hasConcreteRequester(element)) return false;
		writeFrame(displayRequesterFolder(gen(), target), element, name, requesterTemplate(element, builder).render(frame));
		return true;
	}

	@Override
	public boolean writePushRequester(PassiveView element, FrameBuilder builder) {
		File pushRequesterFile = javaFile(displayRequesterFolder(gen(), target), nameOfPassiveViewFile(element, builder.toFrame(), "PushRequester"));
		if (!context.cache().isModified(element) && pushRequesterFile.exists()) return true;
		Frame frame = builder.toFrame();
		Template template = pushRequesterTemplate(element, builder);
		boolean accessible = isAccessible(frame);
		if (accessible || template == null) return false;
		String name = nameOfPassiveViewFile(element, frame, "PushRequester");
		if (!hasConcreteRequester(element)) return false;
		writeFrame(displayRequesterFolder(gen(), target), element, name, template.render(frame));
		return true;
	}

	public Template srcTemplate(Layer layer, FrameBuilder builder) {
		if (builder.is("accessible")) return null;
		if (!ElementHelper.isRoot(layer)) return null;
		return setup(new DisplayTemplate());
	}

	public Template genTemplate(Layer layer, FrameBuilder builder) {
		if (layer.i$(io.intino.konos.model.Template.Desktop.class)) return setup(new AbstractDesktopTemplate());
		return setup(new AbstractDisplayTemplate());
	}

	public Template notifierTemplate(PassiveView element, FrameBuilder builder) {
		return setup(new PassiveViewNotifierTemplate());
	}

	public Template requesterTemplate(PassiveView element, FrameBuilder builder) {
		return setup(new PassiveViewRequesterTemplate());
	}

	public Template pushRequesterTemplate(PassiveView element, FrameBuilder builder) {
		return null;
	}

	private void writeSrc(Layer element, String type, FrameBuilder builder) {
		final String newDisplay = displayFilename(element.name$(), builder.is("accessible") ? "Proxy" : "");
		Template template = srcTemplate(element, builder);
		if (template == null) return;
		File sourceFile = displayFile(src(), newDisplay, type, target);
		if (sourceFile.exists()) return;
		writeFrame(displayFolder(src(), type, target), element, newDisplay, template.render(builder.toFrame()));
	}

	private void writeGen(Layer element, String type, FrameBuilder builder) {
		Template template = genTemplate(element, builder);
		if (template == null) return;
		final String newDisplay = displayName(element, isAccessible(builder));
		writeFrame(displayFolder(gen(), type, target), element, newDisplay, template.render(builder.add("gen").toFrame()));
	}

}
