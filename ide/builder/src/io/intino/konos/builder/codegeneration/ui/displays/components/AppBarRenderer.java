package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.ChildComponents.AppBar;
import org.siani.itrules.model.Frame;

public class AppBarRenderer extends ComponentRenderer<AppBar> {

	public AppBarRenderer(Settings settings, AppBar component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
	}

	@Override
	protected Frame properties() {
		Frame result = super.properties();
		result.addSlot("color", element.color().name().toLowerCase());
		result.addSlot("position", element.position().name().toLowerCase());
		return result;
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("appbar", "");
	}
}
