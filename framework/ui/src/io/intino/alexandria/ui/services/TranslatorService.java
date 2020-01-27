package io.intino.alexandria.ui.services;

import io.intino.alexandria.ui.services.translator.Dictionary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslatorService {
	private Map<String, Dictionary> dictionaries = new HashMap<>();

	public String translate(String word, String language) {
		language = dictionaries.containsKey(language) ? language : "en";
		Dictionary languageDictionary = dictionaries.get(language);
		return languageDictionary != null && languageDictionary.containsKey(word) ? languageDictionary.get(word) : word;
	}

	public void addAll(List<Dictionary> dictionaries) {
		dictionaries.forEach(d -> {
			if (!this.dictionaries.containsKey(d.language())) this.dictionaries.put(d.language(), new Dictionary());
			this.dictionaries.get(d.language()).putAll(d);
		});
	}
}
