package io.intino.konos.dsl.functions;

import io.intino.konos.dsl.Template;

import java.util.List;

@FunctionalInterface
public interface TemplatesProvider {
	List<Template> templates();
}
