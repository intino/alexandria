package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;

import java.util.Collections;

import static io.intino.alexandria.ui.documentation.Model.method;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class ImageWidget extends BaseWidget {

	public ImageWidget() {
		super();
		facets(asList("Avatar", "Absolute", "Relative"));
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("value", Property.Type.Text, "Image for widget. It must be a resource directory relative path to image."));
		add(Model.property("defaultValue", Property.Type.Text, "Default image for widget if value is not present."));
		add(Model.property("text", Property.Type.Text, "Text used to create avatar image. It uses initial letters of text.").facets(singletonList("Avatar")));
		add(Model.property("width", Property.Type.Integer, "Width of image in pixels with Absolute facet or percentage with Relative facet").facets(asList("Absolute", "Relative")));
		add(Model.property("height", Property.Type.Integer, "Height of image in pixels with Absolute facet or percentage with Relative facet").facets(asList("Absolute", "Relative")));
	}

	protected void addMethods() {
		super.addMethods();
		addMethod(method("value", Collections.emptyList(), "Returns value stored in widget", "java.net.URL"));
		addMethod(method("defaultValue", singletonList(Model.methodParameter("value", "java.net.URL")), "Sets the default image", "Void"));
		addMethod(method("update", singletonList(Model.methodParameter("value", "java.net.URL")), "Updates image value and refresh widget", "Void"));
	}

	protected void addEvents() {
		super.addEvents();
		addEvent(Model.method("onChange", singletonList(Model.methodParameter("listener", "io.intino.alexandria.ui.displays.events.ChangeListener")), "This event is fired when widget value changes", "void").facets(singletonList("Editable")));
	}

}
