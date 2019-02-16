package io.intino.konos.builder.utils;

import org.hashids.Hashids;

import java.util.HashMap;
import java.util.Map;

public class IdGenerator {
	private Map<String, Integer> idsMap = new HashMap<>();
	private int seed = 0;

	private static final Hashids generator = new Hashids("");

	public String shortId(String name) {
		int seed = idsMap.containsKey(name) ? idsMap.get(name) : this.seed++;
		String result = generator.encode(seed);
		idsMap.put(name, seed);
		return result;
	}

}
