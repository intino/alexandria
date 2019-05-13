package io.intino.konos.builder.codegeneration.cache;

import io.intino.konos.builder.helpers.ElementHelper;
import io.intino.tara.magritte.Layer;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyMap;

public class Cache extends HashMap<String, Long> {
	private final ElementHelper elementHelper;

	private static final String CreateDate = "Konos.CreateDate";

	public Cache() {
		this(emptyMap());
	}

	public Cache(Map<String, Long> values) {
		this.elementHelper = new ElementHelper();
		init(values);
	}

	private void init(Map<String, Long> values) {
		if (!checkCreateDate(values.get(CreateDate))) return;
		putAll(values);
	}

	private boolean checkCreateDate(Long createDate) {
		int diffInDays = (int)( (Instant.now().toEpochMilli() - createDate) / (1000 * 60 * 60 * 24));
		return diffInDays > 7;
	}

	public Cache add(Layer element) {
		long mark = element.core$().birthMark();
		put(elementHelper.nameOf(element), mark);
		return this;
	}

	public void addAll(Set<Entry<String, Long>> entrySet) {
		entrySet.forEach(e -> put(e.getKey(), e.getValue()));
	}
}
