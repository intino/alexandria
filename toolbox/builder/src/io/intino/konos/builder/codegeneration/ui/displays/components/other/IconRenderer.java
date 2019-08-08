package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.model.graph.OperationComponents.Operation;
import io.intino.konos.model.graph.OtherComponents.BaseIcon;
import io.intino.konos.model.graph.OtherComponents.Icon;

public class IconRenderer<O extends Operation> extends ComponentRenderer<BaseIcon> {

	public IconRenderer(Settings settings, BaseIcon component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();

		if (element.icon() != null && !element.icon().isEmpty()) {
			Object content = element.i$(Icon.class) ? resourceMethodFrame("icon", element.icon()) : element.icon();
			properties.add("icon", content);
		}

		return properties;
	}

}
