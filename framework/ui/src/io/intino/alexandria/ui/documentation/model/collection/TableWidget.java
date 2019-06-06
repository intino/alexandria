package io.intino.alexandria.ui.documentation.model.collection;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;
import io.intino.alexandria.ui.documentation.model.BaseWidget;

import static java.util.Arrays.asList;

public class TableWidget extends BaseWidget {

	public TableWidget() {
		super("Render table of items of same type in a navigable or infinite view.");
		facets(asList("Navigable", "Editable"));
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("modelClass", Property.Type.Text, "Class for items stored in table"));
		add(Model.property("noItemsMessage", Property.Type.Text, "Message if no items in table. By default, 'no items' message is shown."));
		add(Model.property("mold", Property.Type.Reference, "Mold used to render each item in table").facets(asList("List", "Grid", "Map", "Detail")));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
