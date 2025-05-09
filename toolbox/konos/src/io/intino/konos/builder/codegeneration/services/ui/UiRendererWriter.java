package io.intino.konos.builder.codegeneration.services.ui;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.OutputItem;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.CodeGenerationHelper;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.dsl.CatalogComponents;
import io.intino.konos.dsl.Display;
import io.intino.konos.dsl.HelperComponents;
import io.intino.konos.dsl.PassiveView;
import io.intino.magritte.framework.Layer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Logger;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.displayFilename;
import static io.intino.konos.builder.helpers.CodeGenerationHelper.fileOf;
import static io.intino.konos.builder.helpers.Commons.firstUpperCase;
import static io.intino.konos.builder.helpers.Commons.javaFile;
import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

public abstract class UiRendererWriter implements RendererWriter {
	protected CompilationContext context;
	protected Target target;

	public UiRendererWriter(CompilationContext context, Target target) {
		this.context = context;
		this.target = target;
	}

	public Target target() {
		return target;
	}

	protected File root() {
		return context.root(target);
	}

	protected File src() {
		return src(target);
	}

	protected File src(Target target) {
		return context.src(target);
	}

	protected File gen() {
		return context.gen(target);
	}

	protected File gen(Target target) {
		return context.gen(target);
	}

	protected File res() {
		return context.res(target);
	}

	protected String displayName(Layer element, boolean exposed) {
		final String suffix = exposed ? "Proxy" : "";
		final String abstractValue = exposed ? "" : (ElementHelper.isRoot(element) && hasAbstractClass(element) ? "Abstract" : "");
		return displayFilename(snakeCaseToCamelCase(abstractValue + firstUpperCase(element.name$())), suffix);
	}

	protected boolean hasAbstractClass(Layer element) {
		return CodeGenerationHelper.hasAbstractClass(element, target);
	}

	protected void writeFrame(File packageFolder, Layer element, String name, String text) {
		writeFrame(packageFolder, element, name, text, target);
	}

	protected void writeFrame(File packageFolder, Layer element, String name, String text, Target target) {
		try {
			packageFolder.mkdirs();
			File file = fileOf(packageFolder, name, target);
			Files.write(file.toPath(), text.getBytes(StandardCharsets.UTF_8));
			if (!target.equals(Target.Service)) return;
			context.compiledFiles().add(new OutputItem(context.sourceFileOf(element), javaFile(packageFolder, name).getAbsolutePath()));
		} catch (IOException e) {
			Logger.getGlobal().severe(e.getMessage());
		}
	}

	protected String nameOfPassiveViewFile(PassiveView element, Frame frame, String suffix) {
		return nameOfPassiveViewFile(element, isExposed(frame), suffix);
	}

	protected String nameOfPassiveViewFile(PassiveView element, boolean exposed, String suffix) {
		return snakeCaseToCamelCase(element.name$() + (exposed ? "Proxy" : "") + suffix);
	}

	protected boolean isExposed(FrameBuilder builder) {
		return builder.is("exposed");
	}

	protected boolean isExposed(Frame frame) {
		return frame.is("exposed");
	}

	protected boolean hasConcreteNotifier(PassiveView element) {
		if (element.i$(conceptOf(Display.Exposed.class))) return true;
		return !element.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class)) &&
				!element.i$(conceptOf(CatalogComponents.Table.class)) &&
				!element.i$(conceptOf(CatalogComponents.DynamicTable.class)) &&
				!element.i$(conceptOf(HelperComponents.Row.class)) &&
				!element.i$(conceptOf(io.intino.konos.dsl.Template.class));
	}

	protected boolean hasConcreteRequester(PassiveView element) {
		if (element.i$(conceptOf(Display.Exposed.class))) return true;
		return !element.i$(conceptOf(CatalogComponents.Moldable.Mold.Item.class)) &&
				!element.i$(conceptOf(CatalogComponents.Table.class)) &&
				!element.i$(conceptOf(CatalogComponents.DynamicTable.class)) &&
				!element.i$(conceptOf(HelperComponents.Row.class)) &&
				!element.i$(conceptOf(io.intino.konos.dsl.Template.class));
	}

}
