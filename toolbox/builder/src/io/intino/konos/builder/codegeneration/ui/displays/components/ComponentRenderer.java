package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayRenderer;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.CatalogComponents.Collection.Mold;
import io.intino.konos.model.graph.OtherComponents.Header;
import io.intino.konos.model.graph.OtherComponents.Selector;
import io.intino.konos.model.graph.OtherComponents.Snackbar;
import io.intino.konos.model.graph.OtherComponents.Stamp;
import io.intino.konos.model.graph.OtherComponents.Wizard.Step;
import io.intino.konos.model.graph.avatar.datacomponents.AvatarImage;
import io.intino.konos.model.graph.badge.BadgeBlock;
import io.intino.konos.model.graph.checkbox.othercomponents.CheckBoxSelector;
import io.intino.konos.model.graph.code.datacomponents.CodeText;
import io.intino.konos.model.graph.combobox.catalogcomponents.ComboBoxGrouping;
import io.intino.konos.model.graph.combobox.othercomponents.ComboBoxSelector;
import io.intino.konos.model.graph.conditional.ConditionalBlock;
import io.intino.konos.model.graph.menu.othercomponents.MenuSelector;
import io.intino.konos.model.graph.multiple.MultipleBlock;
import io.intino.konos.model.graph.multiple.datacomponents.*;
import io.intino.konos.model.graph.multiple.othercomponents.MultipleIcon;
import io.intino.konos.model.graph.multiple.othercomponents.MultipleStamp;
import io.intino.konos.model.graph.parallax.ParallaxBlock;
import io.intino.konos.model.graph.radiobox.othercomponents.RadioBoxSelector;
import io.intino.tara.magritte.Layer;

import java.util.*;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;
import static io.intino.konos.model.graph.OperationComponents.Toolbar;

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
	public FrameBuilder frameBuilder() {
		FrameBuilder builder = super.frameBuilder().add("component");
		addOwner(builder);
		builder.add("properties", properties());
		if (buildChildren) builder.add("child");
		builder.add("methodName", element.i$(ConditionalBlock.class) ? "refresh" : "init");
		addSpecificTypes(builder);
		addComponents(element, builder);
		addReferences(element, builder);
		addFacets(element, builder);
		addExtends(element, builder);
		addImplements(element, builder);
		return builder;
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

	protected FrameBuilder addOwner(FrameBuilder builder) {
		if (owner != null) builder.add("owner", (owner.isDecorated() ? "Abstract" : "") + firstUpperCase(owner.name$()));
		return builder;
	}

	@Override
	protected void addDecoratedFrames(FrameBuilder builder) {
		addDecoratedFrames(builder, decorated);
	}

	private void addComponents(Component component, FrameBuilder builder) {
		List<Component> components = components(component);
		components.forEach(c -> {
			FrameBuilder componentBuilder = buildChildren ? childFrame(c) : componentFrame(c);
			builder.add( "component", componentBuilder);
		});
	}

	private void addReferences(Component component, FrameBuilder builder) {
		Set<Component> components = new LinkedHashSet<>(references(component));
		builder.add("componentReferences", componentReferencesFrame());
		components.forEach(c -> builder.add( "reference", referenceFrame(c)));
	}

	private FrameBuilder referenceFrame(Component component) {
		ComponentRenderer renderer = factory.renderer(settings, component, templateProvider, target);
		FrameBuilder builder = new FrameBuilder("reference").add(typeOf(component)).add("name", component.name$());
		builder.add("box", boxName());
		builder.add("id", shortId(component));
		builder.add("properties", renderer.properties());
		addOwner(builder);
		addExtends(component, builder);
		return builder;
	}

	@Override
	protected FrameBuilder componentFrame(Component component) {
		return componentRenderer(component).frameBuilder();
	}

	protected String className(Class clazz) {
		return clazz.getSimpleName().toLowerCase();
	}

	protected FrameBuilder childFrame(Component component) {
		FrameBuilder result = componentRenderer(component).frameBuilder();
		String[] ancestors = ancestors(component);
		result.add("ancestors", ancestors);
		result.add("ancestorsNotMe", Arrays.copyOfRange(ancestors, 1, ancestors.length));
		result.add("value", componentRenderer(component).frameBuilder().add("addType", typeOf(component)));
		return result;
	}

	public FrameBuilder properties() {
		FrameBuilder result = new FrameBuilder().add("properties").add(typeOf(element));
		if (element.color() != null && !element.color().isEmpty()) result.add("color", element.color());
		if (element.i$(AbstractLabeled.class)) result.add("label", element.a$(AbstractLabeled.class).label());
		if (element.i$(AbstractMultiple.class)) {
			AbstractMultiple abstractMultiple = element.a$(AbstractMultiple.class);
			result.add("instances", nameOf(element));
			result.add("multipleArrangement", abstractMultiple.arrangement().name());
			result.add("multipleNoItemsMessage", abstractMultiple.noItemsMessage() != null ? abstractMultiple.noItemsMessage() : "");
		}
		if (element.format() != null) {
			String[] format = element.format().stream().map(Layer::name$).toArray(String[]::new);
			result.add("format", format);
		}
		return result;
	}

	private String[] ancestors(Component component) {
		List<String> result = new ArrayList<>();
		Component parent = component.core$().ownerAs(Component.class);
		while (parent != null) {
			result.add(0, nameOf(parent));
			if (parent.i$(Mold.Item.class)) break;
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
		if (component.i$(Mold.Heading.class)) components.addAll(component.a$(Mold.Heading.class).componentList());
		if (component.i$(Mold.Item.class)) components.addAll(component.a$(Mold.Item.class).componentList());
		if (component.i$(Toolbar.class)) components.addAll(component.a$(Toolbar.class).componentList());
		return components;
	}

	private List<Component> references(Component component) {
		if (element.i$(PrivateComponents.Row.class)) return element.a$(PrivateComponents.Row.class).items().stream().map(i -> i.a$(Component.class)).collect(Collectors.toList());
		return component.components();
	}

	private void addExtends(Component element, FrameBuilder builder) {
		builder.add("extends", extendsFrame(element, builder));
	}

	protected FrameBuilder extendsFrame(Component element, FrameBuilder builder) {
		FrameBuilder result = new FrameBuilder("extends");
		result.add("name", nameOf(element));

		if (!addSpecificTypes(result)) result.add("type", type());
		addFacets(element, result);
		addDecoratedFrames(result, decorated);

		return result;
	}

	protected boolean addSpecificTypes(FrameBuilder builder) {

		if (element.i$(AbstractMultiple.class)) {
			builder.add(AbstractMultiple.class.getSimpleName().replace("Abstract", ""));
			String message = element.a$(AbstractMultiple.class).noItemsMessage();
			if (message != null) builder.add("noItemsMessage", message);
			FrameBuilder methodsFrame = addOwner(baseFrameBuilder()).add("method", "multiple");
			methodsFrame.add("componentType", multipleComponentType(element));
			String objectType = multipleObjectType(element);
			if (objectType != null && !objectType.equals("java.lang.Void")) {
				methodsFrame.add("objectType", objectType);
				methodsFrame.add("objectTypeValue", "value");
			}
			methodsFrame.add("name", nameOf(element));
			builder.add("methods", methodsFrame);
			builder.add("multiple");
			builder.add("componentType", multipleComponentType(element));
			if (objectType != null) builder.add("objectType", objectType);
		}

		if (element.i$(Template.class)) {
			builder.add(Template.class.getSimpleName());
			String modelClass = element.a$(Template.class).modelClass();
			builder.add("componentType", nameOf(element));
			builder.add("objectType", modelClass != null ? modelClass : "java.lang.Void");
			return true;
		}

		if (element.i$(Stamp.class)) {
			builder.add(Stamp.class.getSimpleName());
			if (!element.i$(AbstractMultiple.class)) builder.add("single");
			Template template = element.a$(Stamp.class).template();
			builder.add("template", template.name$());
			builder.add("type", template.name$());
			return true;
		}

		if (element.i$(Mold.Item.class)) {
			builder.add(Mold.Item.class.getSimpleName());
			CatalogComponents.Collection collection = element.a$(Mold.Item.class).core$().ownerAs(CatalogComponents.Collection.class);
			builder.add("itemClass", collection.itemClass() != null ? collection.itemClass() : "java.lang.Void");
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

	protected void addFacets(Component component, FrameBuilder builder) {
		if (component.i$(Editable.class)) builder.add("facet", new FrameBuilder("facet").add("name", Editable.class.getSimpleName()));
		if (component.i$(CodeText.class)) builder.add("facet", new FrameBuilder("facet").add("name", CodeText.class.getSimpleName().replace("Text", "")));
		if (component.i$(BadgeBlock.class)) builder.add("facet", new FrameBuilder("facet").add("name", BadgeBlock.class.getSimpleName().replace("Block", "")));
		if (component.i$(ConditionalBlock.class)) builder.add("facet", new FrameBuilder("facet").add("name", ConditionalBlock.class.getSimpleName().replace("Block", "")));
		if (component.i$(MenuSelector.class)) builder.add("facet", new FrameBuilder("facet").add("name", MenuSelector.class.getSimpleName().replace("Selector", "")));
		if (component.i$(ComboBoxSelector.class)) builder.add("facet", new FrameBuilder("facet").add("name", ComboBoxSelector.class.getSimpleName().replace("Selector", "")));
		if (component.i$(ComboBoxGrouping.class)) builder.add("facet", new FrameBuilder("facet").add("name", ComboBoxGrouping.class.getSimpleName().replace("Grouping", "")));
		if (component.i$(RadioBoxSelector.class)) builder.add("facet", new FrameBuilder("facet").add("name", RadioBoxSelector.class.getSimpleName().replace("Selector", "")));
		if (component.i$(CheckBoxSelector.class)) builder.add("facet", new FrameBuilder("facet").add("name", CheckBoxSelector.class.getSimpleName().replace("Selector", "")));
		if (component.i$(AvatarImage.class)) builder.add("facet", new FrameBuilder("facet").add("name", AvatarImage.class.getSimpleName().replace("Image", "")));
		if (component.i$(ParallaxBlock.class)) builder.add("facet", new FrameBuilder("facet").add("name", ParallaxBlock.class.getSimpleName().replace("Block", "")));
	}

	protected FrameBuilder resourceMethodFrame(String method, String value) {
		FrameBuilder result = new FrameBuilder("resourceMethod").add("name", method).add("value", fixResourceValue(value));
		addOwner(result);
		return result;
	}

	protected String fixResourceValue(String value) {
		return value.startsWith("/") ? value : "/" + value;
	}

	private void addImplements(C element, FrameBuilder builder) {
		if (!element.isOption()) return;
		builder.add("implements", new FrameBuilder("implements", "option").add("option", ""));
	}

	private FrameBuilder componentReferencesFrame() {
		FrameBuilder result = new FrameBuilder("componentReferences");
		List<Component> componentList = null;

		if (element.i$(Block.class)) componentList = element.a$(Block.class).componentList();
		else if (element.i$(Template.class)) componentList = element.a$(Template.class).componentList();
		else if (element.i$(Mold.Item.class)) componentList = element.a$(Mold.Item.class).componentList();

		if (componentList == null) return result;

		result.add("forRoot");
		componentList.forEach(c -> addComponent(c, result));

		return result;
	}

}
