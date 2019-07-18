package io.intino.konos.builder.codegeneration.ui;

import io.intino.itrules.Template;
import io.intino.konos.model.graph.PassiveView;
import io.intino.tara.magritte.Layer;

public interface TemplateProvider {
	Template srcTemplate(Layer layer);
	Template genTemplate(Layer layer);
	Template notifierTemplate(PassiveView element);
	Template requesterTemplate(PassiveView element);
	Template pushRequesterTemplate(PassiveView element);
}
