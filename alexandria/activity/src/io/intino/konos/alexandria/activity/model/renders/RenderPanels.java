package io.intino.konos.alexandria.activity.model.renders;

import io.intino.konos.alexandria.activity.model.ElementRender;
import io.intino.konos.alexandria.activity.model.Panel;

import java.util.List;

import static java.util.Collections.emptyList;

public class RenderPanels extends ElementRender {
	private Source source;
	private Object object = null;

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

	public Object item() {
		return object;
	}

	public RenderPanels item(Object object) {
		this.object = object;
		return this;
	}
}
