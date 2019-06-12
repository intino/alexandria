package io.intino.alexandria.ui.documentation.model.other;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;
import io.intino.alexandria.ui.documentation.model.BaseWidget;

public class DialogWidget extends BaseWidget {

	public DialogWidget() {
		super("This widget allows creation of dialogs of different types.");
//		facets(asList("Paper", "Badge", "Conditional", "Absolute", "Relative", "Parallax", "Stamp", "Animated"));
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
