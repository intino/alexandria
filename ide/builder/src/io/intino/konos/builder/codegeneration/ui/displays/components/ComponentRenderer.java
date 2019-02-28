package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayRenderer;
import io.intino.konos.model.graph.Block;
import io.intino.konos.model.graph.ChildComponents.*;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Editable;
import io.intino.konos.model.graph.checkbox.childcomponents.CheckBoxSelector;
import io.intino.konos.model.graph.code.childcomponents.CodeText;
import io.intino.konos.model.graph.combobox.childcomponents.ComboBoxSelector;
import io.intino.konos.model.graph.menu.childcomponents.MenuSelector;
import io.intino.konos.model.graph.radiobox.childcomponents.RadioBoxSelector;
import io.intino.konos.model.graph.selection.SelectionBlock;
import org.siani.itrules.model.Frame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ComponentRenderer<C extends Component> extends DisplayRenderer<C> {
	private boolean buildChildren = false;
	private boolean decorated;
	private static final ComponentRendererFactory factory = new ComponentRendererFactory();

	public ComponentRenderer(Settings settings, C component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
		this.decorated = component.isDecorated();
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame().addTypes("component");
		frame.addSlot("id", shortId(element));
		frame.addSlot("properties", properties());
		if (buildChildren) frame.addTypes("child");
		addComponents(element, frame);
		addReferences(element, frame);
		addFacets(element, frame);
		addImplements(element, frame);
		return frame;
	}

	public void buildChildren(boolean value) {
		this.buildChildren = value;
	}

	public void decorated(boolean value) {
		this.decorated = value;
	}

	@Override
	protected void addDecoratedFrames(Frame frame) {
		addDecoratedFrames(frame, decorated);
	}

	private void addComponents(Component component, Frame frame) {
		List<Component> components = components(component);
		components.forEach(c -> {
			Frame componentFrame = buildChildren ? childFrame(c) : componentFrame(c);
			frame.addSlot( "component", componentFrame);
		});
	}

	private void addReferences(Component component, Frame frame) {
		List<Component> components = component.components();
		components.forEach(c -> frame.addSlot( "reference", referenceFrame(c)));
	}

	private Frame referenceFrame(Component component) {
		Frame frame = new Frame("reference").addSlot("name", component.name$());
		frame.addSlot("box", boxName());
		frame.addSlot("id", shortId(component));
		frame.addSlot("properties", properties(component));
		addFacets(component, frame);
		return frame;
	}

	@Override
	protected Frame componentFrame(Component component) {
		return componentRenderer(component).buildFrame();
	}

	protected String className(Class clazz) {
		return clazz.getSimpleName().toLowerCase();
	}

	protected Frame childFrame(Component component) {
		Frame result = componentRenderer(component).buildFrame();
		String[] ancestors = ancestors(component);
		result.addSlot("ancestors", ancestors);
		result.addSlot("ancestorsNotMe", Arrays.copyOfRange(ancestors, 1, ancestors.length));
		result.addSlot("value", componentRenderer(component).buildFrame().addSlot("addType", typeOf(component)));
		return result;
	}

	protected Frame properties() {
		return properties(element);
	}

	protected Frame properties(Component component) {
		Frame result = new Frame().addTypes("properties", typeOf(component));
		if (component.label() != null) result.addSlot("label", component.label());
		if (component.style() != null) result.addSlot("style", component.style().name$());
		return result;
	}

	private void addFacets(Component component, Frame result) {
		if (component.i$(Editable.class)) result.addSlot("facet", new Frame("facet").addSlot("name", Editable.class.getSimpleName()));
		if (component.i$(CodeText.class)) result.addSlot("facet", new Frame("facet").addSlot("name", CodeText.class.getSimpleName().replace("Text", "")));
		if (component.i$(SelectionBlock.class)) result.addSlot("facet", new Frame("facet").addSlot("name", SelectionBlock.class.getSimpleName().replace("Block", "")));
		if (component.i$(MenuSelector.class)) result.addSlot("facet", new Frame("facet").addSlot("name", MenuSelector.class.getSimpleName().replace("Selector", "")));
		if (component.i$(ComboBoxSelector.class)) result.addSlot("facet", new Frame("facet").addSlot("name", ComboBoxSelector.class.getSimpleName().replace("Selector", "")));
		if (component.i$(RadioBoxSelector.class)) result.addSlot("facet", new Frame("facet").addSlot("name", RadioBoxSelector.class.getSimpleName().replace("Selector", "")));
		if (component.i$(CheckBoxSelector.class)) result.addSlot("facet", new Frame("facet").addSlot("name", CheckBoxSelector.class.getSimpleName().replace("Selector", "")));
	}

	private String[] ancestors(Component component) {
		List<String> result = new ArrayList<>();
		Component parent = component.core$().ownerAs(Component.class);
		while (parent != null) {
			result.add(0, clean(parent.name$()));
			parent = parent.core$().ownerAs(Component.class);
		}
		return result.toArray(new String[0]);
	}

	private UIRenderer componentRenderer(Component component) {
		ComponentRenderer renderer = factory.renderer(settings, component, templateProvider, target);
		renderer.buildChildren(true);
		renderer.decorated(decorated);
		return renderer;
	}

	private List<Component> components(Component component) {
		List<Component> components = new ArrayList<>();
		if (component.i$(Block.class)) components.addAll(component.a$(Block.class).componentList());
		if (component.i$(Panels.class)) components.addAll(component.a$(Panels.class).panelList().stream().map(p -> p.a$(Component.class)).collect(toList()));
		if (component.i$(Panel.class)) components.addAll(component.a$(Panel.class).componentList());
		if (component.i$(Tabs.class)) components.addAll(component.a$(Tabs.class).tabList().stream().map(t -> t.a$(Component.class)).collect(toList()));
		if (component.i$(Snackbar.class)) components.addAll(component.a$(Snackbar.class).componentList());
		if (component.i$(Header.class)) components.addAll(component.a$(Header.class).componentList());
		if (component.i$(Content.class)) components.addAll(component.a$(Content.class).componentList());
		if (component.i$(Selector.class)) components.addAll(component.a$(Selector.class).componentList());
		return components;
	}

	private void addImplements(C element, Frame frame) {
		if (!element.isOption()) return;
		frame.addSlot("implements", new Frame("implements", "option").addSlot("option", ""));
	}

}
