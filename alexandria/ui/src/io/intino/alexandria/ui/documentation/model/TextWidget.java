package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;

import java.util.Collections;

import static io.intino.alexandria.ui.documentation.Model.method;
import static io.intino.alexandria.ui.documentation.Model.property;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class TextWidget extends BaseWidget {

	public TextWidget() {
		super();
		facets(asList("Multiple", "Code", "Editable"));
	}

	protected void addProperties() {
		super.addProperties();
		add(property("mode", Property.Type.Word, "Transforms text value by applying desired mode", "Uppercase", "Lowercase", "Capitalize", "Normal"));
		add(property("value", Property.Type.Text, "The initial text to display in the box, if any"));
		add(property("language", Property.Type.Word, "Language of code defined in widget.", languages()).facets(singletonList("Code")));
		add(property("prefix", Property.Type.Text, "Text to add before the value."));
		add(property("suffix", Property.Type.Text, "Text to add after the value."));
	}

	protected void addMethods() {
		super.addMethods();
		addMethod(method("value", Collections.emptyList(), "Returns value stored in widget", "String"));
		addMethod(method("update", singletonList(Model.methodParameter("value", "String")), "Allows updating text value", "String"));
	}

	protected void addEvents() {
		super.addEvents();
		addEvent(Model.method("onChange", singletonList(Model.methodParameter("listener", "io.intino.alexandria.ui.displays.events.ChangeListener")), "This event is fired when widget value changes", "void").facets(singletonList("Editable")));
		addEvent(Model.method("onKeyPress", singletonList(Model.methodParameter("listener", "io.intino.alexandria.ui.displays.events.KeyPressListener")), "This event is fired when widget value changes", "void").facets(singletonList("Editable")));
	}

	private String languages() {
		return "Html, Java, Javascript, R";
	}

}
