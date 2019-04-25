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
		add(Model.property("modelClass", Property.Type.Text, "Class for items stored in list"));
		add(Model.property("noItemsMessage", Property.Type.Text, "Message if no items in list. By default, 'no items' message is shown."));
		add(Model.property("mold", Property.Type.Reference, "Mold used to render each item in list").facets(asList("List", "Grid", "Map", "Detail")));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
