package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayRenderer;
import io.intino.konos.model.graph.Block;
import io.intino.konos.model.graph.ChildComponents.*;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.Editable;
import io.intino.konos.model.graph.checkbox.childcomponents.CheckBoxSelector;
import io.intino.konos.model.graph.code.childcomponents.CodeText;
import io.intino.konos.model.graph.combobox.childcomponents.ComboBoxSelector;
import io.intino.konos.model.graph.instance.InstanceBlock;
import io.intino.konos.model.graph.instancecollection.InstanceCollectionBlock;
import io.intino.konos.model.graph.menu.childcomponents.MenuSelector;
import io.intino.konos.model.graph.radiobox.childcomponents.RadioBoxSelector;
import io.intino.konos.model.graph.selection.SelectionBlock;
import org.jetbrains.annotations.NotNull;
import org.siani.itrules.model.Frame;

import java.util.*;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static java.util.stream.Collectors.toList;

public class ComponentRenderer<C extends Component> extends DisplayRenderer<C> {
	private boolean buildChildren = false;
	private boolean decorated;
	private Display owner;
	private static final ComponentRendererFactory factory = new ComponentRendererFactory();

	public ComponentRenderer(Settings settings, C component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
		this.decorated = component.isDecorated();
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame().addTypes("component");
		if (owner != null) frame.addSlot("owner", (owner.isDecorated() ? "Abstract" : "") + firstUpperCase(owner.name$()));
		frame.addSlot("properties", properties());
		if (buildChildren) frame.addTypes("child");
		addSpecificTypes(frame);
		addComponents(element, frame);
		addReferences(element, frame);
		addFacets(element, frame);
		addExtends(element, frame);
		addImplements(element, frame);
		return frame;
	}

	public void buildChildren(boolean value) {
		this.buildChildren = value;
	}

	public void decorated(boolean value) {
		this.decorated = value;
	}

	public void owner(Display owner) {
		this.owner = owner;
	}

	protected Frame properties(Component component) {
		Frame result = new Frame().addTypes("properties", typeOf(component));
		if (component.label() != null) result.addSlot("label", component.label());
		if (component.style() != null) result.addSlot("style", component.style().name$());
		return result;
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
		Set<Component> components = new HashSet<>(component.components());
		frame.addSlot("componentReferences", componentReferencesFrame());
		components.forEach(c -> frame.addSlot( "reference", referenceFrame(c)));
	}

	private Frame referenceFrame(Component component) {
		Frame frame = new Frame("reference").addSlot("name", component.name$());
		frame.addSlot("box", boxName());
		frame.addSlot("id", shortId(component));
		frame.addSlot("properties", properties(component));
		addExtends(component, frame);
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

	public Frame properties() {
		return properties(element);
	}

	private String[] ancestors(Component component) {
		List<String> result = new ArrayList<>();
		Component parent = component.core$().ownerAs(Component.class);
		while (parent != null) {
			result.add(0, nameOf(parent));
			parent = parent.core$().ownerAs(Component.class);
		}
		return result.toArray(new String[0]);
	}

	private UIRenderer componentRenderer(Component component) {
		ComponentRenderer renderer = factory.renderer(settings, component, templateProvider, target);
		renderer.buildChildren(true);
		renderer.decorated(decorated);
		renderer.owner(owner);
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

	@NotNull
	private List<Component> instanceBlocks(Component component) {
		return components(component).stream().filter(c -> c.i$(InstanceBlock.class)).map(c -> c.a$(InstanceBlock.class).type()).distinct().collect(Collectors.toList());
	}

	protected void addExtends(Component element, Frame result) {
		Frame frame = new Frame("extends");

		if (!addSpecificTypes(frame)) frame.addSlot("type", type());
		addFacets(element, frame);
		addDecoratedFrames(frame, decorated);

		result.addSlot("extends", frame);
	}

	private boolean addSpecificTypes(Frame frame) {
		if (element.i$(InstanceBlock.class)) {
			frame.addTypes("instanceBlock");
			frame.addSlot("blockName", element.a$(InstanceBlock.class).type().name$());
			String type = element.a$(InstanceBlock.class).type().name$();
			frame.addSlot("type", type).addSlot("blockType", type);
			return true;
		}
		else if (element.i$(InstanceCollectionBlock.class)) {
			frame.addTypes("instanceCollectionBlock");
			frame.addSlot("blockName", element.a$(InstanceCollectionBlock.class).type().name$());
			String type = element.a$(InstanceCollectionBlock.class).type().name$();
			frame.addSlot("type", type).addSlot("blockType", type);
			return true;
		}
		return false;
	}

	protected void addFacets(Component component, Frame result) {
		if (component.i$(Editable.class)) result.addSlot("facet", new Frame("facet").addSlot("name", Editable.class.getSimpleName()));
		if (component.i$(CodeText.class)) result.addSlot("facet", new Frame("facet").addSlot("name", CodeText.class.getSimpleName().replace("Text", "")));
		if (component.i$(SelectionBlock.class)) result.addSlot("facet", new Frame("facet").addSlot("name", SelectionBlock.class.getSimpleName().replace("Block", "")));
		if (component.i$(MenuSelector.class)) result.addSlot("facet", new Frame("facet").addSlot("name", MenuSelector.class.getSimpleName().replace("Selector", "")));
		if (component.i$(ComboBoxSelector.class)) result.addSlot("facet", new Frame("facet").addSlot("name", ComboBoxSelector.class.getSimpleName().replace("Selector", "")));
		if (component.i$(RadioBoxSelector.class)) result.addSlot("facet", new Frame("facet").addSlot("name", RadioBoxSelector.class.getSimpleName().replace("Selector", "")));
		if (component.i$(CheckBoxSelector.class)) result.addSlot("facet", new Frame("facet").addSlot("name", CheckBoxSelector.class.getSimpleName().replace("Selector", "")));
	}

	private void addImplements(C element, Frame frame) {
		if (!element.isOption()) return;
		frame.addSlot("implements", new Frame("implements", "option").addSlot("option", ""));
	}

	private Frame componentReferencesFrame() {
		Frame result = new Frame("componentReferences");
		if (!element.i$(Block.class)) return result;
		result.addTypes("forBlock");
		element.a$(Block.class).componentList().forEach(c -> addComponent(c, result));
		return result;
	}

}
