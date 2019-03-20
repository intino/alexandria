package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.schemas.Widget;
import io.intino.alexandria.ui.documentation.Model;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class DateWidget extends Widget {

	public DateWidget() {
		addProperties();
		addMethods();
		addEvents();
	}

	private void addProperties() {
		propertyList().add(Model.property("pattern", Property.Type.Text, "pattern is defined using momentjs format", "DD/MM/YYYY"));
		propertyList().add(Model.property("mode", Property.Type.Word, "used to...", "FromNow", "ToNow"));
	}

	private void addMethods() {
		methodList().add(Model.method("get", emptyList(), "returns ....", "java.time.Instant"));
		methodList().add(Model.method("update", singletonList(Model.methodParameter("instant", "java.time.Instant")), "updates ...", "void"));
	}

	private void addEvents() {
	}
}
