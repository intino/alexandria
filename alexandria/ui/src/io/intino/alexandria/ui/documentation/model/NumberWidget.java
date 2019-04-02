package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;

import java.util.Collections;

import static io.intino.alexandria.ui.documentation.Model.method;
import static io.intino.alexandria.ui.documentation.Model.property;
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
		add(property("prefix", Property.Type.Text, "Text to add before the value."));
		add(property("suffix", Property.Type.Text, "Text to add after the value."));
	}

	protected void addMethods() {
		super.addMethods();
		addMethod(method("value", Collections.emptyList(), "Returns value stored in widget", "java.lang.Double"));
		addMethod(method("update", singletonList(Model.methodParameter("value", "java.lang.Double")), "Updates number value and refresh widget", "Void"));
	}

	protected void addEvents() {
		super.addEvents();
		addEvent(Model.method("onChange", singletonList(Model.methodParameter("listener", "io.intino.alexandria.ui.displays.events.ChangeListener")), "This event is fired when widget value changes", "void").facets(singletonList("Editable")));
	}

}
