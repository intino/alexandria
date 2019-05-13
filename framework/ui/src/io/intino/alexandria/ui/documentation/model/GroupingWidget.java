package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;

import static java.util.Collections.singletonList;

public class GroupingWidget extends BaseWidget {

	public GroupingWidget() {
		super("Component used to filter collections with items that are classified in groups");
		facets(singletonList("Labeled"));
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("label", Property.Type.Text, "Title for component. It is optional.").facets(singletonList("Labeled")));
		add(Model.property("position", Property.Type.Word, "Title position", "Top", "Bottom", "Left", "Right").facets(singletonList("Labeled")));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
