package io.intino.alexandria.ui.documentation.model.actionable;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.model.ActionableWidget;

import static io.intino.alexandria.ui.documentation.Model.property;

public class DownloadWidget extends ActionableWidget {

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
