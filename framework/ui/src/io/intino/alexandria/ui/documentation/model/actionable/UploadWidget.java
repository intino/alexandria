package io.intino.alexandria.ui.documentation.model.actionable;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.model.ActionableWidget;

import static io.intino.alexandria.ui.documentation.Model.property;

public class UploadWidget extends ActionableWidget {

	public UploadWidget() {
		super("Allows uplodaing data to server.");
	}

	protected void addProperties() {
		super.addProperties();
		add(property("multipleSelection", Property.Type.Bool, "Used to determine if allows multiple files"));
		add(property("progress", Property.Type.Bool, "Used to determine whether to show file upload progress"));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
