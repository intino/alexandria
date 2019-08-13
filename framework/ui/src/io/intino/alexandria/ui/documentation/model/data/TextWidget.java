package io.intino.alexandria.ui.documentation.model.data;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;
import io.intino.alexandria.ui.documentation.model.BaseWidget;

import static io.intino.alexandria.ui.documentation.Model.method;
import static io.intino.alexandria.ui.documentation.Model.property;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
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
		addMethod(method("value", emptyList(), "Returns value stored in widget", "String"));
		addMethod(method("value", singletonList(Model.methodParameter("value", "String")), "Allows updating text value", "TextEditable").facets(singletonList("Editable")));
		addMethod(method("readonly", emptyList(), "Returns true if component is readonly", "Bool").facets(singletonList("Editable")));
		addMethod(method("readonly", singletonList(Model.methodParameter("value", "Bool")), "Allows updating readonly state. If true component is disabled for edition", "TextEditable").facets(singletonList("Editable")));
	}

	protected void addEvents() {
		super.addEvents();
		addEvent(method("onChange", singletonList(Model.methodParameter("listener", "io.intino.alexandria.ui.displays.events.ChangeListener")), "This event is fired when widget value changes", "TextEditable").facets(singletonList("Editable")));
		addEvent(method("onKeyPress", singletonList(Model.methodParameter("listener", "io.intino.alexandria.ui.displays.events.KeyPressListener")), "This event is fired when widget user press any key", "TextEditable").facets(singletonList("Editable")));
		addEvent(method("onEnterPress", singletonList(Model.methodParameter("listener", "io.intino.alexandria.ui.displays.events.KeyPressListener")), "This event is fired when user press 'Enter' key", "TextEditable").facets(singletonList("Editable")));
	}

	private String[] languages() {
		return new String[] { "Html", "Java", "Javascript", "R" };
	}

}
