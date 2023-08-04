package io.intino.konos.builder.codegeneration.ui;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.builder.codegeneration.services.ui.Updater;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.context.KonosException;
import io.intino.konos.builder.helpers.CodeGenerationHelper;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.Component;
import io.intino.magritte.framework.Layer;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.*;
import static io.intino.konos.builder.helpers.Commons.*;

public abstract class ElementRenderer<C extends Layer> extends UIRenderer {
	protected final C element;
	protected final RendererWriter writer;

	protected ElementRenderer(CompilationContext compilationContext, C element, RendererWriter rendererWriter) {
		super(compilationContext);
		this.element = element;
		this.writer = rendererWriter;
	}

	@Override
	public void execute() throws KonosException {
		File displayFile = CodeGenerationHelper.fileOf(displayFolder(gen(writer.target()), typeOf(element), writer.target()), displayName(false), writer.target());
		if (writer.target() != Target.Accessor && writer.target() != Target.Android && isRendered(element) && displayFile.exists()) return;
		super.execute();
	}

	protected String targetPackageName() {
		return targetPackageName(writer.target());
	}

	protected String targetPackageName(Target target) {
		if (target == Target.Android) return "mobile.android";
		if (target == Target.MobileShared) return "mobile";
		return "ui";
	}

	protected final void write(FrameBuilder builder) {
		writer.write(element, typeOf(element), builder);
	}

	private String displayName(boolean accessible) {
		final String suffix = accessible ? "Proxy" : "";
		final String abstractValue = accessible ? "" : (ElementHelper.isRoot(element) && hasAbstractClass(element, writer.target()) ? "Abstract" : "");
		return displayFilename(snakeCaseToCamelCase(abstractValue + firstUpperCase(element.name$())), suffix);
	}

	protected abstract Updater updater(String displayName, File sourceFile);

}
