package io.intino.konos.builder.helpers;

import io.intino.konos.builder.codegeneration.ElementReference;
import io.intino.konos.builder.utils.IdGenerator;
import io.intino.konos.model.graph.InteractionComponents;
import io.intino.tara.magritte.Layer;
import io.intino.tara.magritte.Node;
import io.intino.tara.magritte.Predicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.intino.konos.model.graph.Utils.isUUID;
import static java.util.Collections.reverse;

public class ElementHelper {
	private static final IdGenerator generator = new IdGenerator();
	private static Map<Layer, String> typeMap = new HashMap<>();

	public String shortId(Layer element) {
		return shortId(element, "");
	}

	public String shortId(Layer element, String suffix) {
		return generator.shortId(rootNameOf(element) + nameOf(element) + suffix);
	}

	public String nameOf(Layer element) {
		String result = element.name$();
		if (!isUUID(result)) return result;
		return generateName(element);
	}

	public String nameOf(String id) {
		return Predicate.nameOf(id);
	}

	public String typeOf(Layer element) {
		if (!typeMap.containsKey(element)) {
			String type = element.i$(InteractionComponents.Actionable.class) ?
							typeOfActionable(element.a$(InteractionComponents.Actionable.class)) :
							element.getClass().getSimpleName();

			typeMap.put(element, type);
		}
		return typeMap.get(element);
	}

	private String typeOfActionable(InteractionComponents.Actionable actionable) {
		String result = InteractionComponents.Actionable.Action.class.getSimpleName();
		if (actionable.isAction()) {
			String context = actionable.asAction().context() == InteractionComponents.Actionable.Action.Context.Selection ? "Selection" : "";
			result = context + InteractionComponents.Actionable.Action.class.getSimpleName();
		}
		if (actionable.isOpenDrawer()) result = InteractionComponents.Actionable.OpenDrawer.class.getSimpleName();
		if (actionable.isCloseDrawer()) result = InteractionComponents.Actionable.CloseDrawer.class.getSimpleName();
		if (actionable.isOpenPage()) result = InteractionComponents.Actionable.OpenPage.class.getSimpleName();
		if (actionable.isOpenSite()) result = InteractionComponents.Actionable.OpenSite.class.getSimpleName();
		if (actionable.isOpenBlock()) result = InteractionComponents.Actionable.OpenBlock.class.getSimpleName();
		if (actionable.isOpenDialog()) result = InteractionComponents.Actionable.OpenDialog.class.getSimpleName();
		if (actionable.isCloseDialog()) result = InteractionComponents.Actionable.CloseDialog.class.getSimpleName();
		if (actionable.isExport()) result = InteractionComponents.Actionable.Export.class.getSimpleName();
		if (actionable.isDownload()) {
			String context = actionable.asDownload().context() == InteractionComponents.Actionable.Download.Context.Selection ? "Selection" : "";
			result = InteractionComponents.Actionable.Download.class.getSimpleName() + context;
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

	public static boolean isRoot(Layer element) {
		return isRoot(element.core$());
	}

	private String generateName(Layer element) {
		return generateName(element.core$(), "");
	}

	private String generateName(Node element, String name) {
		Node owner = element.owner();
		if (isRoot(owner)) return owner.name() + position(element, owner);
		return generateName(owner, name) + position(element, owner);
	}

	private String rootNameOf(Layer element) {
		return element.core$().rootNodeId();
	}

	private static boolean isRoot(Node node) {
		return node.owner() == null || node.owner() == node.model();
	}

	private int position(Node element, Node owner) {
		List<Node> children = owner.componentList();
		for (int pos = 0; pos< children.size(); pos++)
			if (children.get(pos).id().equals(element.id())) return pos;
		return -1;
	}

}
