package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;

import static io.intino.alexandria.ui.documentation.Model.property;

public class DownloadWidget extends OperationWidget {

	public DownloadWidget() {
		super("Allows downloading data defined in server.");
	}

	protected void addProperties() {
		super.addProperties();
		add(property("options", Property.Type.TextList, "Used to define downloading alternatives"));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
