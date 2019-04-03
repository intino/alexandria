package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.Collection;
import org.siani.itrules.model.Frame;

public class CollectionRenderer extends SizedRenderer<Collection> {

	public CollectionRenderer(Settings settings, Collection component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("collection", "");
	}
}
