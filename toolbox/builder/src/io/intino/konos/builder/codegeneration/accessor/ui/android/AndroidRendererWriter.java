package io.intino.konos.builder.codegeneration.accessor.ui.android;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.builder.codegeneration.accessor.ui.android.templates.*;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.services.ui.UiRendererWriter;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.PassiveView;
import io.intino.magritte.framework.Layer;

import java.io.File;

import static io.intino.konos.builder.helpers.CodeGenerationHelper.*;

public class AndroidRendererWriter extends UiRendererWriter {

	public AndroidRendererWriter(CompilationContext context) {
		super(context, Target.Android);
	}

	@Override
	public boolean write(Layer element, String type, FrameBuilder builder) {
		writeSrc(element, type, builder);
		writeGen(element, type, builder);
		writeLayout(element, type, builder);
		writeShared(element, type, builder);
		return true;
	}

	@Override
	public boolean writeNotifier(PassiveView element, FrameBuilder builder) {
		Frame frame = builder.toFrame();
		String name = nameOfPassiveViewFile(element, frame, "Notifier");
		if (!hasConcreteNotifier(element)) return false;
		writeFrame(displayNotifierFolder(src(Target.MobileShared), target), element, name, notifierTemplate(element, builder).render(frame));
		return true;
	}

	@Override
	public boolean writeRequester(PassiveView element, FrameBuilder builder) {
		Frame frame = builder.toFrame();
		String name = nameOfPassiveViewFile(element, frame, "Requester");
		if (!hasConcreteRequester(element)) return false;
		writeFrame(displayRequesterFolder(gen(Target.MobileShared), target), element, name, requesterTemplate(element, builder).render(frame));
		return true;
	}

	@Override
	public boolean writePushRequester(PassiveView element, FrameBuilder builder) {
		return false;
	}

	private void writeSrc(Layer element, String type, FrameBuilder builder) {
		Template template = srcTemplate(element, builder);
		if (template == null) return;
		final String newDisplay = displayFilename(element.name$(), isAccessible(builder) ? "Proxy" : "");
		File file = displayFile(src(), newDisplay, type, target);
		if (!element.i$(io.intino.konos.model.Template.class) && file.exists()) return;
		writeFrame(displayFolder(src(), type, target), element, newDisplay, template.render(builder.toFrame()));
	}

	private void writeGen(Layer element, String type, FrameBuilder builder) {
		Template template = genTemplate(element, builder);
		if (template == null) return;
		final String newDisplay = displayFilename(element.name$(), isAccessible(builder) ? "Proxy" : "");
		File file = displayFile(gen(), newDisplay, type, target);
		if (file.exists()) return;
		writeFrame(displayFolder(gen(), type, target), element, newDisplay, template.render(builder.toFrame()));
	}

	private void writeLayout(Layer element, String type, FrameBuilder builder) {
		builder.add("res");
		Template template = resTemplate(element, builder);
		if (template == null) return;
		final String newDisplay = displayFilename(element.name$(), isAccessible(builder) ? "Proxy" : "");
		File file = displayFile(res(), newDisplay, type, Target.AndroidResource);
		if (hasAbstractClass(element) && file.exists()) return;
		writeFrame(displayFolder(res(), type, Target.AndroidResource), element, newDisplay, template.render(builder.toFrame()), Target.AndroidResource);
	}

	private void writeShared(Layer element, String type, FrameBuilder builder) {
		if (!hasAbstractClass(element)) return;
		builder.add("interface");
		Template template = srcTemplate(element, builder);
		if (template == null) return;
		final String newDisplay = displayFilename(element.name$(), isAccessible(builder) ? "Proxy" : "");
		writeFrame(displayFolder(src(Target.MobileShared), type, Target.MobileShared), element, newDisplay, template.render(builder.toFrame()), Target.MobileShared);
	}

	public Template srcTemplate(Layer layer, FrameBuilder builder) {
		if (isAccessible(builder)) return null;
		if (!ElementHelper.isRoot(layer)) return null;
		return setup(new DisplayTemplate());
	}

	public Template genTemplate(Layer layer, FrameBuilder builder) {
		if (layer.i$(io.intino.konos.model.Template.Desktop.class)) return setup(new AbstractDesktopTemplate());
		return setup(new AbstractDisplayTemplate());
	}

	public Template resTemplate(Layer layer, FrameBuilder builder) {
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

}
