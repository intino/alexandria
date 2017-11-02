package io.intino.konos.server.activity.displays.panels.model;

import io.intino.konos.server.activity.displays.elements.model.AbstractView;
import io.intino.konos.server.activity.displays.elements.model.Element;

import java.util.ArrayList;
import java.util.List;

public class Panel extends Element {
	public List<AbstractView> viewList = new ArrayList<>();

	public List<AbstractView> views() {
		return viewList;
	}

	public Panel add(View view) {
		this.viewList.add(view);
		return this;
	}
}
