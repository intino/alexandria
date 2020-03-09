package io.intino.konos.builder.codegeneration.ui;

import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
import io.intino.konos.model.graph.PassiveView;
import io.intino.magritte.framework.Layer;

public interface TemplateProvider {
	Template srcTemplate(Layer layer, FrameBuilder builder);
	Template genTemplate(Layer layer, FrameBuilder builder);
	Template notifierTemplate(PassiveView element, FrameBuilder builder);
	Template requesterTemplate(PassiveView element, FrameBuilder builder);
	Template pushRequesterTemplate(PassiveView element, FrameBuilder builder);
}
