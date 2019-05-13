package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.AbstractAbsolute;
import io.intino.konos.model.graph.AbstractRelative;
import io.intino.konos.model.graph.Component;
import org.siani.itrules.model.Frame;

public class SizedRenderer<C extends Component> extends ComponentRenderer<C> {

	public SizedRenderer(Settings settings, C component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public Frame properties() {
		Frame result = super.properties();
		addSize(result);
		return result;
	}

	private void addSize(Frame result) {
		if (element.i$(AbstractRelative.class)) {
			AbstractRelative abstractRelative = element.a$(AbstractRelative.class);
			result.addSlot("width", abstractRelative.width() + "%");
			result.addSlot("height", abstractRelative.height() + "%");
		} else if (element.i$(AbstractAbsolute.class)) {
			AbstractAbsolute abstractAbsolute = element.a$(AbstractAbsolute.class);
			result.addSlot("width", abstractAbsolute.width() + "px");
			result.addSlot("height", abstractAbsolute.height() + "px");
		}
	}

}