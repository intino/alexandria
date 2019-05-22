package io.intino.alexandria.ui.documentation.model.collection;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;
import io.intino.alexandria.ui.documentation.model.BaseWidget;

import static java.util.Collections.emptyList;

public class SearchBoxWidget extends BaseWidget {

	public SearchBoxWidget() {
		super("Component used to filter items in collections");
		facets(emptyList());
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("placeholder", Property.Type.Text, "Description rendered if no filter condition is defined."));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
