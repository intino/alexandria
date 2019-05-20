package io.intino.konos.builder.helpers;

import io.intino.konos.builder.codegeneration.ElementReference;
import io.intino.konos.builder.utils.IdGenerator;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.temporal.TemporalCatalog;
import io.intino.tara.magritte.Layer;
import io.intino.tara.magritte.Node;
import io.intino.tara.magritte.Predicate;

import java.util.ArrayList;
import java.util.List;

import static io.intino.konos.model.graph.Utils.isUUID;
import static java.util.Collections.reverse;

public class ElementHelper {
	private static final IdGenerator generator = new IdGenerator();

	public String shortId(Layer element) {
		return shortId(element, "");
	}

	public String shortId(Layer element, String suffix) {
		return generator.shortId(nameOf(element) + suffix);
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
		if (element.i$(Display.class)) {
			String type = element.getClass().getSimpleName();
			boolean temporalCatalog = type.equalsIgnoreCase("temporalCatalog");
			return temporalCatalog ? "temporal" + element.a$(TemporalCatalog.class).type().name() + "Catalog" : type;
		}
		return element.getClass().getSimpleName();
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

	private boolean isRoot(Node node) {
		return node.owner() == null || node.owner() == node.model();
	}

	private int position(Node element, Node owner) {
		List<Node> children = owner.componentList();
		for (int pos = 0; pos< children.size(); pos++)
			if (children.get(pos).id().equals(element.id())) return pos;
		return -1;
	}

}
