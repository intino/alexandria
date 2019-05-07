package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;

public class OpenPageWidget extends OperationWidget {

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
