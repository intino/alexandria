package io.intino.konos.builder.codegeneration.ui;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.services.ui.Target;
import io.intino.konos.dsl.PassiveView;
import io.intino.magritte.framework.Layer;

public interface RendererWriter {
	//	Template srcTemplate(Layer layer, FrameBuilder builder);
//	Template genTemplate(Layer layer, FrameBuilder builder);
//	Template resTemplate(Layer layer, FrameBuilder builder);
//	Template notifierTemplate(PassiveView element, FrameBuilder builder);
//	Template requesterTemplate(PassiveView element, FrameBuilder builder);
//	Template pushRequesterTemplate(PassiveView element, FrameBuilder builder);
	Target target();

	boolean write(Layer element, String type, FrameBuilder builder);

	boolean writeNotifier(PassiveView element, FrameBuilder builder);

	boolean writeRequester(PassiveView element, FrameBuilder builder);

	boolean writePushRequester(PassiveView element, FrameBuilder builder);
}
