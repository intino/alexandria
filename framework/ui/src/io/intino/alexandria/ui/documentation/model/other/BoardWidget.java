package io.intino.alexandria.ui.documentation.model.other;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;
import io.intino.alexandria.ui.documentation.model.BaseWidget;

import java.util.Arrays;
import java.util.Collections;

public class BoardWidget extends BaseWidget {

	public BoardWidget() {
		super("This widget allows rendering a list of application links.");
		facets(Collections.emptyList());
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("tsv", Property.Type.Text, "TSV file that contains applications info: name, url"));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
