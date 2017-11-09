package io.intino.konos.alexandria.activity.box.model.renders;

import io.intino.konos.alexandria.activity.box.model.ElementRender;
import io.intino.konos.alexandria.activity.box.model.Panel;

import java.util.List;

import static java.util.Collections.emptyList;

public class RenderPanels extends ElementRender {
	private Source source;

	public List<Panel> source() {
		return source != null ? source.panels() : emptyList();
	}

	public RenderPanels source(Source source) {
		this.source = source;
		return this;
	}

	public interface Source {
		List<Panel> panels();
	}
}
