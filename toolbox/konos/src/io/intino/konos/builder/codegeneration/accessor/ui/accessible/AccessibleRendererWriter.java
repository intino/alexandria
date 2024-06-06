package io.intino.konos.builder.codegeneration.accessor.ui.accessible;

import io.intino.itrules.Engine;
import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.template.Template;
import io.intino.konos.builder.codegeneration.Formatters;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.services.ui.UiRendererWriter;
import io.intino.konos.builder.codegeneration.services.ui.templates.AbstractDesktopSkeletonTemplate;
import io.intino.konos.builder.codegeneration.services.ui.templates.AbstractDisplaySkeletonTemplate;
import io.intino.konos.builder.codegeneration.services.ui.templates.ComponentTemplate;
import io.intino.konos.builder.codegeneration.services.ui.templates.PassiveViewNotifierTemplate;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.Display;
import io.intino.konos.dsl.PassiveView;
import io.intino.magritte.framework.Layer;

import java.io.File;

import static io.intino.itrules.template.Template.compose;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.displayFolder;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.displayNotifierFolder;
import static io.intino.konos.builder.helpers.Commons.javaFile;

public class AccessibleRendererWriter extends UiRendererWriter {
	private final File destination;

	public AccessibleRendererWriter(CompilationContext context, File destination) {
		super(context, Target.AccessibleAccessor);
		this.destination = destination;
	}

	public boolean write(Layer element, String type, FrameBuilder builder) {
		if (!element.i$(Display.Accessible.class)) return true;
		if (!builder.is("accessible")) return true;
		writeDisplay(element, type, builder);
		return true;
	}

	@Override
	public boolean writeNotifier(PassiveView element, FrameBuilder builder) {
		if (!element.i$(Display.Accessible.class)) return true;
		if (!builder.is("accessible")) return true;
		File notifierFile = javaFile(displayNotifierFolder(destination, target), nameOfPassiveViewFile(element, builder.toFrame(), "Notifier"));
		if (!context.cache().isModified(element) && notifierFile.exists()) return true;
		Frame frame = builder.toFrame();
		String name = nameOfPassiveViewFile(element, frame, "Notifier");
		if (!hasConcreteNotifier(element)) return false;
		writeFrame(displayNotifierFolder(destination, target), element, name, new PassiveViewNotifierTemplate().render(frame, Formatters.all));
		return true;
	}

	@Override
	public boolean writeRequester(PassiveView element, FrameBuilder builder) {
		return true;
	}

	@Override
	public boolean writePushRequester(PassiveView element, FrameBuilder builder) {
		return true;
	}

	public Template template(Layer layer, FrameBuilder builder) {
		if (layer.i$(io.intino.konos.dsl.Template.Desktop.class))
			return compose(new ComponentTemplate(), new AbstractDesktopSkeletonTemplate());
		return compose(new ComponentTemplate(), new AbstractDisplaySkeletonTemplate());
	}

	private void writeDisplay(Layer element, String type, FrameBuilder builder) {
		Template template = template(element, builder);
		if (template == null) return;
		final String newDisplay = displayName(element, isAccessible(builder));
		writeFrame(displayFolder(destination, type, target), element, newDisplay, new Engine(template).addAll(Formatters.all).render(builder.add("gen").toFrame()));
	}

}
