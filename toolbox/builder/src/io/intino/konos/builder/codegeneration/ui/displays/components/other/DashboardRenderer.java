package io.intino.konos.builder.codegeneration.ui.displays.components.other;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.displays.components.SizedRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.VisualizationComponents.Dashboard;

public class DashboardRenderer extends SizedRenderer<Dashboard> {

	public DashboardRenderer(CompilationContext compilationContext, Dashboard component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		addShinyFacet(result);
		addMetabaseFacet(result);
		element.parameterList().forEach(p -> result.add("parameter", parameterMethodFrame(p.name(), p.value())));
		return result;
	}

	private void addShinyFacet(FrameBuilder builder) {
		if (!element.isShiny()) return;
		builder.add("shiny");
		Dashboard.Shiny shiny = element.asShiny();
		if (shiny.serverScript() != null && !shiny.serverScript().isEmpty()) builder.add("serverScript", resourceMethodFrame("serverScript", shiny.serverScript()));
		if (shiny.uiScript() != null && !shiny.uiScript().isEmpty()) builder.add("uiScript", resourceMethodFrame("uiScript", shiny.uiScript()));
		shiny.resources().forEach(r -> builder.add("resource", resourceMethodFrame("add", r)));
	}

	private void addMetabaseFacet(FrameBuilder builder) {
		if (!element.isMetabase()) return;
		builder.add("metabase");
		Dashboard.Metabase metabase = element.asMetabase();
		builder.add("url", metabase.url());
		builder.add("secretKey", metabase.secretKey());
		builder.add("bordered", metabase.bordered());
		builder.add("titled", metabase.titled());
		builder.add("theme", metabase.theme().name());
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("dashboard", "");
	}
}
