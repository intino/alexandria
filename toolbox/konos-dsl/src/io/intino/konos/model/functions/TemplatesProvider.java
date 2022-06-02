package io.intino.konos.model.functions;

import io.intino.konos.model.Template;

import java.util.List;

@FunctionalInterface
public interface TemplatesProvider {
	List<Template> templates();
}
