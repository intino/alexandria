package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;

import java.util.Collections;

import static io.intino.alexandria.ui.documentation.Model.method;
import static java.util.Collections.singletonList;

public class NumberWidget extends BaseWidget {

	public NumberWidget() {
		super();
		facets(singletonList("Editable"));
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("value", Property.Type.Text, "The initial number to display in the number widget."));
		add(Model.property("min", Property.Type.Text, "The minimum number that can be selected."));
		add(Model.property("max", Property.Type.Text, "The max number that can be selected."));
		add(Model.property("step", Property.Type.Text, "The amount to increment the value by when a user clicks up or down on the scroll bar."));
	}

	protected void addMethods() {
		super.addMethods();
		add(method("value", Collections.emptyList(), "Returns value stored in widget", "java.lang.Double"));
		add(method("update", singletonList(Model.methodParameter("value", "java.lang.Double")), "Updates number value and refresh widget", "Void"));
	}

	protected void addEvents() {
		super.addEvents();
	}

}
