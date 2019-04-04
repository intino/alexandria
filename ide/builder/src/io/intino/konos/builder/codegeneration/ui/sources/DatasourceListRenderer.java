package io.intino.konos.builder.codegeneration.ui.sources;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.model.graph.Datasource;
import io.intino.konos.model.graph.KonosGraph;

import java.util.List;

@SuppressWarnings("Duplicates")
public class DatasourceListRenderer extends UIRenderer {
	private final List<Datasource> sources;
	private final TemplateProvider templateProvider;

	public DatasourceListRenderer(Settings settings, KonosGraph graph, TemplateProvider templateProvider, Target target) {
		super(settings, target);
		this.sources = graph.datasourceList();
		this.templateProvider = templateProvider;
	}

	public void execute() {
		sources.forEach(s -> new DatasourceRenderer(settings, s, templateProvider, target).execute());
	}

}