package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class DateWidget extends BaseWidget {

	protected void addProperties() {
		super.addProperties();
		add(Model.property("pattern", Property.Type.Text, "pattern is defined using momentjs format", "DD/MM/YYYY"));
		add(Model.property("mode", Property.Type.Word, "used to...", "FromNow", "ToNow"));
	}

	protected void addMethods() {
		super.addMethods();
		add(Model.method("get", emptyList(), "returns ....", "java.time.Instant"));
		add(Model.method("update", singletonList(Model.methodParameter("instant", "java.time.Instant")), "updates ...", "void"));
	}

	protected void addEvents() {
		super.addEvents();
	}

}
