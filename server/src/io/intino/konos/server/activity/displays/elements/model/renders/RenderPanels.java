package io.intino.konos.server.activity.displays.elements.model.renders;

import io.intino.konos.server.activity.displays.elements.model.ElementRender;
import io.intino.konos.server.activity.displays.layouts.model.ElementOption;
import io.intino.konos.server.activity.displays.panels.model.Panel;

import java.util.List;

import static java.util.Collections.emptyList;

public class RenderPanels extends ElementRender {
	private Source source;

	public RenderPanels(ElementOption option) {
		super(option);
	}

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
