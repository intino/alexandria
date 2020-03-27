package io.intino.konos.builder.codegeneration.action;

import io.intino.konos.builder.codegeneration.CompilationContext;
import io.intino.konos.model.graph.Sentinel;

import java.util.Collections;

public class WebHookActionRenderer extends ActionRenderer {

	private final Sentinel.WebHook webHook;

	public WebHookActionRenderer(CompilationContext context, Sentinel.WebHook webHook) {
		super(context, "WebHook");
		this.webHook = webHook;
	}

	@Override
	protected void render() {
		final String name = firstUpperCase(webHook.name$());
		classes().put(webHook.getClass().getSimpleName() + "#" + firstUpperCase(webHook.core$().owner().name()), "actions" + "." + name + "Action");
		execute(name, name, null, webHook.parameterList(), Collections.emptyList(), webHook.graph().schemaList());
	}
}
