package io.intino.alexandria.zif.grammar;

import io.intino.alexandria.zif.Zif;

import java.util.*;
import java.util.regex.Pattern;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

public class PropertyChecker {
	private final Property property;
	private final List<Checker> checkers;

	public PropertyChecker(Property property) {
		this.property = property;
		this.checkers = add(identifierChecker(), grammarChecker());
	}

	private List<Checker> add(Checker... checkers) {
		return Arrays.stream(checkers).filter(Objects::nonNull).collect(toList());
	}

	public PropertyChecker set(Zif zif) {
		checkers.add(uniqueChecker(zif));
		return this;
	}

	private Checker grammarChecker() {
		if (property.grammar() == null) return null;
		return new Grammar(property.grammar())::parse;
	}

	private Checker uniqueChecker(Zif zif) {
		if (!property.isIdentifier()) return null;
		return v -> zif.search(Property.Identifier, v).size() >= 1 ? "Identifier already exists" : null;
	}

	private Checker identifierChecker() {
		if (!property.isIdentifier()) return null;
		return v -> v.contains(" ") ? "Identifier can not contain blank spaces" : null;
	}

	public String check(String value) {
		for (Checker checker : checkers) {
			String check = checker.check(value);
			if (check != null) return check;
		}
		return null;
	}

	public static class Grammar {
		private static final String Expected = "Expected ";
		private final List<String[]> options;
		private static final Pattern email = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
		private static final Pattern date = Pattern.compile("^((19|2[0-9])[0-9]{2})(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])$");

		public Grammar(String definition) {
			this.options = load(definition);
		}

		public String parse(String text) {
			return parse(text,text.contains("\n"));
		}

		String parse(String text, boolean multiple) {
			return multiple ? parseMultiple(text) : parseSingle(text);
		}

		private String parseSingle(String text) {
			return parse(tokenize(text));
		}

		private String parseMultiple(String text) {
			String[] sentences = text.split("\n");
			for (int i = 0; i < sentences.length; i++) {
				String result = parse(tokenize(sentences[i]));
				if (result == null) continue;
				return sentences.length > 1 ? result + " in line " + (i+1) : result;
			}
			return null;
		}

		private String parse(String[] value) {
			List<Error> errors = new ArrayList<>();
			for (String[] option : options) {
				Error error = match(value, option);
				if (error == null) return null;
				errors.add(error);
			}
			if (isAnyExpectedToken(errors)) return Expected + expected(errors);
			Integer index = Math.min(indexOfMax(errors), options.size()-1);
			return "'" + value[indexOfMax(errors)] + "' is an invalid " + errors.get(index).token;
		}

		private String expected(List<Error> errors) {
			StringJoiner joiner = new StringJoiner(" | ");
			for (int i = 0; i < options.size(); i++) {
				Error error = errors.get(i);
				if (error.type == Type.invalidToken) continue;
				String s = options.get(i)[error.position];
				joiner.add(s);
			}
			return joiner.toString();
		}

		private boolean isAnyExpectedToken(List<Error> errors) {
			return errors.stream().anyMatch(s->s.type == Type.expectedToken);
		}

		private Integer indexOfMax(List<Error> errors) {
			return errors.stream()
					.filter(e->e.type == Type.invalidToken)
					.map(e -> e.position)
					.max(comparingInt(o -> o))
					.orElse(0);
		}

		private Error match(String[] value, String[] expected) {
			for (int i = 0; i < value.length && i <expected.length ; i++) {
				Error error = match(i, value[i], expected[i]);
				if (error != null) return error;
			}
			if (value.length == expected.length) return null;
			return value.length < expected.length ?
					new Error(Type.expectedToken, tokenIn(expected[value.length]), value.length) :
					new Error(Type.invalidToken, Token.constant, value.length-1);
		}

		private Error match(int position, String value, String expected) {
			Token token = tokenIn(expected);
			Pattern pattern = patternOf(token);
			boolean match = pattern == null ? value.equalsIgnoreCase(expected) : pattern.matcher(value).matches();
			return match ? null : new Error(Type.invalidToken, token, position);
		}

		private Token tokenIn(String expected) {
			return isVariable(expected) ?
					Token.valueOf(expected.substring(1).toLowerCase()) :
					Token.constant;
		}

		private Pattern patternOf(Token token) {
			return token == Token.date ? date : token == Token.email ? email : null;
		}

		private boolean isVariable(String token) {
			return token.charAt(0) == '#';
		}

		private List<String[]> load(String definition) {
			ArrayList<String[]> list = new ArrayList<>();
			for (String option : definition.split("\\|"))
				list.add(tokenize(option));
			return list;
		}

		private String[] tokenize(String option) {
			return Arrays.stream(option.split(" "))
					.filter(s->!s.isEmpty())
					.toArray(String[]::new);
		}

		public boolean isEmpty() {
			return options.size() == 0;
		}

	}

	public static class Error {
		final Token token;
		final Type type;
		final int position;

		Error(Type type, Token token, int position) {
			this.token = token;
			this.type = type;
			this.position = position;
		}
	}

	private enum Token {
		constant, date, email
	}

	private enum Type {
		invalidToken, expectedToken
	}

	private interface Checker {
		String check(String value);
	}


}
