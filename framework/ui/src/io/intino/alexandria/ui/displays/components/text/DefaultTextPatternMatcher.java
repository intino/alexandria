package io.intino.alexandria.ui.displays.components.text;

import java.util.HashMap;
import java.util.Map;

public class DefaultTextPatternMatcher implements TextPatternMatcher {

	@Override
	public Map<String, String> validationRules() {
		return new HashMap<>() {{
			put("M", "[A-Z]");
			put("m", "[a-z]");
			put("L", "[A-Za-z]");
			put("l", "[A-Za-z]| ");
			put("A", "[A-Za-z0-9]");
			put("a", "[A-Za-z0-9]| ");
			put("C", ".");
			put("c", ".| ");
			put("0", "[0-9]");
			put("9", "[0-9]| ");
			put("#", "[0-9]|+|-");
		}};
	}

	@Override
	public boolean allowIncompleteValues() {
		return false;
	}

	@Override
	public boolean addSpecialCharactersToValue(String pattern) {
		return true;
	}

	@Override
	public Character maskCharacter(String pattern) {
		return '_';
	}

}
