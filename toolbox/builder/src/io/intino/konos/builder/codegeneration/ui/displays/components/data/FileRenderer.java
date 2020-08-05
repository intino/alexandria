package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.DataComponents;
import io.intino.konos.model.graph.DataComponents.File;

import java.util.stream.Collectors;

public class FileRenderer extends SizedRenderer<File> {

	public FileRenderer(CompilationContext compilationContext, File component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		if (element.value() != null && !element.value().isEmpty()) result.add("value", resourceMethodFrame("value", element.value()));
		if (element.isEditable() && element.asEditable().showPreview()) result.add("preview", true);
		if (element.isEditable() && element.asEditable().showDropZone()) result.add("dropZone", true);
		addValidation(result);
		return result;
	}

	private void addValidation(FrameBuilder frame) {
		if (!element.isEditable()) return;
		File.Editable.Validation validation = element.asEditable().validation();
		if (validation == null) return;
		frame.add("maxSize", validation.maxSize() * 1024);
		frame.add("allowedTypes", "\"" + validation.allowedTypes().stream().map(Enum::name).collect(Collectors.joining("\",\"")) + "\"");
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("file", "");
	}
}
