package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.itrules.Engine;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.template.Template;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.services.ui.templates.*;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.dsl.PassiveView;
import io.intino.konos.dsl.Template.Desktop;
import io.intino.magritte.framework.Layer;

import java.io.File;

import static io.intino.itrules.template.Template.compose;
import static io.intino.konos.builder.codegeneration.Formatters.all;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.*;
import static io.intino.konos.builder.helpers.Commons.javaFile;

public class ServerRendererWriter extends UiRendererWriter {

	public ServerRendererWriter(CompilationContext context) {
		super(context, Target.Server);
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
		writeFrame(displayNotifierFolder(gen(), target), element, name, new PassiveViewNotifierTemplate().render(frame, all));
		registerClass(builder, displayNotifierFolder(gen(), target), element, "Notifier");
		return true;
	}

	@Override
	public boolean writeRequester(PassiveView element, FrameBuilder builder) {
		File requesterFile = javaFile(displayRequesterFolder(gen(), target), nameOfPassiveViewFile(element, builder.toFrame(), "Requester"));
		if (!context.cache().isModified(element) && requesterFile.exists()) return true;
		Frame frame = builder.toFrame();
		String name = nameOfPassiveViewFile(element, frame, "Requester");
		if (!hasConcreteRequester(element)) return false;
		writeFrame(displayRequesterFolder(gen(), target), element, name, new PassiveViewRequesterTemplate().render(frame, all));
		registerClass(builder, displayRequesterFolder(gen(), target), element, "Requester");
		return true;
	}

	@Override
	public boolean writePushRequester(PassiveView element, FrameBuilder builder) {
		File pushRequesterFile = javaFile(displayRequesterFolder(gen(), target), nameOfPassiveViewFile(element, builder.toFrame(), "PushRequester"));
		if (!context.cache().isModified(element) && pushRequesterFile.exists()) return true;
		Frame frame = builder.toFrame();
		var template = new PassiveViewPushRequesterTemplate();
		if (isAccessible(frame)) return false;
		String name = nameOfPassiveViewFile(element, frame, "PushRequester");
		if (!hasConcreteRequester(element)) return false;
		writeFrame(displayRequesterFolder(gen(), target), element, name, template.render(frame, all));
		registerClass(builder, displayRequesterFolder(gen(), target), element, "PushRequester");
		return true;
	}

	private void writeSrc(Layer element, String type, FrameBuilder builder) {
		final String newDisplay = displayFilename(element.name$(), builder.is("accessible") ? "Proxy" : "");
		var template = srcTemplate(element, builder);
		if (template == null) return;
		File sourceFile = displayFile(src(), newDisplay, type, target);
		if (sourceFile.exists()) return;
		writeFrame(displayFolder(src(), type, target), element, newDisplay, template.render(builder.toFrame(), all));
	}

	private void writeGen(Layer element, String type, FrameBuilder builder) {
		var template = genTemplate(element);
		final String newDisplay = displayName(element, isAccessible(builder));
		writeFrame(displayFolder(gen(), type, target), element, newDisplay, new Engine(template).addAll(all).render(builder.add("gen").toFrame()));
	}

	private DisplayTemplate srcTemplate(Layer layer, FrameBuilder builder) {
		if (builder.is("accessible")) return null;
		if (!ElementHelper.isRoot(layer)) return null;
		return new DisplayTemplate();
	}

	private Template genTemplate(Layer layer) {
		if (layer.i$(Desktop.class))
			return compose(new ComponentTemplate(), new DisplayBoxTemplate(), new AbstractDesktopSkeletonTemplate());
		return compose(new ComponentTemplate(), new DisplayBoxTemplate(), new AbstractDisplaySkeletonTemplate());
	}

	private void registerClass(FrameBuilder builder, File folder, PassiveView element, String suffix) {
		context.compiledFiles().add(new OutputItem(context.sourceFileOf(element), javaFile(folder, nameOfPassiveViewFile(element, builder.toFrame(), suffix)).getAbsolutePath()));
	}

}
