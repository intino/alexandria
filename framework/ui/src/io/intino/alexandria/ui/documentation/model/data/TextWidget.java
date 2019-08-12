package io.intino.alexandria.ui.documentation.model.data;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;
import io.intino.alexandria.ui.documentation.model.BaseWidget;

import java.util.Collections;

import static io.intino.alexandria.ui.documentation.Model.method;
import static io.intino.alexandria.ui.documentation.Model.property;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class TextWidget extends BaseWidget {

	public TextWidget() {
		super("Use this widget to present text content in your user interface");
		facets(asList("Multiple", "Code", "Editable", "Highlighted"));
	}

	protected void addProperties() {
		super.addProperties();
		add(property("mode", Property.Type.Word, "Transforms text value by applying desired mode", "Uppercase", "Lowercase", "Capitalize", "Normal"));
		add(property("value", Property.Type.Text, "The initial text to display in the box, if any"));
		add(property("language", Property.Type.Word, "Language of code defined in widget.", languages()).facets(singletonList("Code")));
		add(property("prefix", Property.Type.Text, "Text to add before the value."));
		add(property("suffix", Property.Type.Text, "Text to add after the value."));
		add(property("textColor", Property.Type.Text, "Color for text.").facets(singletonList("Highlight")));
		add(property("backgroundColor", Property.Type.Text, "Color for background.").facets(singletonList("Highlight")));
		add(property("noItemsMessage", Property.Type.Text, "Text rendered when no elements are added to component.").facets(singletonList("Multiple")));
		add(property("arrangement", Property.Type.Word, "Text rendered when no elements are added to component.", multipleArrangements()).facets(singletonList("Multiple")));
		add(property("spacing", Property.Type.Word, "Spacing between text components applied.", multipleSpacings()).facets(singletonList("Multiple")));
	}

	protected void addMethods() {
		super.addMethods();
		addMethod(method("value", Collections.emptyList(), "Returns value stored in widget", "String"));
		addMethod(method("update", singletonList(Model.methodParameter("value", "String")), "Allows updating text value", "String"));
	}

	protected void addEvents() {
		super.addEvents();
		addEvent(method("onChange", singletonList(Model.methodParameter("listener", "io.intino.alexandria.ui.displays.events.ChangeListener")), "This event is fired when widget value changes", "void").facets(singletonList("Editable")));
		addEvent(method("onKeyPress", singletonList(Model.methodParameter("listener", "io.intino.alexandria.ui.displays.events.KeyPressListener")), "This event is fired when widget value changes", "void").facets(singletonList("Editable")));
	}

	private String[] languages() {
		return new String[] { "Html", "Java", "Javascript", "R" };
	}

}
