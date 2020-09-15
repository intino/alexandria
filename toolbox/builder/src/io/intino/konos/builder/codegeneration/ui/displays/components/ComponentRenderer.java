package io.intino.konos.builder.codegeneration.ui.displays.components;

import io.intino.itrules.FrameBuilder;
import io.intino.konos.builder.codegeneration.Target;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.builder.codegeneration.ui.UIRenderer;
import io.intino.konos.builder.codegeneration.ui.displays.DisplayRenderer;
import io.intino.konos.builder.context.CompilationContext;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.konos.model.graph.*;
import io.intino.konos.model.graph.CatalogComponents.Collection.Mold;
import io.intino.konos.model.graph.DataComponents.File;
import io.intino.konos.model.graph.DataComponents.Image;
import io.intino.konos.model.graph.DataComponents.Text;
import io.intino.konos.model.graph.OtherComponents.*;
import io.intino.magritte.framework.Layer;

import java.util.*;
import java.util.stream.Collectors;

import static io.intino.konos.builder.codegeneration.Formatters.firstUpperCase;

public class ComponentRenderer<C extends Component> extends DisplayRenderer<C> {
	private boolean buildChildren = false;
	private boolean decorated;
	private Display owner;
	private static final ComponentRendererFactory factory = new ComponentRendererFactory();
	private static final Map<String, FrameBuilder> componentFrameMap = new HashMap<>();

	public ComponentRenderer(CompilationContext compilationContext, C component, TemplateProvider provider, Target target) {
		super(compilationContext, component, provider, target);
		this.decorated = ElementHelper.isRoot(component);
	}

	@Override
	public final FrameBuilder buildFrame() {
		String frameId = frameId();
		if (componentFrameMap.containsKey(frameId)) return componentFrameMap.get(frameId);
		FrameBuilder builder = super.buildFrame().add("component");
		if (!belongsToAccessible(element)) builder.add("concreteBox", boxName());
		if (isEmbeddedComponent(element)) builder.add("embedded");
		addOwner(builder);
		addProperties(builder);
		if (buildChildren) builder.add("child");
		builder.add("methodName", element.i$(Block.Conditional.class) && !element.i$(Block.Multiple.class) ? "initConditional" : "init");
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
		if (owner != null) builder.add("owner", (ElementHelper.isRoot(owner) ? "Abstract" : "") + firstUpperCase(owner.name$()));
		Component parentComponent = element.core$().ownerAs(Component.class);
		if (element.i$(BaseStamp.class) && parentComponent != null && !ElementHelper.isRoot(parentComponent)) builder.add("parentId", shortId(parentComponent));
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
			Display virtualParent = virtualParent() != null ? virtualParent() : (c.i$(CatalogComponents.Collection.class) && component.i$(OtherComponents.Selector.CollectionBox.class) ? component : null);
			FrameBuilder componentBuilder = buildChildren ? childFrame(c, virtualParent) : componentFrame(c, virtualParent);
			builder.add( "component", componentBuilder);
		});
	}

	private void addReferences(Component component, FrameBuilder builder) {
		Set<Component> components = new LinkedHashSet<>(references(component));
		if (target == Target.Owner) builder.add("componentReferences", componentReferencesFrame());
		components.forEach(c -> builder.add( "reference", referenceFrame(c)));
	}

	private FrameBuilder referenceFrame(Component component) {
		ComponentRenderer renderer = factory.renderer(context, component, templateProvider, target);
		FrameBuilder builder = new FrameBuilder("reference").add(typeOf(component)).add("name", component.name$());
		if (isEmbeddedComponent(component)) builder.add("embedded");
		Component parentComponent = component.core$().ownerAs(Component.class);
		builder.add("box", boxName());
		if (!belongsToAccessible(component)) builder.add("concreteBox", boxName());
		builder.add("id", shortId(component));
		if (parentComponent != null) builder.add("parentId", nameOf(parentComponent));
		builder.add("properties", renderer.properties());
		addOwner(builder);
		addExtends(component, builder);
		return builder;
	}

	protected FrameBuilder componentFrame(Component component, Component virtualParent) {
		return componentRenderer(component, virtualParent).buildFrame();
	}

	protected FrameBuilder childFrame(Component component, Display virtualParent) {
		FrameBuilder result = componentRenderer(component, virtualParent).buildFrame();
		String[] ancestors = ancestors(component);
		Component parent = component.core$().ownerAs(Component.class);
		if (parent != null) result.add("parent", nameOf(parent));
		if (isEmbeddedComponent(component)) result.add("embedded");
		if (!result.contains("ancestors")) result.add("ancestors", ancestors);
		if (!result.contains("ancestorsNotMe")) result.add("ancestorsNotMe", ancestors.length > 0 ? Arrays.copyOfRange(ancestors, 1, ancestors.length) : new String[0]);
		result.add("value", componentRenderer(component, virtualParent).buildFrame().add("addType", typeOf(component)));
		return result;
	}

	public FrameBuilder properties() {
		FrameBuilder result = new FrameBuilder().add("properties").add(typeOf(element));
		if (element.color() != null && !element.color().isEmpty()) result.add("color", element.color());
		if (element.isOption()) result.add("name", element.asOption().name$());
		if (element.i$(Labeled.class)) result.add("label", element.a$(Labeled.class).label());
		if (!element.visible()) result.add("visible", element.visible());
		if (element.isTraceable()) result.add("traceable", true);
		if (element.i$(Multiple.class)) {
			Multiple abstractMultiple = element.a$(Multiple.class);
			result.add("multiple");
			result.add("instances", nameOf(element));
			result.add("multipleArrangement", abstractMultiple.arrangement().name());
			result.add("multipleSpacing", abstractMultiple.spacing().value());
			result.add("multipleNoItemsMessage", abstractMultiple.noItemsMessage() != null ? abstractMultiple.noItemsMessage() : "");
			result.add("multipleWrapItems", abstractMultiple.wrapItems());
			result.add("multipleEditable", element.i$(Editable.class));
			result.add("multipleMin", abstractMultiple.count() != null ? abstractMultiple.count().min() : 0);
			result.add("multipleMax", abstractMultiple.count() != null ? abstractMultiple.count().max() : -1);
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

	private UIRenderer componentRenderer(Component component, Display virtualParent) {
		ComponentRenderer renderer = factory.renderer(context, component, templateProvider, target);
		renderer.buildChildren(true);
		renderer.decorated(decorated);
		renderer.owner(owner);
		renderer.virtualParent(virtualParent);
		return renderer;
	}

	private List<Component> references(Component component) {
		if (element.i$(HelperComponents.Row.class)) return element.a$(HelperComponents.Row.class).items().stream().map(i -> i.a$(Component.class)).collect(Collectors.toList());
		if (element.i$(OtherComponents.Selector.CollectionBox.class) && element.a$(OtherComponents.Selector.CollectionBox.class).source() != null) return Collections.emptyList();
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

		if (element.i$(Multiple.class)) {
			String message = element.a$(Multiple.class).noItemsMessage();
			if (message != null) builder.add("noItemsMessage", message);
			FrameBuilder methodsFrame = addOwner(buildBaseFrame()).add("method").add("multiple");
			methodsFrame.add("componentType", multipleComponentType(element));
			methodsFrame.add("componentName", multipleComponentName(element));
			if (element.i$(OwnerTemplateStamp.class)) methodsFrame.add("componentOwnerBox", element.a$(OwnerTemplateStamp.class).owner().name());
			if (element.i$(Editable.class)) {
				methodsFrame.add("editableMethods", new FrameBuilder("editableMethods"));
				if (!isMultipleSpecificComponent(element)) methodsFrame.add("editableClass", editableClassFrame());
			}
			String objectType = multipleObjectType(element);
			if (objectType != null) {
				methodsFrame.add("objectType", objectType);
				if (!objectType.equals("java.lang.Void")) methodsFrame.add("objectTypeValue", "value");
			}
			methodsFrame.add("name", nameOf(element));
			builder.add("methods", methodsFrame);
			builder.add("multiple");
			builder.add("componentType", multipleComponentType(element));
			builder.add("componentName", multipleComponentName(element));
			if (!isMultipleSpecificComponent(element) && element.i$(Editable.class)) builder.add("componentPrefix", nameOf(element));
			if (objectType != null) builder.add("objectType", objectType);
		}

		if (element.i$(Template.class)) {
			builder.add("template");
			String modelClass = element.a$(Template.class).modelClass();
			builder.add("componentType", nameOf(element));
			builder.add("objectType", modelClass != null ? modelClass : "java.lang.Void");
			return true;
		}

		if (element.i$(BaseStamp.class) && !element.i$(DisplayStamp.class)) {
			builder.add("basestamp");
			if (!element.i$(Multiple.class)) builder.add("single");
			if (element.i$(OwnerTemplateStamp.class)) builder.add("ownertemplatestamp");
			if (element.i$(DisplayStamp.class)) builder.add("displaystamp");
			String templateName = templateName(element.a$(BaseStamp.class));
			builder.add("template", templateName);
			builder.add("type", templateName);
			builder.add("generic", genericOf(element.a$(BaseStamp.class)));
			if (element.i$(OwnerTemplateStamp.class)) {
				Service.UI.Use owner = element.a$(OwnerTemplateStamp.class).owner();
				builder.add("ownerPackage", ownerTemplateStampPackage(owner));
				builder.add("ownerBox", ownerTemplateStampBox(owner));
			}
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

	private String ownerTemplateStampPackage(Service.UI.Use use) {
		return use.package$() + "." + use.name().toLowerCase() + ".box.ui.displays.templates";
	}

	private String ownerTemplateStampBox(Service.UI.Use use) {
		return use.package$() + "." + use.name().toLowerCase() + ".box." + use.name();
	}

	private Object genericOf(BaseStamp stamp) {
		if (stamp.i$(TemplateStamp.class)) {
			boolean parent = KonosGraph.isParent(context.graphName(), element.a$(TemplateStamp.class).template());
			return parent ? "<>" : "";
		}
		return "";
	}

	private String templateName(BaseStamp stamp) {
		if (stamp.i$(OtherComponents.OwnerTemplateStamp.class)) return stamp.a$(OwnerTemplateStamp.class).template();
		if (stamp.i$(OtherComponents.TemplateStamp.class)) {
			Template template = stamp.a$(TemplateStamp.class).template();
			return template != null ? template.name$() : null;
		}
		return null;
	}

	private FrameBuilder editableClassFrame() {
		FrameBuilder result = new FrameBuilder("editableClass");
		result.add("componentType", multipleComponentType(element));
		result.add("componentName", multipleComponentName(element));
		addDecoratedFrames(result, decorated);
		result.add("componentProperties", properties().add("componentClass"));
		return result;
	}

	private void addProperties(FrameBuilder builder) {
		FrameBuilder properties = properties();
		if (properties.slots() <= 0) return;
		builder.add("properties", properties());
	}

	private String multipleComponentType(C element) {
		String prefix = "io.intino.alexandria.ui.displays.components.";
		String name = multipleComponentName(element);
		if (name == null) return null;
		return element.i$(BaseStamp.Multiple.class) || element.i$(Block.Multiple.class) ? name : prefix + name;
	}

	private boolean isMultipleSpecificComponent(C element) {
		return element.i$(BaseStamp.Multiple.class) || element.i$(Block.Multiple.class);
	}

	private String multipleComponentName(C element) {
		String editable = element.i$(Editable.class) ? "Editable" : "";
		if (element.i$(Text.Multiple.class)) return "Text" + editable;
		if (element.i$(File.Multiple.class)) return "File" + editable;
		if (element.i$(Image.Multiple.class)) return "Image" + editable;
		if (element.i$(Icon.Multiple.class)) return "Icon" + editable;
		if (element.i$(DataComponents.Number.Multiple.class)) return "Number" + editable;
		if (element.i$(DataComponents.Date.Multiple.class)) return "Date" + editable;
		if (element.i$(BaseStamp.Multiple.class)) {
			if (element.i$(OtherComponents.OwnerTemplateStamp.class)) {
				OwnerTemplateStamp stamp = element.a$(OwnerTemplateStamp.class);
				return ownerTemplateStampPackage(stamp.owner()) + "." + firstUpperCase(stamp.template());
			}
			return firstUpperCase(element.a$(TemplateStamp.class).template().name$());
		}
		if (element.i$(Block.Multiple.class)) return firstUpperCase(nameOf(element));
		return null;
	}

	private String multipleObjectType(C element) {
		if (element.i$(Text.Multiple.class)) return "java.lang.String";
		if (element.i$(File.Multiple.class)) return "io.intino.alexandria.ui.File";
		if (element.i$(Image.Multiple.class)) return "io.intino.alexandria.ui.File";
		if (element.i$(Icon.Multiple.class)) return "java.net.URL";
		if (element.i$(DataComponents.Number.Multiple.class)) return "java.lang.Double";
		if (element.i$(DataComponents.Date.Multiple.class)) return "java.time.Instant";
		if (element.i$(BaseStamp.Multiple.class)) {
			String modelClass = element.i$(OwnerTemplateStamp.class) ? "java.lang.Void" : element.a$(TemplateStamp.class).template().modelClass();
			return modelClass != null ? modelClass : "java.lang.Void";
		}
		if (element.i$(Block.Multiple.class)) return "java.lang.Void";
		return null;
	}

	protected FrameBuilder resourceMethodFrame(String method, String value) {
		FrameBuilder result = new FrameBuilder("resourceMethod").add("name", method).add("value", fixResourceValue(value));
		addOwner(result);
		return result;
	}

	protected FrameBuilder parameterMethodFrame(String name, String value) {
		FrameBuilder frame = new FrameBuilder("parameter");
		frame.add("name", name);
		frame.add("value", value);
		addOwner(frame);
		return frame;
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
		else if (element.i$(Dialog.class)) componentList = element.a$(Dialog.class).componentList();
		else if (element.i$(Mold.Item.class)) componentList = element.a$(Mold.Item.class).componentList();

		if (componentList == null) return result;

		result.add("forRoot");
		componentList.forEach(c -> addComponent(c, virtualParent(), result));

		return componentList.size() > 0 ? result : null;
	}

	private String frameId() {
		return nameOf(element) + (buildChildren && virtualParent() != null ? virtualParent().name$() : "") + (owner != null ? owner.name$() : "") + target.name() + buildChildren + decorated;
	}

}
