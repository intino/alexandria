package io.intino.alexandria.ui.documentation.model.operation;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;
import io.intino.alexandria.ui.documentation.model.OperationWidget;

public class OpenBlockWidget extends OperationWidget {

	public OpenBlockWidget() {
		super("Allows opening conditional blocks.");
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("block", Property.Type.Reference, "Block to open after user clicks operation"));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
