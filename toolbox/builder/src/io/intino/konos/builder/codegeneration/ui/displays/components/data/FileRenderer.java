package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.model.graph.DataComponents.File;

public class FileRenderer extends SizedRenderer<File> {

	public FileRenderer(Settings settings, File component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		if (element.value() != null && !element.value().isEmpty()) result.add("value", resourceMethodFrame("value", element.value()));
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("file", "");
	}
}
