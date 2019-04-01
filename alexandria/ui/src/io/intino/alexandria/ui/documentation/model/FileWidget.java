package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;

import java.util.Collections;

import static io.intino.alexandria.ui.documentation.Model.method;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class FileWidget extends BaseWidget {

	public FileWidget() {
		super();
		facets(asList("Absolute", "Relative"));
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("value", Property.Type.Text, "File for widget. It must be a resource directory relative path to file."));
		add(Model.property("width", Property.Type.Integer, "Width of preview visor in pixels with Absolute facet or percentage with Relative facet").facets(asList("Absolute", "Relative")));
		add(Model.property("height", Property.Type.Integer, "Height of preview visor in pixels with Absolute facet or percentage with Relative facet").facets(asList("Absolute", "Relative")));
	}

	protected void addMethods() {
		super.addMethods();
		add(method("value", Collections.emptyList(), "Returns value stored in widget", "java.net.URL"));
		add(method("update", singletonList(Model.methodParameter("value", "java.net.URL")), "Updates file value and refresh widget", "Void"));
	}

	protected void addEvents() {
		super.addEvents();
	}

}
