package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.schemas.Widget;
import io.intino.alexandria.ui.documentation.Model;

public class TextWidget extends Widget {
	public TextWidget() {
		addProperties();
		addMethods();
		addEvents();
	}

	private void addProperties() {
		propertyList().add(Model.property("format", Property.Type.Word, "used to...", "H1", "H2", "H3", "H4", "H5", "H6", "Subtitle1", "Subtitle2", "Body1", "Body2", "Button", "Caption", "Overline", "Default"));
		propertyList().add(Model.property("mode", Property.Type.Word, "used to...", "Uppercase", "Lowercase", "Capitalize", "Normal"));
		propertyList().add(Model.property("value", Property.Type.Text, "default value for component"));
	}

	private void addMethods() {
	}

	private void addEvents() {
	}
}
