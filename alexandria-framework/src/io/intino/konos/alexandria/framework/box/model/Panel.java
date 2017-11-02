package io.intino.konos.alexandria.framework.box.model;

import io.intino.konos.alexandria.framework.box.model.panel.View;

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
