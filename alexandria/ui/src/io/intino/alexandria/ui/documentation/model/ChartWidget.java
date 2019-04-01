package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;

public class ChartWidget extends BaseWidget {

	protected void addProperties() {
		super.addProperties();
		add(Model.property("operations", Property.Type.Text, "operations applied to chart"));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
