package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Method;
import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.schemas.Widget;

import java.util.Collections;

import static io.intino.alexandria.ui.documentation.Model.method;
import static io.intino.alexandria.ui.documentation.Model.property;

public abstract class BaseWidget extends Widget {

	public BaseWidget(String description) {
		description(description);
		addProperties();
		addMethods();
		addEvents();
	}

	protected void addProperties() {
		add(property("color", Property.Type.Text, "Defines the color for widget"));
		add(property("format", Property.Type.ReferenceArray, "Add formats to widget. Formats must be declared"));
	}

	protected void addMethods() {
		addMethod(method("refresh", Collections.emptyList(),"Refresh client side widget", "void"));
	}

	protected void addEvents() {
	}

	protected void add(Property property) {
		propertyList().add(property);
	}

	protected void addMethod(Method method) {
		methodList().add(method);
	}

	protected void addEvent(Method method) {
		eventList().add(method);
	}
}
