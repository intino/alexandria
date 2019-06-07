package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.AbstractAbsolute;
import io.intino.konos.model.graph.AbstractRelative;
import io.intino.konos.model.graph.Component;

public class SizedRenderer<C extends Component> extends ComponentRenderer<C> {

	private static final String OffsetSize = "calc(%.0f%% - %dpx)";

	public SizedRenderer(Settings settings, C component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		addSize(result);
		return result;
	}

	private void addSize(FrameBuilder result) {
		if (element.i$(AbstractRelative.class)) {
			AbstractRelative abstractRelative = element.a$(AbstractRelative.class);
			result.add("width", relativeSizeOf(abstractRelative.width(), abstractRelative.offsetWidth()));
			result.add("height", relativeSizeOf(abstractRelative.height(), abstractRelative.offsetHeight()));
		} else if (element.i$(AbstractAbsolute.class)) {
			AbstractAbsolute abstractAbsolute = element.a$(AbstractAbsolute.class);
			result.add("width", abstractAbsolute.width() + "px");
			result.add("height", abstractAbsolute.height() + "px");
		}
	}

	private String relativeSizeOf(double size, int offset) {
		return offset != -1 ? String.format(OffsetSize, size, offset) : size + "%";
	}
}