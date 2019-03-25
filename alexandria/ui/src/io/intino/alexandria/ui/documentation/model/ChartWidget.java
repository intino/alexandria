package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.schemas.Widget;
import io.intino.alexandria.ui.documentation.Model;

public class ChartWidget extends Widget {

	public ChartWidget() {
		addProperties();
		addMethods();
		addEvents();
	}

	private void addProperties() {
		propertyList().add(Model.property("operations", Property.Type.Text, "operations applied to chart"));
	}

	private void addMethods() {
	}

	private void addEvents() {
	}
}
