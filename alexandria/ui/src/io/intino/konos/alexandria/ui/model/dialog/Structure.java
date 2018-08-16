package io.intino.konos.alexandria.ui.model.dialog;

import java.util.HashMap;

public class Structure extends HashMap<String, Value> {
	@Override
	public Value get(Object key) {
		if (!containsKey(key)) return new Value(null);
		return super.get(key);
	}
}
