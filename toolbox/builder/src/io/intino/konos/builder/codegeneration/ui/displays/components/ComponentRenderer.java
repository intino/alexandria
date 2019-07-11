package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayRenderer;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.CatalogComponents.Collection.Mold;
import io.intino.konos.model.graph.OtherComponents.Stamp;
import io.intino.konos.model.graph.conditional.ConditionalBlock;
import io.intino.konos.model.graph.multiple.MultipleBlock;
import io.intino.konos.model.graph.multiple.datacomponents.*;
import io.intino.konos.model.graph.multiple.othercomponents.MultipleIcon;
import io.intino.konos.model.graph.multiple.othercomponents.MultipleStamp;
import io.intino.tara.magritte.Layer;

import java.util.*;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;

public class ComponentRenderer<C extends Component> extends DisplayRenderer<C> {
	private boolean buildChildren = false;
	private boolean decorated;
	private Display owner;
	private static final ComponentRendererFactory factory = new ComponentRendererFactory();
	private static final Map<String, FrameBuilder> componentFrameMap = new HashMap<>();

	public ComponentRenderer(Settings settings, C component, TemplateProvider provider, Target target) {
		super(settings, component, provider, target);
		this.decorated = component.isDecorated();
	}

	@Override
	public final FrameBuilder buildFrame() {
		String frameId = frameId();
		if (componentFrameMap.containsKey(frameId)) return componentFrameMap.get(frameId);
		FrameBuilder builder = super.buildFrame().add("component");
		addOwner(builder);
		addProperties(builder);
		if (buildChildren) builder.add("child");
		builder.add("methodName", element.i$(ConditionalBlock.class) ? "initConditional" : "init");
		addSpecificTypes(builder);
		addComponents(element, builder);
		addReferences(element, builder);
		addFacets(element, builder);
		addExtends(element, builder);
		addImplements(element, builder);
		fill(builder);
		componentFrameMap.put(frameId, builder);
		return builder;
	}

	public static void clearCache() {
		componentFrameMap.clear();
	}

	public void buildChildren(boolean value) {
		this.buildChildren = value;
	}

	public void decorated(boolean value) {
		this.decorated = value;
	}

	public Display owner() {
		return this.owner;
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

	protected void fill(FrameBuilder builder) {
	}

	private void addComponents(Component component, FrameBuilder builder) {
		addComponentsImports(builder);
		components(component).forEach(c -> {
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
		return componentRenderer(component).buildFrame();
	}

	protected FrameBuilder childFrame(Component component) {
		FrameBuilder result = componentRenderer(component).buildFrame();
		String[] ancestors = ancestors(component);
		result.add("ancestors", ancestors);
		result.add("ancestorsNotMe", Arrays.copyOfRange(ancestors, 1, ancestors.length));
		result.add("value", componentRenderer(component).buildFrame().add("addType", typeOf(component)));
		return result;
	}

	public FrameBuilder properties() {
		FrameBuilder result = new FrameBuilder().add("properties").add(typeOf(element));
		if (element.color() != null && !element.color().isEmpty()) result.add("color", element.color());
		if (element.isOption()) result.add("name", element.asOption().name$());
		if (element.i$(AbstractLabeled.class)) result.add("label", element.a$(AbstractLabeled.class).label());
		if (element.i$(AbstractMultiple.class)) {
			AbstractMultiple abstractMultiple = element.a$(AbstractMultiple.class);
			result.add("instances", nameOf(element));
			result.add("multipleArrangement", abstractMultiple.arrangement().name());
			result.add("multipleSpacing", abstractMultiple.spacing().value());
			result.add("multipleNoItemsMessage", abstractMultiple.noItemsMessage() != null ? abstractMultiple.noItemsMessage() : "");
		}
		if (element.format() != null) {
			String[] format = element.format().stream().map(Layer::name$).toArray(String[]::new);
			result.add("format", format);
		}
		return result;
	}

	protected String className(Class clazz) {
		return clazz.getSimpleName().toLowerCase();
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

	private List<Component> references(Component component) {
		if (element.i$(PrivateComponents.Row.class)) return element.a$(PrivateComponents.Row.class).items().stream().map(i -> i.a$(Component.class)).collect(Collectors.toList());
		return component.components();
	}

	private void addExtends(Component element, FrameBuilder builder) {
		builder.add("extends", extendsFrame(element, builder));
	}

	protected FrameBuilder extendsFrame(Component element, FrameBuilder builder) {
		FrameBuilder result = new FrameBuilder("extends");
		if (element.i$(CatalogComponents.Collection.class)) result.add("collection");
		if (element.i$(CatalogComponents.Table.class)) result.add("table");
		result.add("name", nameOf(element));

		if (!addSpecificTypes(result)) result.add("type", type());
		addFacets(element, result);
		addDecoratedFrames(result, decorated);

		return result;
	}

	protected boolean addSpecificTypes(FrameBuilder builder) {

		if (element.i$(AbstractMultiple.class)) {
			builder.add("multiple");
			String message = element.a$(AbstractMultiple.class).noItemsMessage();
			if (message != null) builder.add("noItemsMessage", message);
			FrameBuilder methodsFrame = addOwner(buildBaseFrame()).add("method").add("multiple");
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
			builder.add("template");
			String modelClass = element.a$(Template.class).modelClass();
			builder.add("componentType", nameOf(element));
			builder.add("objectType", modelClass != null ? modelClass : "java.lang.Void");
			return true;
		}

		if (element.i$(Stamp.class)) {
			builder.add("stamp");
			if (!element.i$(AbstractMultiple.class)) builder.add("single");
			Template template = element.a$(Stamp.class).template();
			builder.add("template", template.name$());
			builder.add("type", template.name$());
			return true;
		}

		if (element.i$(Mold.Item.class)) {
			builder.add("item");
			CatalogComponents.Collection collection = element.a$(Mold.Item.class).core$().ownerAs(CatalogComponents.Collection.class);
			builder.add("itemClass", collection.itemClass() != null ? collection.itemClass() : "java.lang.Void");
			return true;
		}

		return false;
	}

	private void addProperties(FrameBuilder builder) {
		FrameBuilder properties = properties();
		if (properties.slots() <= 0) return;
		builder.add("properties", properties());
	}

	private String multipleComponentType(C element) {
		String prefix = "io.intino.alexandria.ui.displays.components.";
		if (element.i$(MultipleText.class)) return prefix + "Text";
		if (element.i$(MultipleFile.class)) return prefix + "File";
		if (element.i$(MultipleImage.class)) return prefix + "Image";
		if (element.i$(MultipleIcon.class)) return prefix + "Icon";
		if (element.i$(MultipleNumber.class)) return prefix + "Number";
		if (element.i$(MultipleDate.class)) return prefix + "Date";
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

		return componentList.size() > 0 ? result : null;
	}

	private String frameId() {
		return nameOf(element) + (owner != null ? owner.name$() : "") + target.name() + buildChildren + decorated;
	}

}
