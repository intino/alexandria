package io.intino.alexandria.sqlpredicate.parser;

public interface PredicateParserConstants {

	/**
	 * End of File.
	 */
	int EOF = 0;
	/**
	 * RegularExpression Id.
	 */
	int LINE_COMMENT = 6;
	/**
	 * RegularExpression Id.
	 */
	int BLOCK_COMMENT = 7;
	/**
	 * RegularExpression Id.
	 */
	int NOT = 8;
	/**
	 * RegularExpression Id.
	 */
	int AND = 9;
	/**
	 * RegularExpression Id.
	 */
	int OR = 10;
	/**
	 * RegularExpression Id.
	 */
	int BETWEEN = 11;
	/**
	 * RegularExpression Id.
	 */
	int LIKE = 12;
	/**
	 * RegularExpression Id.
	 */
	int ESCAPE = 13;
	/**
	 * RegularExpression Id.
	 */
	int IN = 14;
	/**
	 * RegularExpression Id.
	 */
	int IS = 15;
	/**
	 * RegularExpression Id.
	 */
	int TRUE = 16;
	/**
	 * RegularExpression Id.
	 */
	int FALSE = 17;
	/**
	 * RegularExpression Id.
	 */
	int NULL = 18;
	/**
	 * RegularExpression Id.
	 */
	int XPATH = 19;
	/**
	 * RegularExpression Id.
	 */
	int XQUERY = 20;
	/**
	 * RegularExpression Id.
	 */
	int DECIMAL_LITERAL = 21;
	/**
	 * RegularExpression Id.
	 */
	int HEX_LITERAL = 22;
	/**
	 * RegularExpression Id.
	 */
	int OCTAL_LITERAL = 23;
	/**
	 * RegularExpression Id.
	 */
	int FLOATING_POINT_LITERAL = 24;
	/**
	 * RegularExpression Id.
	 */
	int EXPONENT = 25;
	/**
	 * RegularExpression Id.
	 */
	int STRING_LITERAL = 26;
	/**
	 * RegularExpression Id.
	 */
	int ID = 27;

	/**
	 * Lexical state.
	 */
	int DEFAULT = 0;

	/**
	 * Literal token values.
	 */
	String[] tokenImage = {
			"<EOF>",
			"\" \"",
			"\"\\t\"",
			"\"\\n\"",
			"\"\\r\"",
			"\"\\f\"",
			"<LINE_COMMENT>",
			"<BLOCK_COMMENT>",
			"\"NOT\"",
			"\"AND\"",
			"\"OR\"",
			"\"BETWEEN\"",
			"\"LIKE\"",
			"\"ESCAPE\"",
			"\"IN\"",
			"\"IS\"",
			"\"TRUE\"",
			"\"FALSE\"",
			"\"NULL\"",
			"\"XPATH\"",
			"\"XQUERY\"",
			"<DECIMAL_LITERAL>",
			"<HEX_LITERAL>",
			"<OCTAL_LITERAL>",
			"<FLOATING_POINT_LITERAL>",
			"<EXPONENT>",
			"<STRING_LITERAL>",
			"<ID>",
			"\"=\"",
			"\"<>\"",
			"\">\"",
			"\">=\"",
			"\"<\"",
			"\"<=\"",
			"\"(\"",
			"\",\"",
			"\")\"",
			"\"+\"",
			"\"-\"",
			"\"*\"",
			"\"/\"",
			"\"%\"",
	};

}
