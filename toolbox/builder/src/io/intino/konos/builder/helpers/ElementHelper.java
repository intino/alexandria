package io.intino.konos.builder.helpers;

import cottons.utils.StringHelper;
import io.intino.konos.builder.codegeneration.ElementReference;
import io.intino.konos.model.ActionableComponents;
import io.intino.konos.model.InteractionComponents;
import io.intino.konos.model.KonosGraph;
import io.intino.magritte.framework.Layer;
import io.intino.magritte.framework.Node;
import io.intino.magritte.framework.Predicate;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.reverse;

public class ElementHelper {
	private static final Map<Layer, String> typeMap = Collections.synchronizedMap(new HashMap<>());

	public String shortId(Layer element) {
		return shortId(element, "");
	}

	public String shortId(Layer element, String suffix) {
		String name = nameOf(element);
		if (isNamed(name)) {
			if (!isRoot(element.core$()) || isEmbedded(element.core$())) name = generatedName(element, name);
		} else name = generatedName(element, name);
		return name + suffix;
	}

	public static String conceptOf(Class<?> aClass) {
		return aClass.getName().substring(aClass.getPackageName().length()+1);
	}

	private String generatedName(Layer element, String name) {
		return ("a" + (anonymousOwner(element) + "_" + name).hashCode()).replace("-", "_");
	}

	public String nameOf(Layer element) {
		String name = element.name$();
		if (name.contains("_")) {
			String rootName = rootNameOf(element);
			name = name.replace(rootName, shortName(StringHelper.camelCaseToSnakeCase(rootName)));
		}
		return name;
	}

	private String shortName(String snakeName) {
		String result = Arrays.stream(snakeName.split("-")).map(this::initials).collect(Collectors.joining(""));
		return "";//result.length() > 10 ? result.substring(0, 10) : result;
	}

	private String initials(String name) {
		String initials = name.replaceAll("[a-z]", "");
		if (!initials.isEmpty()) return initials;
		return name.length() > 2 ? name.substring(0, 3) : name;
	}

	private String anonymousOwner(Layer element) {
		Node owner = element.core$().owner();
		StringBuilder name = new StringBuilder();
		while (owner != null && isNamed(owner.name())) {
			name.insert(0, owner.name() + "_");
			owner = owner.owner();
		}
		if (owner != null) name.insert(0, withOutHashCode(owner.name()) + "_");
		String id = name.toString();
		return id.substring(0, id.length() - 1);
	}

	private String withOutHashCode(String name) {
		return name.substring(0, name.lastIndexOf("_"));
	}

	private boolean isNamed(String name) {
		if (!name.contains("_")) return true;
		String[] s = name.split("_");
		return s.length != 4;
	}

	public String nameOf(String id) {
		return Predicate.nameOf(id);
	}

	public String typeOf(Layer element) {
		if (!typeMap.containsKey(element)) {
			String type = element.i$(ActionableComponents.Actionable.class) ?
					typeOfActionable(element.a$(ActionableComponents.Actionable.class)) :
					element.getClass().getSimpleName();
			typeMap.put(element, type);
		}
		return typeMap.get(element);
	}

	private String typeOfActionable(ActionableComponents.Actionable actionable) {
		String result = ActionableComponents.Actionable.Action.class.getSimpleName();
		if (actionable.isAction()) {
			String context = actionable.asAction().context() == ActionableComponents.Actionable.Action.Context.Selection ? "Selection" : "";
			result = context + ActionableComponents.Actionable.Action.class.getSimpleName();
		}
		if (actionable.isOpenDrawer()) result = ActionableComponents.Actionable.OpenDrawer.class.getSimpleName();
		if (actionable.isCloseDrawer()) result = ActionableComponents.Actionable.CloseDrawer.class.getSimpleName();
		if (actionable.isOpenPage()) result = ActionableComponents.Actionable.OpenPage.class.getSimpleName();
		if (actionable.isOpenSite()) result = ActionableComponents.Actionable.OpenSite.class.getSimpleName();
		if (actionable.isOpenBlock()) result = ActionableComponents.Actionable.OpenBlock.class.getSimpleName();
		if (actionable.isCloseBlock()) result = ActionableComponents.Actionable.CloseBlock.class.getSimpleName();
		if (actionable.isOpenDialog()) result = ActionableComponents.Actionable.OpenDialog.class.getSimpleName();
		if (actionable.isOpenLayer()) result = ActionableComponents.Actionable.OpenLayer.class.getSimpleName();
		if (actionable.isCloseLayer()) result = ActionableComponents.Actionable.CloseLayer.class.getSimpleName();
		if (actionable.isOpenPopover()) result = ActionableComponents.Actionable.OpenPopover.class.getSimpleName();
		if (actionable.isCloseDialog()) result = ActionableComponents.Actionable.CloseDialog.class.getSimpleName();
		if (actionable.isCopyToClipboard()) result = ActionableComponents.Actionable.CopyToClipboard.class.getSimpleName();
		if (actionable.isSignText()) result = ActionableComponents.Actionable.SignText.class.getSimpleName();
		if (actionable.isSignDocument()) result = ActionableComponents.Actionable.SignDocument.class.getSimpleName();
		if (actionable.isSelectPreviousItem())
			result = ActionableComponents.Actionable.SelectPreviousItem.class.getSimpleName();
		if (actionable.isSelectNextItem())
			result = ActionableComponents.Actionable.SelectNextItem.class.getSimpleName();
		if (actionable.isExport()) result = ActionableComponents.Actionable.Export.class.getSimpleName();
		if (actionable.isDownload()) {
			String context = actionable.asDownload().context() == ActionableComponents.Actionable.Download.Context.Selection ? "Selection" : "";
			result = ActionableComponents.Actionable.Download.class.getSimpleName() + context;
		}
		return result;
	}

	public String ownerId(Layer element) {
		Node owner = element.core$().owner();
		List<String> result = new ArrayList<>();
		while (owner != null) {
			result.add(shortId(owner.as(Layer.class)));
			owner = owner.owner();
		}
		reverse(result);
		return String.join(".", result);
	}

	public ElementReference referenceOf(Layer element) {
		return ElementReference.of(nameOf(element), typeOf(element), ElementReference.Context.from(element));
	}

	private String generateName(Layer element) {
		return generateName(element.core$(), "");
	}

	private String generateName(Node element, String name) {
		Node owner = element.owner();
		if (isRoot(owner)) return owner.name() + position(element, owner);
		return generateName(owner, name) + position(element, owner);
	}

	private String rootPathOf(Layer element) {
		return element.core$().rootNodeId();
	}

	private String rootNameOf(Layer element) {
		String path = rootPathOf(element);
		return path != null ? path.split("#")[0] : "";
	}

	private int position(Node element, Node owner) {
		List<Node> children = owner.componentList();
		for (int pos = 0; pos < children.size(); pos++)
			if (children.get(pos).id().equals(element.id())) return pos;
		return -1;
	}

	public static boolean isRoot(Layer element) {
		return element != null && isRoot(element.core$());
	}

	private static boolean isRoot(Node node) {
		return node.owner() == null || node.owner() == node.model();
	}

	private static boolean isEmbedded(Node node) {
		KonosGraph graph = node.graph().as(KonosGraph.class);
		return graph.collectionsDisplays(null).stream().map(Layer::core$).collect(Collectors.toList()).contains(node);
	}
}
