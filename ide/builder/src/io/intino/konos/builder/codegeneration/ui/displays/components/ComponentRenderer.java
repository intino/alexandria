package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayRenderer;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.ChildComponents.Collection;
import io.intino.konos.model.graph.ChildComponents.*;
import io.intino.konos.model.graph.ChildComponents.Wizard.Step;
import io.intino.konos.model.graph.avatar.childcomponents.AvatarImage;
import io.intino.konos.model.graph.badge.BadgeBlock;
import io.intino.konos.model.graph.checkbox.childcomponents.CheckBoxSelector;
import io.intino.konos.model.graph.code.childcomponents.CodeText;
import io.intino.konos.model.graph.combobox.childcomponents.ComboBoxSelector;
import io.intino.konos.model.graph.conditional.ConditionalBlock;
import io.intino.konos.model.graph.menu.childcomponents.MenuSelector;
import io.intino.konos.model.graph.multiple.MultipleBlock;
import io.intino.konos.model.graph.multiple.childcomponents.*;
import io.intino.konos.model.graph.parallax.ParallaxBlock;
import io.intino.konos.model.graph.radiobox.childcomponents.RadioBoxSelector;
import io.intino.tara.magritte.Layer;
import org.siani.itrules.model.Frame;

import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;

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
		Set<Component> components = new LinkedHashSet<>(references(component));
		frame.addSlot("componentReferences", componentReferencesFrame());
		components.forEach(c -> frame.addSlot( "reference", referenceFrame(c)));
	}

	private Frame referenceFrame(Component component) {
		ComponentRenderer renderer = factory.renderer(settings, component, templateProvider, target);
		Frame frame = new Frame("reference", typeOf(component)).addSlot("name", component.name$());
		frame.addSlot("box", boxName());
		frame.addSlot("id", shortId(component));
		frame.addSlot("properties", renderer.properties());
		addOwner(frame);
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
		Frame result = new Frame().addTypes("properties", typeOf(element));
		if (element.color() != null && !element.color().isEmpty()) result.addSlot("color", element.color());
		if (element.i$(AbstractLabeled.class)) result.addSlot("label", element.a$(AbstractLabeled.class).label());
		if (element.i$(AbstractMultiple.class)) {
			AbstractMultiple abstractMultiple = element.a$(AbstractMultiple.class);
			result.addSlot("instances", nameOf(element));
			result.addSlot("multipleArrangement", abstractMultiple.arrangement().name());
			result.addSlot("multipleNoItemsMessage", abstractMultiple.noItemsMessage() != null ? abstractMultiple.noItemsMessage() : "");
		}
		if (element.format() != null) {
			String[] format = element.format().stream().map(Layer::name$).toArray(String[]::new);
			result.addSlot("format", format);
		}
		return result;
	}

	private String[] ancestors(Component component) {
		List<String> result = new ArrayList<>();
		Component parent = component.core$().ownerAs(Component.class);
		while (parent != null) {
			result.add(0, nameOf(parent));
			if (parent.i$(Collection.Mold.Item.class)) break;
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
		if (component.i$(Template.class)) components.addAll(component.a$(Template.class).componentList());
		if (component.i$(Catalog.class)) components.addAll(component.a$(Catalog.class).componentList());
		if (component.i$(Snackbar.class)) components.addAll(component.a$(Snackbar.class).componentList());
		if (component.i$(Step.class)) components.addAll(component.a$(Step.class).componentList());
		if (component.i$(Header.class)) components.addAll(component.a$(Header.class).componentList());
		if (component.i$(Selector.class)) components.addAll(component.a$(Selector.class).componentList());
		if (component.i$(Collection.Mold.Heading.class)) components.addAll(component.a$(Collection.Mold.Heading.class).componentList());
		if (component.i$(Collection.Mold.Item.class)) components.addAll(component.a$(Collection.Mold.Item.class).componentList());
		return components;
	}

	private List<Component> references(Component component) {
		if (element.i$(PrivateComponents.Row.class)) return element.a$(PrivateComponents.Row.class).items().stream().map(i -> i.a$(Component.class)).collect(Collectors.toList());
		return component.components();
	}

	protected void addExtends(Component element, Frame result) {
		Frame frame = new Frame("extends");
		frame.addSlot("name", nameOf(element));

		if (!addSpecificTypes(frame)) frame.addSlot("type", type());
		addFacets(element, frame);
		addDecoratedFrames(frame, decorated);

		result.addSlot("extends", frame);
	}

	protected boolean addSpecificTypes(Frame frame) {

		if (element.i$(AbstractMultiple.class)) {
			frame.addTypes(AbstractMultiple.class.getSimpleName().replace("Abstract", ""));
			String message = element.a$(AbstractMultiple.class).noItemsMessage();
			if (message != null) frame.addSlot("noItemsMessage", message);
			Frame methodsFrame = addOwner(baseFrame()).addTypes("method", "multiple");
			methodsFrame.addSlot("componentType", multipleComponentType(element));
			String objectType = multipleObjectType(element);
			if (objectType != null && !objectType.equals("java.lang.Void")) {
				methodsFrame.addSlot("objectType", objectType);
				methodsFrame.addSlot("objectTypeValue", "value");
			}
			methodsFrame.addSlot("name", nameOf(element));
			frame.addSlot("methods", methodsFrame);
			frame.addTypes("multiple");
			frame.addSlot("componentType", multipleComponentType(element));
			if (objectType != null) frame.addSlot("objectType", objectType);
		}

		if (element.i$(Template.class)) {
			frame.addTypes(Template.class.getSimpleName());
			String modelClass = element.a$(Template.class).modelClass();
			frame.addSlot("componentType", nameOf(element));
			frame.addSlot("objectType", modelClass != null ? modelClass : "java.lang.Void");
			return true;
		}

		if (element.i$(Stamp.class)) {
			frame.addTypes(Stamp.class.getSimpleName());
			if (!element.i$(AbstractMultiple.class)) frame.addTypes("single");
			Template template = element.a$(Stamp.class).template();
			frame.addSlot("template", template.name$());
			frame.addSlot("type", template.name$());
			return true;
		}

		if (element.i$(Collection.Mold.Item.class)) {
			frame.addTypes(Collection.Mold.Item.class.getSimpleName());
			Collection collection = element.a$(Collection.Mold.Item.class).core$().ownerAs(Collection.class);
			frame.addSlot("itemClass", collection.itemClass() != null ? collection.itemClass() : "java.lang.Void");
			return true;
		}

		return false;
	}

	private String multipleComponentType(C element) {
		String prefix = "io.intino.alexandria.ui.displays.components.";
		if (element.i$(MultipleText.class)) return prefix + MultipleText.class.getSimpleName().replace("Multiple", "");
		if (element.i$(MultipleFile.class)) return prefix + MultipleFile.class.getSimpleName().replace("Multiple", "");
		if (element.i$(MultipleImage.class)) return prefix + MultipleImage.class.getSimpleName().replace("Multiple", "");
		if (element.i$(MultipleIcon.class)) return prefix + MultipleIcon.class.getSimpleName().replace("Multiple", "");
		if (element.i$(MultipleNumber.class)) return prefix + MultipleNumber.class.getSimpleName().replace("Multiple", "");
		if (element.i$(MultipleDate.class)) return prefix + MultipleDate.class.getSimpleName().replace("Multiple", "");
		if (element.i$(MultipleStamp.class)) return firstUpperCase(element.a$(MultipleStamp.class).template().name$());
		if (element.i$(MultipleBlock.class)) return firstUpperCase(nameOf(element));
		return null;
	}

	private String multipleObjectType(C element) {
		if (element.i$(MultipleText.class)) return "java.lang.String";
		if (element.i$(MultipleFile.class)) return "java.net.URL";
		if (element.i$(MultipleImage.class)) return "java.net.URL";
		if (element.i$(MultipleIcon.class)) return "java.net.URL";
		if (element.i$(MultipleNumber.class)) return "java.lang.Double";
		if (element.i$(MultipleDate.class)) return "java.time.Instant";
		if (element.i$(MultipleStamp.class)) {
			String modelClass = element.a$(MultipleStamp.class).template().modelClass();
			return modelClass != null ? modelClass : "java.lang.Void";
		}
		if (element.i$(MultipleBlock.class)) return "java.lang.Void";
		return null;
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
		List<Component> componentList = null;

		if (element.i$(Block.class)) componentList = element.a$(Block.class).componentList();
		else if (element.i$(Template.class)) componentList = element.a$(Template.class).componentList();
		else if (element.i$(Collection.Mold.Item.class)) componentList = element.a$(Collection.Mold.Item.class).componentList();

		if (componentList == null) return result;

		result.addTypes("forRoot");
		componentList.forEach(c -> addComponent(c, result));

		return result;
	}

}
