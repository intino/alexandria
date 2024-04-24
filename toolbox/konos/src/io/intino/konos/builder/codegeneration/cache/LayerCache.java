package io.intino.konos.builder.codegeneration.cache;

import io.intino.konos.builder.codegeneration.ElementReference;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.magritte.framework.Layer;
import io.intino.magritte.framework.utils.StoreAuditor;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;

public class LayerCache extends HashMap<String, String> {
	private final ElementHelper elementHelper;
	final StoreAuditor auditor;

	public LayerCache() {
		this(null, emptyMap());
	}

	public LayerCache(StoreAuditor auditor) {
		this(auditor, emptyMap());
	}

	public LayerCache(StoreAuditor auditor, Map<String, String> elements) {
		this.elementHelper = new ElementHelper();
		this.auditor = auditor;
		init(elements);
	}

	private void init(Map<String, String> elements) {
		putAll(elements);
	}

	public LayerCache add(Layer element) {
		ElementReference reference = new ElementReference().name(elementHelper.nameOf(element)).type(elementHelper.typeOf(element)).context(ElementReference.Context.from(element));
		put(element.core$().id(), reference.toString());
		return this;
	}

	public LayerCache clone() {
		LayerCache elementCache = new LayerCache(auditor);
		elementCache.putAll(this);
		return elementCache;
	}

	public boolean isModified(Layer element) {
		return auditor == null || (auditor.isModified(element.core$()) || auditor.isCreated(element.core$()));
	}
}
