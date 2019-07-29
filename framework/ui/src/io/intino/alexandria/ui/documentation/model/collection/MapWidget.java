package io.intino.alexandria.ui.documentation.model.collection;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;
import io.intino.alexandria.ui.documentation.model.BaseWidget;

import static java.util.Arrays.asList;

public class MapWidget extends BaseWidget {

	public MapWidget() {
		super("Render list of items of same type in a map view.");
		facets(asList("Navigable", "Editable"));
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("sourceClass", Property.Type.Text, "Class responsible of loading items in list"));
		add(Model.property("itemClass", Property.Type.Text, "Class for items stored in list"));
		add(Model.property("noItemsMessage", Property.Type.Text, "Message if no items in list. By default, 'no items' message is shown."));
		add(Model.property("Mold", Property.Type.Object, "Defines the template used to render each item in list"));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
