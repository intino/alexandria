package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.model.graph.OtherComponents.Selector;

public class SelectorRenderer extends ComponentRenderer<Selector> {

	public SelectorRenderer(CompilationContext compilationContext, Selector component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
	}

	@Override
	protected void fill(FrameBuilder builder) {
		super.fill(builder);
		addMethod(builder);
	}

	@Override
	public FrameBuilder properties() {
		FrameBuilder result = super.properties();
		result.add("multipleSelection", element.multipleSelection() ? "true" : "false");
		if (element.isReadonly()) result.add("readonly", element.isReadonly());
		if (element.isFocused()) result.add("readonly", element.isFocused());
		addMenuProperties(result);
		addComboBoxProperties(result);
		addRadioBoxProperties(result);
		addAddressableProperties(result);
		addToggleBoxProperties(result);
		return result;
	}

	private void addMenuProperties(FrameBuilder builder) {
		if (!element.isMenu()) return;
		Selector.Menu.Layout layout = element.asMenu().layout();
		if (layout == null) return;
		builder.add("layout", layout.name());
	}

	private void addComboBoxProperties(FrameBuilder builder) {
		if (!element.isComboBox()) return;
		String placeholder = element.asComboBox().placeholder();
		if (placeholder == null || placeholder.isEmpty()) return;
		builder.add("placeholder", placeholder);
	}

	private void addRadioBoxProperties(FrameBuilder builder) {
		if (!element.isRadioBox()) return;
		String selected = element.asRadioBox().selected();
		if (selected == null || selected.isEmpty()) return;
		builder.add("selected", selected);
	}

	private void addToggleBoxProperties(FrameBuilder builder) {
		if (!element.isToggleBox()) return;
		Selector.ToggleBox toggleBox = element.asToggleBox();
		String selected = toggleBox.selected();
		if (selected != null && !selected.isEmpty()) builder.add("selected", selected);
		Selector.ToggleBox.Layout layout = toggleBox.layout();
		builder.add("layout", layout.name());
		builder.add("size", toggleBox.size().name());
	}

	private void addMethod(FrameBuilder builder) {
		if (!element.isAddressable()) return;
		builder.add("methods", addressedMethod());
	}

	private FrameBuilder addressedMethod() {
		FrameBuilder result = addOwner(buildBaseFrame()).add("method").add(Selector.class.getSimpleName()).add("addressable");
		result.add("name", nameOf(element));
		return result;
	}

	private void addAddressableProperties(FrameBuilder builder) {
		if (!element.isAddressable()) return;
		Selector.Addressable addressable = element.asAddressable();
		String path = addressable.addressableResource().path();
		builder.add("path", path);
	}

	@Override
	protected String className(Class clazz) {
		return super.className(clazz).replace("selector", "");
	}
}
