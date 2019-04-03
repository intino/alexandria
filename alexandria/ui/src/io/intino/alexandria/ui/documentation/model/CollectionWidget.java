package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;

import static java.util.Arrays.asList;

public class CollectionWidget extends BaseWidget {

	public CollectionWidget() {
		super();
		facets(asList("List", "Grid", "Table", "Map", "Detail"));
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("modelClass", Property.Type.Text, "Class for items stored in collection"));
		add(Model.property("noItemsMessage", Property.Type.Text, "Message if no items in collection. By default, 'no items' message is shown."));
		add(Model.property("mold", Property.Type.Reference, "Mold used to render each item in collection").facets(asList("List", "Grid", "Map", "Detail")));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
