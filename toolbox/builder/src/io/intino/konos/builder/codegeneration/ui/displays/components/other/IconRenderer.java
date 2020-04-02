package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.OtherComponents.BaseIcon;
import io.intino.konos.model.graph.OtherComponents.Icon;

public class IconRenderer extends ComponentRenderer<BaseIcon> {

	public IconRenderer(CompilationContext compilationContext, BaseIcon component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
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
