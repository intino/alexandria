package io.intino.alexandria.ui.documentation.model.actionable;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.model.ActionableWidget;

import static io.intino.alexandria.ui.documentation.Model.property;

public class ExportWidget extends ActionableWidget {

	public ExportWidget() {
		super("Allows exporting data by defining a date range.");
	}

	protected void addProperties() {
		super.addProperties();
		add(property("from", Property.Type.Instant, "Default from range for export operation"));
		add(property("to", Property.Type.Instant, "Default to range for export operation"));
		add(property("options", Property.Type.TextList, "Used to define downloading alternatives"));
		add(property("min", Property.Type.Integer, "Minimum days allowed when selecting range"));
		add(property("max", Property.Type.Integer, "Maximum days allowed when selecting range"));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
