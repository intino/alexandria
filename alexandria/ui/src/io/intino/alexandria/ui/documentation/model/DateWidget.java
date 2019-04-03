package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;

import static io.intino.alexandria.ui.documentation.Model.property;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class DateWidget extends BaseWidget {

	public DateWidget() {
		super();
		facets(asList("Multiple", "Code", "Editable"));
	}

	protected void addProperties() {
		super.addProperties();
		add(property("value", Property.Type.Text, "The initial text to display in the box, if any"));
		add(property("pattern", Property.Type.Text, "Pattern is defined using momentjs format", "DD/MM/YYYY"));
		add(property("mode", Property.Type.Word, "Defines date value with a relative time. Displays the date as the time from or to now", "FromNow", "ToNow"));
		add(property("timePicker", Property.Type.Bool, "Defines if the edition component allows time selection").facets(singletonList("Editable")));
		add(property("min", Property.Type.Instant, "Defines the min instant allowed").facets(singletonList("Editable")));
		add(property("max", Property.Type.Instant, "Defines the max instant allowed").facets(singletonList("Editable")));
		add(property("mask", Property.Type.Text, "Defines the mask to show for user when editing. Example: '__/__/____'").facets(singletonList("Editable")));
	}

	protected void addMethods() {
		super.addMethods();
		addMethod(Model.method("get", emptyList(), "Returns the value that the widget stores", "java.time.Instant"));
		addMethod(Model.method("update", singletonList(Model.methodParameter("instant", "java.time.Instant")), "Updates date widget with new instant value", "void"));
	}

	protected void addEvents() {
		super.addEvents();
		addEvent(Model.method("onChange", singletonList(Model.methodParameter("listener", "io.intino.alexandria.ui.displays.events.ChangeListener")), "This event is fired when widget value changes", "void").facets(singletonList("Editable")));
	}

}
