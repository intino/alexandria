package io.intino.alexandria.ui.documentation.model.other;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;
import io.intino.alexandria.ui.documentation.model.BaseWidget;

import java.util.Arrays;

public class ChartWidget extends BaseWidget {

	public ChartWidget() {
		super("This widget allows rendering DataFrames by using ggplot from R language.");
		facets(Arrays.asList("Absolute", "Relative"));
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("operations", Property.Type.Text, "operations applied to chart"));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
