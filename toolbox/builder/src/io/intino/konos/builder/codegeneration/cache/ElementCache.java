package io.intino.konos.builder.codegeneration.cache;

import io.intino.konos.builder.codegeneration.ElementReference;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.tara.magritte.Layer;
import io.intino.tara.magritte.utils.StoreAuditor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;

public class ElementCache extends HashMap<String, String> {
	private final ElementHelper elementHelper;
	final StoreAuditor auditor;

	public ElementCache() {
		this(null, emptyMap());
	}

	public ElementCache(StoreAuditor auditor) {
		this(auditor, emptyMap());
	}

	public ElementCache(StoreAuditor auditor, Map<String, String> elements) {
		this.elementHelper = new ElementHelper();
		this.auditor = auditor;
		init(elements);
	}

	private void init(Map<String, String> elements) {
		putAll(elements);
	}

	public ElementCache add(Layer element) {
		ElementReference reference = new ElementReference().name(elementHelper.nameOf(element)).type(elementHelper.typeOf(element)).context(ElementReference.Context.from(element));
		put(element.core$().id(), reference.toString());
		return this;
	}

	public ElementCache clone() {
		ElementCache elementCache = new ElementCache(auditor);
		elementCache.putAll(this);
		return elementCache;
	}

	public boolean isDirty(Layer element) {
		return auditor == null || (auditor.isModified(element.core$()) || auditor.isCreated(element.core$()));
	}

	public List<ElementReference> removeList() {
		List<StoreAuditor.Change> removeList = auditor != null ? auditor.changeList().stream().filter(c -> c.action() == StoreAuditor.Action.Removed).collect(toList()) : emptyList();
		return removeList.stream().filter(c -> containsKey(c.nodeId())).map(c -> ElementReference.from(get(c.nodeId()))).collect(toList());
	}

}
