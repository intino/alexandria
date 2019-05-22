package io.intino.alexandria.ui.documentation.model.collection;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;
import io.intino.alexandria.ui.documentation.model.BaseWidget;

import static java.util.Collections.singletonList;

public class SortingWidget extends BaseWidget {

	public SortingWidget() {
		super("Component used to sort items in collections");
		facets(singletonList("Labeled"));
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("label", Property.Type.Text, "Title for component. It is optional.").facets(singletonList("Labeled")));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
