package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;

import java.util.Arrays;

import static java.util.Arrays.asList;

public class ActionableWidget extends BaseWidget {

	public ActionableWidget(String description) {
		super(description);
		facets(asList("Action", "OpenDrawer", "CloseDrawer", "OpenPage", "OpenSite", "OpenBlock", "OpenDialog", "CloseDialog", "Export", "Download"));
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("title", Property.Type.Text, "Title used to render operation"));
		add(Model.property("target", Property.Type.Word, "Class for items stored in list", "Blank", "Self", "Parent", "Top"));
		add(Model.property("icon", Property.Type.Text, "Icon used to render operation. If operation is of type MaterialIconButton, refer to: https://material.io/tools/icons, otherwise, define the resource path of your icon").facets(Arrays.asList("IconButton", "MaterialIconButton")));
		add(Model.property("confirmText", Property.Type.Text, "Shows a confirmation text to user before execute operation").facets(Arrays.asList("Confirmable")));
	}

	protected void addMethods() {
		super.addMethods();
	}

	protected void addEvents() {
		super.addEvents();
	}

}
