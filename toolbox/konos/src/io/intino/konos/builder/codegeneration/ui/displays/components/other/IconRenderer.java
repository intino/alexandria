package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.codegeneration.ui.displays.components.ComponentRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.OtherComponents.BaseIcon;
import io.intino.konos.dsl.OtherComponents.Icon;

import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

public class IconRenderer extends ComponentRenderer<BaseIcon> {

	public IconRenderer(CompilationContext compilationContext, BaseIcon component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder properties = super.properties();

		if (element.icon() != null && !element.icon().isEmpty()) {
			Object content = element.i$(conceptOf(Icon.class)) ? resourceMethodFrame("icon", element.icon()) : element.icon();
			properties.add("icon", content);
		}

		return properties;
	}

}
