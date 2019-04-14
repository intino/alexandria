package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayRenderer;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.ChildComponents.Collection;
import io.intino.konos.model.graph.ChildComponents.Header;
import io.intino.konos.model.graph.ChildComponents.Selector;
import io.intino.konos.model.graph.ChildComponents.Snackbar;
import io.intino.konos.model.graph.ChildComponents.Wizard.Step;
import io.intino.konos.model.graph.avatar.childcomponents.AvatarImage;
import io.intino.konos.model.graph.badge.BadgeBlock;
import io.intino.konos.model.graph.checkbox.childcomponents.CheckBoxSelector;
import io.intino.konos.model.graph.code.childcomponents.CodeText;
import io.intino.konos.model.graph.combobox.childcomponents.ComboBoxSelector;
import io.intino.konos.model.graph.conditional.ConditionalBlock;
import io.intino.konos.model.graph.menu.childcomponents.MenuSelector;
import io.intino.konos.model.graph.multiple.MultipleBlock;
import io.intino.konos.model.graph.multiple.childcomponents.MultipleText;
import io.intino.konos.model.graph.parallax.ParallaxBlock;
import io.intino.konos.model.graph.radiobox.childcomponents.RadioBoxSelector;
import io.intino.konos.model.graph.stamp.StampBlock;
import io.intino.tara.magritte.Layer;
import org.siani.itrules.model.Frame;

import java.util.*;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.model.graph.KonosGraph.isMultiple;

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
		addOwner(frame);
		frame.addSlot("properties", properties());
		if (buildChildren) frame.addTypes("child");
		frame.addSlot("methodName", element.i$(ConditionalBlock.class) ? "refresh" : "init");
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
		if (component.color() != null && !component.color().isEmpty()) result.addSlot("color", element.color());
		if (component.i$(AbstractLabeled.class)) result.addSlot("label", component.a$(AbstractLabeled.class).label());
		if (isMultiple(component)) result.addSlot("instances", nameOf(component));
		if (component.i$(MultipleText.class)) result.addSlot("multipleLayout", component.a$(MultipleText.class).layout().name());
		if (component.format() != null) {
			String[] format = component.format().stream().map(Layer::name$).toArray(String[]::new);
			result.addSlot("format", format);
		}
		return result;
	}

	protected Frame addOwner(Frame frame) {
		if (owner != null) frame.addSlot("owner", (owner.isDecorated() ? "Abstract" : "") + firstUpperCase(owner.name$()));
		return frame;
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
		if (component.i$(Mold.class)) components.addAll(component.a$(Mold.class).componentList());
		if (component.i$(Catalog.class)) components.addAll(component.a$(Catalog.class).componentList());
		if (component.i$(Snackbar.class)) components.addAll(component.a$(Snackbar.class).componentList());
		if (component.i$(Step.class)) components.addAll(component.a$(Step.class).componentList());
		if (component.i$(Header.class)) components.addAll(component.a$(Header.class).componentList());
		if (component.i$(Selector.class)) components.addAll(component.a$(Selector.class).componentList());
		return components;
	}

	protected void addExtends(Component element, Frame result) {
		Frame frame = new Frame("extends");

		if (!addSpecificTypes(frame)) frame.addSlot("type", type());
		addFacets(element, frame);
		addDecoratedFrames(frame, decorated);

		result.addSlot("extends", frame);
	}

	private boolean addSpecificTypes(Frame frame) {

		if (element.i$(MultipleBlock.class)) {
			frame.addTypes(MultipleBlock.class.getSimpleName());
			String message = element.a$(MultipleBlock.class).noItemsMessage();
			if (message != null) frame.addSlot("noItemsMessage", message);
		}
		else if (element.i$(MultipleText.class)) {
			Frame methodsFrame = addOwner(baseFrame()).addTypes("method", "multiple");
			methodsFrame.addSlot("componentType", "Text");
			methodsFrame.addSlot("objectType", "String");
			methodsFrame.addSlot("name", nameOf(element));
			frame.addSlot("methods", methodsFrame);
			frame.addTypes("multiple");
			frame.addSlot("componentType", "Text");
			frame.addSlot("objectType", "String");
		}

		if (element.i$(StampBlock.class)) {
			frame.addTypes(StampBlock.class.getSimpleName());
			Mold mold = element.a$(StampBlock.class).mold();
			frame.addSlot("mold", mold.name$());
			frame.addSlot("type", mold.name$());
			return true;
		}

		if (element.i$(Collection.class)) {
			Frame methodsFrame = addOwner(baseFrame()).addTypes("method", Collection.class.getSimpleName());
			Collection collection = element.a$(Collection.class);
			String modelClass = collection.source().modelClass();
			methodsFrame.addSlot("modelClass", modelClass);
			methodsFrame.addSlot("mold", collection.mold().name$());
			frame.addTypes(Collection.class.getSimpleName());
			frame.addSlot("methods", methodsFrame);
			frame.addSlot("modelClass", modelClass);
			frame.addSlot("mold", collection.mold().name$());
			return false;
		}

		return false;
	}

	protected void addFacets(Component component, Frame result) {
		if (component.i$(Editable.class)) result.addSlot("facet", new Frame("facet").addSlot("name", Editable.class.getSimpleName()));
		if (component.i$(CodeText.class)) result.addSlot("facet", new Frame("facet").addSlot("name", CodeText.class.getSimpleName().replace("Text", "")));
		if (component.i$(BadgeBlock.class)) result.addSlot("facet", new Frame("facet").addSlot("name", BadgeBlock.class.getSimpleName().replace("Block", "")));
		if (component.i$(ConditionalBlock.class)) result.addSlot("facet", new Frame("facet").addSlot("name", ConditionalBlock.class.getSimpleName().replace("Block", "")));
		if (component.i$(MenuSelector.class)) result.addSlot("facet", new Frame("facet").addSlot("name", MenuSelector.class.getSimpleName().replace("Selector", "")));
		if (component.i$(ComboBoxSelector.class)) result.addSlot("facet", new Frame("facet").addSlot("name", ComboBoxSelector.class.getSimpleName().replace("Selector", "")));
		if (component.i$(RadioBoxSelector.class)) result.addSlot("facet", new Frame("facet").addSlot("name", RadioBoxSelector.class.getSimpleName().replace("Selector", "")));
		if (component.i$(CheckBoxSelector.class)) result.addSlot("facet", new Frame("facet").addSlot("name", CheckBoxSelector.class.getSimpleName().replace("Selector", "")));
		if (component.i$(AvatarImage.class)) result.addSlot("facet", new Frame("facet").addSlot("name", AvatarImage.class.getSimpleName().replace("Image", "")));
		if (component.i$(ParallaxBlock.class)) result.addSlot("facet", new Frame("facet").addSlot("name", ParallaxBlock.class.getSimpleName().replace("Block", "")));
	}

	protected Frame resourceMethodFrame(String method, String value) {
		Frame frame = new Frame("resourceMethod").addSlot("name", method).addSlot("value", fixResourceValue(value));
		addOwner(frame);
		return frame;
	}

	protected String fixResourceValue(String value) {
		return value.startsWith("/") ? value : "/" + value;
	}

	private void addImplements(C element, Frame frame) {
		if (!element.isOption()) return;
		frame.addSlot("implements", new Frame("implements", "option").addSlot("option", ""));
	}

	private Frame componentReferencesFrame() {
		Frame result = new Frame("componentReferences");
		if (element.i$(Block.class)) {
			result.addTypes("forBlock");
			element.a$(Block.class).componentList().forEach(c -> addComponent(c, result));
		}
		else if (element.i$(Mold.class)) {
			result.addTypes("forMold");
			element.a$(Mold.class).componentList().forEach(c -> addComponent(c, result));
		}
		return result;
	}

}
