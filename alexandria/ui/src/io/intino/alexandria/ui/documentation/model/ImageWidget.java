package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.schemas.Widget;
import io.intino.alexandria.ui.documentation.Model;

public class ImageWidget extends Widget {
	public ImageWidget() {
		addProperties();
		addMethods();
		addEvents();
	}

	private void addProperties() {
		propertyList().add(Model.property("default", Property.Type.Resource, "default value for component"));
		propertyList().add(Model.property("value", Property.Type.Resource, "value for component"));
	}

	private void addMethods() {
	}

	private void addEvents() {
	}
}
