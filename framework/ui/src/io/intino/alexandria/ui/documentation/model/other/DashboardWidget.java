package io.intino.alexandria.ui.documentation.model.other;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;
import io.intino.alexandria.ui.documentation.model.BaseWidget;

import java.util.Arrays;

public class DashboardWidget extends BaseWidget {

	public DashboardWidget() {
		super("This widget allows rendering Dashboards.");
		facets(Arrays.asList("Absolute", "Relative"));
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("script", Property.Type.Text, "R executable program that contains the Dashboard"));
		add(Model.property("parameters", Property.Type.TextList, "parameters applied to dashboard"));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
