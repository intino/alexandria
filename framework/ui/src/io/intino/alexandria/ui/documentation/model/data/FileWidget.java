package io.intino.alexandria.ui.documentation.model.data;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;
import io.intino.alexandria.ui.documentation.model.BaseWidget;

import java.util.Collections;

import static io.intino.alexandria.ui.documentation.Model.method;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class FileWidget extends BaseWidget {

	public FileWidget() {
		super("This widget allows both uploading or preview files of different types.");
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
		addMethod(method("value", Collections.emptyList(), "Returns value stored in widget", "java.net.URL"));
		addMethod(method("update", singletonList(Model.methodParameter("value", "java.net.URL")), "Updates file value and refresh widget", "Void"));
	}

	protected void addEvents() {
		super.addEvents();
		addEvent(method("onChange", singletonList(Model.methodParameter("listener", "io.intino.alexandria.ui.displays.events.ChangeListener")), "This event is fired when widget value changes", "void").facets(singletonList("Editable")));
	}

}
