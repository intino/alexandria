package io.intino.konos.model.graph.functions;

import io.intino.konos.model.graph.Template;

import java.util.List;

@FunctionalInterface
public interface TemplatesProvider {

	public List<Template> templates();
}
