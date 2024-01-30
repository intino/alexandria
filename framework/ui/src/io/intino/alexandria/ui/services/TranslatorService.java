package io.intino.alexandria.ui.services;

import io.intino.alexandria.ui.services.translator.Dictionary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslatorService {
	private final Map<String, Dictionary> dictionaries = new HashMap<>();

	private static final Map<String, String> FamilyLanguages = Map.of("mx", "es");
	public String translate(String word, String language) {
		String selectedLanguage = locateDictionaryLanguage(language);
		Dictionary languageDictionary = dictionaries.get(selectedLanguage);
		return languageDictionary != null && languageDictionary.containsKey(word) ? languageDictionary.get(word) : word;
	}

	public void addAll(List<Dictionary> dictionaries) {
		dictionaries.forEach(d -> {
			if (!this.dictionaries.containsKey(d.language())) this.dictionaries.put(d.language(), new Dictionary());
			this.dictionaries.get(d.language()).putAll(d);
		});
	}

	private String locateDictionaryLanguage(String language) {
		if (dictionaries.containsKey(language)) return language;
		String familyLanguage = FamilyLanguages.getOrDefault(language, "en");
		return dictionaries.containsKey(familyLanguage) ? familyLanguage : "en";
	}
}
