package io.intino.konos.builder.codegeneration.cache;

import io.intino.konos.builder.codegeneration.ElementReference;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.tara.magritte.Layer;
import io.intino.tara.magritte.utils.StoreAuditor;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toMap;

public class ElementCache extends HashSet<String> {
	private final ElementHelper elementHelper;
	final StoreAuditor auditor;

	public ElementCache() {
		this(null, emptyList());
	}

	public ElementCache(StoreAuditor auditor) {
		this(auditor, emptyList());
	}

	public ElementCache(StoreAuditor auditor, List<String> ids) {
		this.elementHelper = new ElementHelper();
		this.auditor = auditor;
		init(ids);
	}

	private void init(List<String> ids) {
		addAll(ids);
	}

	public ElementCache add(Layer element) {
		ElementReference reference = new ElementReference().name(elementHelper.nameOf(element)).type(elementHelper.typeOf(element)).context(ElementReference.Context.from(element));
		add(reference.toString());
		return this;
	}

	public void addAll(List<String> ids) {
		ids.forEach(this::add);
	}

	public ElementCache clone() {
		ElementCache elementCache = new ElementCache(auditor);
		elementCache.addAll(this);
		return elementCache;
	}

	public boolean isDirty(Layer element) {
		return auditor == null || (auditor.isModified(element.core$()) || auditor.isCreated(element.core$()));
	}

	public List<ElementReference> removeList() {
		Map<String, ElementReference> cache = stream().map(ElementReference::from).collect(toMap(ElementReference::name, r -> r));
		Stream<StoreAuditor.Change> removeList = auditor != null ? auditor.changeList().stream().filter(c -> c.action() == StoreAuditor.Action.Removed) : Stream.empty();
		return removeList.map(c -> cache.get(elementHelper.nameOf(c.nodeId()))).collect(Collectors.toList());
	}

}
