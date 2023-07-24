package io.intino.alexandria.ui.displays.components.text;

import java.util.Map;

public interface TextPatternMatcher {
	Map<String, String> validationRules();
	boolean allowIncompleteValues();
	boolean addSpecialCharactersToValue(String pattern);
	Character maskCharacter(String pattern);
}
