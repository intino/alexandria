package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Event;
import io.intino.alexandria.schemas.Method;
import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.schemas.Widget;

import java.util.Collections;

import static io.intino.alexandria.ui.documentation.Model.method;
import static io.intino.alexandria.ui.documentation.Model.property;

public abstract class BaseWidget extends Widget {

	public BaseWidget() {
		addProperties();
		addMethods();
		addEvents();
	}

	protected void addProperties() {
		add(property("color", Property.Type.Text, "Defines the color for widget"));
		add(property("format", Property.Type.ReferenceArray, "Add formats to widget. Formats must be declared"));
	}

	protected void addMethods() {
		add(method("refresh", Collections.emptyList(),"Refresh client side widget", "void"));
	}

	protected void addEvents() {
	}

	protected void add(Property property) {
		propertyList().add(property);
	}

	protected void add(Method method) {
		methodList().add(method);
	}

	protected void add(Event event) {
		eventList().add(event);
	}

}
