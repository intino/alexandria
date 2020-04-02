package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.Absolute;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Relative;

public class SizedRenderer<C extends Component> extends ComponentRenderer<C> {

	private static final String OffsetSize = "calc(%.0f%% - %dpx)";

	public SizedRenderer(CompilationContext compilationContext, C component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		addSize(result);
		return result;
	}

	private void addSize(FrameBuilder result) {
		if (element.i$(Relative.class)) {
			Relative abstractRelative = element.a$(Relative.class);
			result.add("width", relativeSizeOf(abstractRelative.width(), abstractRelative.offsetWidth()));
			result.add("height", relativeSizeOf(abstractRelative.height(), abstractRelative.offsetHeight()));
		} else if (element.i$(Absolute.class)) {
			Absolute abstractAbsolute = element.a$(Absolute.class);
			result.add("width", abstractAbsolute.width() + "px");
			result.add("height", abstractAbsolute.height() + "px");
		}
	}

	private String relativeSizeOf(double size, int offset) {
		return offset != -1 ? String.format(OffsetSize, size, offset) : size + "%";
	}
}