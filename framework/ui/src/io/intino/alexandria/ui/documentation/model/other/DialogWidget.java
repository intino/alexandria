package io.intino.alexandria.ui.documentation.model.other;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;
import io.intino.alexandria.ui.documentation.model.BaseWidget;

import java.util.Collections;

public class DialogWidget extends BaseWidget {

	public DialogWidget() {
		super("This widget allows creation of dialogs of different types.");
		facets(Collections.singletonList("Fullscreen"));
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("title", Property.Type.Text, "Dialog title"));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
