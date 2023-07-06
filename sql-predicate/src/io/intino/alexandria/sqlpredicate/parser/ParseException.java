package io.intino.alexandria.sqlpredicate.parser;

public class ParseException extends Exception {
	protected static String EOL = System.getProperty("line.separator", "\n");

	public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal) {
		super(initialise(currentTokenVal, expectedTokenSequencesVal, tokenImageVal));
		currentToken = currentTokenVal;
		expectedTokenSequences = expectedTokenSequencesVal;
		tokenImage = tokenImageVal;
	}

	public ParseException() {
		super();
	}

	public ParseException(String message) {
		super(message);
	}

	public Token currentToken;

	public int[][] expectedTokenSequences;

	public String[] tokenImage;

	private static String initialise(Token currentToken, int[][] expectedTokenSequences, String[] tokenImage) {
		StringBuilder expected = new StringBuilder();
		int maxSize = 0;
		for (int[] expectedTokenSequence : expectedTokenSequences) {
			if (maxSize < expectedTokenSequence.length) maxSize = expectedTokenSequence.length;
			for (int i : expectedTokenSequence) expected.append(tokenImage[i]).append(' ');
			if (expectedTokenSequence[expectedTokenSequence.length - 1] != 0) expected.append("...");
			expected.append(EOL).append("    ");
		}
		String retval = "Encountered \"";
		Token tok = currentToken.next;
		for (int i = 0; i < maxSize; i++) {
			if (i != 0) retval += " ";
			if (tok.kind == 0) {
				retval += tokenImage[0];
				break;
			}
			retval += " " + tokenImage[tok.kind];
			retval += " \"";
			retval += add_escapes(tok.image);
			retval += " \"";
			tok = tok.next;
		}
		if (currentToken.next != null)
			retval += "\" at line " + currentToken.next.beginLine + ", column " + currentToken.next.beginColumn;
		retval += "." + EOL;


		if (expectedTokenSequences.length != 0) {
			if (expectedTokenSequences.length == 1) retval += "Was expecting:" + EOL + "    ";
			else retval += "Was expecting one of:" + EOL + "    ";
			retval += expected.toString();
		}
		return retval;
	}

	static String add_escapes(String str) {
		StringBuilder retval = new StringBuilder();
		char ch;
		for (int i = 0; i < str.length(); i++) {
			switch (str.charAt(i)) {
				case '\b':
					retval.append("\\b");
					continue;
				case '\t':
					retval.append("\\t");
					continue;
				case '\n':
					retval.append("\\n");
					continue;
				case '\f':
					retval.append("\\f");
					continue;
				case '\r':
					retval.append("\\r");
					continue;
				case '\"':
					retval.append("\\\"");
					continue;
				case '\'':
					retval.append("\\\'");
					continue;
				case '\\':
					retval.append("\\\\");
					continue;
				default:
					if ((ch = str.charAt(i)) < 0x20 || ch > 0x7e) {
						String s = "0000" + Integer.toString(ch, 16);
						retval.append("\\u").append(s.substring(s.length() - 4));
					} else retval.append(ch);
			}
		}
		return retval.toString();
	}
}
