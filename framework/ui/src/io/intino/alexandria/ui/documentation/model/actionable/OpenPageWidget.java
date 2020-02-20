package io.intino.alexandria.ui.documentation.model.actionable;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;
import io.intino.alexandria.ui.documentation.model.ActionableWidget;

public class OpenPageWidget extends ActionableWidget {

	public OpenPageWidget() {
		super("Allows opening application pages.");
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("page", Property.Type.Reference, "Page to open after user clicks operation"));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
