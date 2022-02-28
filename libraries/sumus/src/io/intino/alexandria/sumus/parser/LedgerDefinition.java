package io.intino.alexandria.sumus.parser;


import io.intino.alexandria.sumus.Attribute;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LedgerDefinition {
	public final List<AttributeDefinition> attributes;
	public final Map<String, Map<String, String>> labels;

	public LedgerDefinition() {
		this.attributes = new ArrayList<>();
		this.labels = new HashMap<>();
	}

	public boolean contains(String name) {
		return attributes.stream().anyMatch(a->a.name.equalsIgnoreCase(name));
	}

	public static LedgerDefinition read(File file) throws IOException {
		return new Parser(Files.readString(file.toPath())).parse().get();
	}

	public static LedgerDefinition of(String content) {
		return new Parser(content).parse().get();
	}

	static class Parser {
		private final String content;
		private final LedgerDefinition definition;
		private State state;

		public Parser(String content) {
			this.content = content;
			this.definition = new LedgerDefinition();
			this.state = this::acceptAttribute;
		}

		private AttributeDefinition attribute;

		private Map<String,String> lastLabels = new HashMap<>();
		private State acceptAttribute(String token) {
			if (token.equalsIgnoreCase("attributes")) return this::acceptAttribute;
			if (token.startsWith("language")) return startNewLanguage(token.replace("language.",""));
			if (definition.contains(token)) throw new ParserException("Attribute " + token + " already exists");
			attribute = new AttributeDefinition(token);
			definition.attributes.add(attribute);
			return this::acceptAttributeType;
		}

		private State startNewLanguage(String language) {
			if (!definition.labels.containsKey(language)) definition.labels.put(language, new HashMap<>());
			lastLabels = definition.labels.get(language);
			return this::acceptLabel;
		}

		private String lastLabel;
		private State acceptLabel(String token) {
			if (token.equalsIgnoreCase("attributes")) return this::acceptAttribute;
			if (token.startsWith("language")) return startNewLanguage(token.replace("language.",""));
			lastLabel = token;
			return this::acceptLabelValue;
		}

		private State acceptLabelValue(String token) {
			lastLabels.put(lastLabel, token);
			return this::acceptLabel;
		}

		private State acceptAttributeType(String token) {
			attribute.type(typeIn(token));
			return this::acceptDimension;
		}

		private State acceptDimension(String token) {
			if (last != ']') return acceptAttribute(token);
			attribute.dimension(token);
			return this::acceptDimension;

		}

		private Attribute.Type typeIn(String token) {
			return Attribute.Type.valueOf(token);
		}

		public LedgerDefinition get() {
			return definition;
		}

		public Parser parse() {
			while (true) {
				String token = nextToken();
				if (token == null) break;
				process(token);
			}
			return this;
		}

		private void process(String token) {
			try {
				state = state.accepts(token);
			}
			catch (Exception e) {
				System.out.printf("Parse error at line %d: %s\n", line, e.getMessage());
			}
		}

		private interface State {
			State accepts(String token);
		}

		private int idx = 0;
		private int line = 1;
		private char last;
		private String nextToken() {
			StringBuilder result = new StringBuilder();
			while (idx < content.length()) {
				last = content.charAt(idx++);
				if (last == '\n') line++;
				if ("\t\n:[]".indexOf(last) < 0) {
					result.append(last);
					continue;
				}
				if (clean(result) != null) break;
			}
			return clean(result);
		}

		private String clean(StringBuilder sb) {
			String value = sb.toString().trim();
			return value.isEmpty() ? null : value;
		}

		private static class ParserException extends RuntimeException {
			public ParserException(String message) {
				super(message);
			}
		}
	}
}
