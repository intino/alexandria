package io.intino.alexandria.sqlpredicate.parser;

public class TokenMgrError extends Error {
	public static final int LEXICAL_ERROR = 0;
	public static final int STATIC_LEXER_ERROR = 1;
	public static final int INVALID_LEXICAL_STATE = 2;
	public static final int LOOP_DETECTED = 3;
	int errorCode;

	protected static String addEscapes(String str) {
		StringBuilder retval = new StringBuilder();
		char ch;
		for (int i = 0; i < str.length(); i++)
			switch (str.charAt(i)) {
				case '\b' -> retval.append("\\b");
				case '\t' -> retval.append("\\t");
				case '\n' -> retval.append("\\n");
				case '\f' -> retval.append("\\f");
				case '\r' -> retval.append("\\r");
				case '\"' -> retval.append("\\\"");
				case '\'' -> retval.append("\\\'");
				case '\\' -> retval.append("\\\\");
				default -> {
					if ((ch = str.charAt(i)) < 0x20 || ch > 0x7e) {
						String s = "0000" + Integer.toString(ch, 16);
						retval.append("\\u" + s.substring(s.length() - 4, s.length()));
					} else {
						retval.append(ch);
					}
				}
			}
		return retval.toString();
	}


	protected static String LexicalErr(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, int curChar) {
		return ("Lexical error at line " +
				errorLine + ", column " +
				errorColumn + ".  Encountered: " +
				(EOFSeen ? "<EOF>" : ("'" + addEscapes(String.valueOf(curChar)) + "' (" + curChar + "),")) + //
				(errorAfter == null || errorAfter.length() == 0 ? "" : " after prefix \"" + addEscapes(errorAfter) + "\"")) + //
				(lexState == 0 ? "" : " (in lexical state " + lexState + ")");
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

	public TokenMgrError(String message, int reason) {
		super(message);
		errorCode = reason;
	}

	public TokenMgrError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, int curChar, int reason) {
		this(LexicalErr(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), reason);
	}
}
