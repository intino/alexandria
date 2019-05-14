package io.intino.konos.builder.codegeneration.cache;

import io.intino.konos.builder.codegeneration.ElementReference;
import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.tara.magritte.Layer;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toMap;

public class ElementCache extends HashMap<ElementReference, Long> {
	private final ElementHelper elementHelper;

	private static final String CreateDate = "Konos.CreateDate";

	public ElementCache() {
		this(emptyMap());
	}

	public ElementCache(Map<String, Long> values) {
		this.elementHelper = new ElementHelper();
		init(values);
	}

	private void init(Map<String, Long> values) {
		if (!checkCreateDate(values.get(CreateDate))) return;
		putAll(values.entrySet().stream().collect(toMap(e -> ElementReference.of(e.getKey()), Entry::getValue)));
	}

	private boolean checkCreateDate(Long createDate) {
		if (createDate == null) return true;
		int diffInDays = (int)( (Instant.now().toEpochMilli() - createDate) / (1000 * 60 * 60 * 24));
		return diffInDays > 7;
	}

	public ElementCache add(Layer element) {
		long mark = element.core$().birthMark();
		put(new ElementReference().name(elementHelper.nameOf(element)).type(elementHelper.typeOf(element)), mark);
		return this;
	}

	public void addAll(Set<Entry<String, Long>> entrySet) {
		entrySet.forEach(e -> put(ElementReference.from(e.getKey()), e.getValue()));
	}

}
