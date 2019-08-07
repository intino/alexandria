package io.intino.alexandria.ui.documentation.model.operation;

import io.intino.alexandria.schemas.Property;

import static io.intino.alexandria.ui.documentation.Model.property;

public class DownloadSelectionWidget extends SelectionOperationWidget {

	public DownloadSelectionWidget() {
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
