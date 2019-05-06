package io.intino.konos.builder.codegeneration.ui.displays.components.data;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.model.graph.DataComponents.File;
import org.siani.itrules.model.Frame;

public class FileRenderer extends SizedRenderer<File> {

	public FileRenderer(Settings settings, File component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		if (element.value() != null && !element.value().isEmpty()) result.addSlot("value", resourceMethodFrame("value", element.value()));
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("file", "");
	}
}
