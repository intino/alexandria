package io.intino.konos.builder.codegeneration.action;

import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.model.graph.Parameter;
import io.intino.konos.model.graph.Sentinel;
import io.intino.magritte.framework.Concept;
import io.intino.magritte.framework.Node;

import java.util.Collections;
import java.util.stream.Collectors;

public class WebHookActionRenderer extends ActionRenderer {

	private final Sentinel.WebHook webHook;

	public WebHookActionRenderer(CompilationContext context, Sentinel.WebHook webHook) {
		super(context, "WebHook");
		this.webHook = webHook;
	}

	@Override
	protected void render() {
		final String name = firstUpperCase(webHook.name$());
		classes().put(webHook.getClass().getSimpleName() + "#" + name, "actions" + "." + name + "Action");
		execute(name, name, null, webHook.parameterList().stream().map(this::castParameters).collect(Collectors.toList()), Collections.emptyList(), webHook.graph().schemaList());
	}

	private Parameter castParameters(Sentinel.WebHook.Parameter p) {
		Concept concept = p.graph().core$().concept(Parameter.class);
		Node node = concept.createNode(p.name$(), p.graph().core$().model().core$());
		Parameter root = node.as(Parameter.class);
		root.core$().addAspect(Parameter.Text.class);
		root.isText();
		return root;
	}
}
