package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.ui.RendererWriter;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.dsl.Absolute;
import io.intino.konos.dsl.Component;
import io.intino.konos.dsl.Relative;

import static io.intino.konos.builder.helpers.ElementHelper.conceptOf;

public class SizedRenderer<C extends Component> extends ComponentRenderer<C> {

	private static final String OffsetSize = "calc(%.0f%% - %dpx)";

	public SizedRenderer(CompilationContext compilationContext, C component, RendererWriter provider) {
		super(compilationContext, component, provider);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		addSize(result);
		return result;
	}

	private void addSize(FrameBuilder result) {
		if (element.i$(conceptOf(Relative.class))) {
			Relative abstractRelative = element.a$(Relative.class);
			result.add("width", relativeSizeOf(abstractRelative.width(), abstractRelative.offsetWidth()));
			result.add("height", relativeSizeOf(abstractRelative.height(), abstractRelative.offsetHeight()));
		} else if (element.i$(conceptOf(Absolute.class))) {
			Absolute abstractAbsolute = element.a$(Absolute.class);
			result.add("width", abstractAbsolute.width() + "px");
			result.add("height", abstractAbsolute.height() + "px");
		}
	}

	private String relativeSizeOf(double size, int offset) {
		return offset != -1 ? String.format(OffsetSize, size, offset) : size + "%";
	}

}