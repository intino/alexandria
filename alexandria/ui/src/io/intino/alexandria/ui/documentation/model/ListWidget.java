package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;

import static java.util.Arrays.asList;

public class ListWidget extends BaseWidget {

	public ListWidget() {
		super("Render list of items of same type in a paged or infinite view.");
		facets(asList("Navigable", "Editable"));
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("sourceClass", Property.Type.Text, "Class responsible of loading items in list"));
		add(Model.property("itemClass", Property.Type.Text, "Class for items stored in list"));
		add(Model.property("noItemsMessage", Property.Type.Text, "Message if no items in list. By default, 'no items' message is shown."));
		add(Model.property("pageSize", Property.Type.Integer, "Count of items per page by default."));
		add(Model.property("scrollingMark", Property.Type.Text, "Used to optimize scrolling if views are slow-moving while rendering. A mark view is shown when scrolling."));
		add(Model.property("Mold", Property.Type.Object, "Defines the template used to render each item in list"));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
