package io.intino.alexandria.ui.documentation.model.other;

import io.intino.alexandria.ui.documentation.model.BaseWidget;

import java.util.Arrays;

public class SelectorWidget extends BaseWidget {

	public SelectorWidget() {
		super("This widget renders a list of options using style defined in facet.");
		facets(Arrays.asList("Menu", "RadioBox", "ListBox", "ComboBox", "Tabs"));
	}

	protected void addProperties() {
		super.addProperties();
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
