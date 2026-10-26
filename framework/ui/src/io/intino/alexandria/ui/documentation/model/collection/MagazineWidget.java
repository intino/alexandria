package io.intino.alexandria.ui.documentation.model.collection;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;
import io.intino.alexandria.ui.documentation.model.BaseWidget;

import static java.util.Arrays.asList;

public class MagazineWidget extends BaseWidget {

	public MagazineWidget() {
		super("Render items as a responsive collection of cards.");
		facets(asList("Navigable", "Selectable"));
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("sourceClass", Property.Type.Text, "Class responsible of loading magazine items."));
		add(Model.property("itemClass", Property.Type.Text, "Class for items rendered as magazine cards."));
		add(Model.property("noItemsMessage", Property.Type.Text, "Message shown when the magazine has no items."));
		add(Model.property("pageSize", Property.Type.Integer, "Count of items loaded per page by default."));
		add(Model.property("Mold", Property.Type.Object, "Defines the template used to render each magazine card."));
	}
}
