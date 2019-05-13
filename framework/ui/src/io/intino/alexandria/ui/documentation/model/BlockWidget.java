package io.intino.alexandria.ui.documentation.model;

import io.intino.alexandria.schemas.Property;
import io.intino.alexandria.ui.documentation.Model;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class BlockWidget extends BaseWidget {

	public BlockWidget() {
		super("With this widget you can organize your page in order to define the page layout.");
		facets(asList("Paper", "Badge", "Conditional", "Absolute", "Relative", "Parallax", "Stamp"));
	}

	protected void addProperties() {
		super.addProperties();
		add(Model.property("label", Property.Type.ReferenceArray, "Block title if any", layoutOptions()));
		add(Model.property("layout", Property.Type.ReferenceArray, "Layout used to define children location", layoutOptions()));
		add(Model.property("spacing", Property.Type.Reference, "Spacing used between children", spacingOptions()));
		add(Model.property("value", Property.Type.Integer, "Value for badge if block has Badge facet").facets(asList("badge")));
		add(Model.property("max", Property.Type.Integer, "Max value for badge. If value exceeds then value is collapsed").facets(asList("badge")));
		add(Model.property("showZero", Property.Type.Bool, "By default, badge is hidden if value is 0. This behavior is managed by this property").facets(asList("badge")));
		add(Model.property("mode", Property.Type.Word, "View used to render badge", "Normal", "Dot").facets(asList("badge")));
		add(Model.property("selected", Property.Type.Reference, "Block is visible only if Option is selected").facets(asList("Conditional")));
		add(Model.property("width", Property.Type.Integer, "Width of image in pixels with Absolute facet or percentage with Relative facet").facets(asList("Absolute", "Relative")));
		add(Model.property("height", Property.Type.Integer, "Height of image in pixels with Absolute facet or percentage with Relative facet").facets(asList("Absolute", "Relative")));
		add(Model.property("background", Property.Type.Text, "If parallax facet is enabled, this property defines the background to use for this effect. It must be a resource directory relative path.").facets(asList("Parallax")));
		add(Model.property("mold", Property.Type.Reference, "This property defines the mold to use for rendering block").facets(asList("Stamp")));
	}

	protected void addMethods() {
		super.addMethods();
		addMethod(Model.method("value", emptyList(), "Returns the badge value", "Integer").facets(singletonList("Badge")));
		addMethod(Model.method("update", singletonList(Model.methodParameter("value", "Integer")), "Updates the value for badge", "void").facets(singletonList("Badge")));
		addMethod(Model.method("isVisible", emptyList(), "Indicates if conditional block is visible", "Bool").facets(singletonList("Conditional")));
		addMethod(Model.method("isHidden", emptyList(), "Indicates if conditional block is hidden", "Bool").facets(singletonList("Conditional")));
		addMethod(Model.method("visible", singletonList(Model.methodParameter("value", "Bool")), "Sets if conditional block is visible or not", "Void").facets(singletonList("Conditional")));
		addMethod(Model.method("hidden", singletonList(Model.methodParameter("value", "Bool")), "Sets if conditional block is hidden or not", "Void").facets(singletonList("Conditional")));
		addMethod(Model.method("show", emptyList(), "Shows the conditional block", "Void").facets(singletonList("Conditional")));
		addMethod(Model.method("hide", emptyList(), "Hides the conditional block", "Void").facets(singletonList("Conditional")));
	}

	protected void addEvents() {
		super.addEvents();
		addEvent(Model.method("onShow", singletonList(Model.methodParameter("listener", "io.intino.alexandria.ui.displays.events.ShowListener")), "This event is fired when conditional block is shown", "void").facets(singletonList("Conditional")));
		addEvent(Model.method("onHide", singletonList(Model.methodParameter("listener", "io.intino.alexandria.ui.displays.events.HideListener")), "This event is fired when conditional block is hidden", "void").facets(singletonList("Conditional")));
	}

	private String[] layoutOptions() {
		return new String[] { "Horizontal", "HorizontalReverse", "Vertical", "VerticalReverse", "Center", "CenterJustified", "CenterCenter",
				"Flexible", "Justified", "StartJustified", "EndJustified", "AroundJustified", "Wrap", "NoWrap", "WrapReverse" };
	}

	private String[] spacingOptions() {
		return new String[] { "DP8", "DP16", "DP24", "DP32", "DP40", "None" };
	}

}
