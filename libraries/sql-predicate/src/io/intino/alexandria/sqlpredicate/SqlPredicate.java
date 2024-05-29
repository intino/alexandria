package io.intino.alexandria.sqlpredicate;

import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.sqlpredicate.context.EvaluationContext;
import io.intino.alexandria.sqlpredicate.expressions.*;
import io.intino.alexandria.sqlpredicate.expressions.functions.FilterFunction;
import io.intino.alexandria.sqlpredicate.parser.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static io.intino.alexandria.sqlpredicate.parser.PredicateParserConstants.*;

public class SqlPredicate {
	private static final Map<String, Object> cache = Collections.synchronizedMap(new LRUCache<>(100));
	private static final String CONVERT_STRING_EXPRESSIONS_PREFIX = "convert_string_expressions:";

	public static Predicate<EvaluationContext> parse(String sql) throws InvalidExpressionException {
		if (sql == null || sql.trim().isEmpty()) return m -> true;
		BooleanExpression expression = parseSQL(sql);
		return context -> {
			try {
				return expression.matches(context);
			} catch (Exception e) {
				Logger.error(e);
				return false;
			}
		};
	}

	public static void registerFunction(String name, FilterFunction function) {
		FunctionCallExpression.registerFunction(name, function);
	}


	static BooleanExpression parseSQL(String sql) throws InvalidExpressionException {
		Object result = cache.get(sql);
		if (result instanceof InvalidExpressionException) throw (InvalidExpressionException) result;
		else if (result instanceof BooleanExpression) return (BooleanExpression) result;
		else {
			boolean convertStringExpressions = false;
			if (sql.startsWith(CONVERT_STRING_EXPRESSIONS_PREFIX)) {
				convertStringExpressions = true;
				sql = sql.substring(CONVERT_STRING_EXPRESSIONS_PREFIX.length());
			}
			if (convertStringExpressions) ComparisonExpression.CONVERT_STRING_EXPRESSIONS.set(true);
			try {
				BooleanExpression e = new SqlPredicate(sql).parse();
				cache.put(sql, e);
				return e;
			} catch (InvalidExpressionException t) {
				cache.put(sql, t);
				throw t;
			} finally {
				if (convertStringExpressions) {
					ComparisonExpression.CONVERT_STRING_EXPRESSIONS.remove();
				}
			}
		}
	}


	public static void clearCache() {
		cache.clear();
	}

	private String sql;

	protected SqlPredicate(String sql) {
		this(new StringReader(sql));
		this.sql = sql;
	}

	protected BooleanExpression parse() throws InvalidExpressionException {
		try {
			return this.JmsSelector();
		} catch (Throwable e) {
			throw (InvalidExpressionException) new InvalidExpressionException(sql).initCause(e);
		}
	}

	private static BooleanExpression asBooleanExpression(Expression value) throws ParseException {
		if (value instanceof BooleanExpression) return (BooleanExpression) value;
		if (value instanceof PropertyExpression) return UnaryExpression.createBooleanCast(value);
		throw new ParseException("Expression will not result in a boolean value: " + value);
	}

	private BooleanExpression JmsSelector() throws ParseException {
		Expression left = orExpression();
		jj_consume_token(0);
		return asBooleanExpression(left);
	}

	private Expression orExpression() throws ParseException {
		Expression left = andExpression();
		Expression right;
		while (((jjNtk == -1) ? jj_ntk_f() : jjNtk) == OR) {
			jj_consume_token(OR);
			right = andExpression();
			left = LogicExpression.createOR(asBooleanExpression(left), asBooleanExpression(right));
		}
		return left;
	}

	private Expression andExpression() throws ParseException {
		Expression left;
		Expression right;
		left = equalityExpression();
		while (((jjNtk == -1) ? jj_ntk_f() : jjNtk) == AND) {
			jj_consume_token(AND);
			right = equalityExpression();
			left = LogicExpression.createAND(asBooleanExpression(left), asBooleanExpression(right));
		}
		return left;
	}

	private Expression equalityExpression() throws ParseException {
		Expression left;
		Expression right;
		left = comparisonExpression();
		label_3:
		while (true) {
			switch ((jjNtk == -1) ? jj_ntk_f() : jjNtk) {
				case IS:
				case 28:
				case 29: {
					break;
				}
				default:
					break label_3;
			}
			switch ((jjNtk == -1) ? jj_ntk_f() : jjNtk) {
				case 28 -> {
					jj_consume_token(28);
					right = comparisonExpression();
					left = ComparisonExpression.createEqual(left, right);
				}
				case 29 -> {
					jj_consume_token(29);
					right = comparisonExpression();
					left = ComparisonExpression.createNotEqual(left, right);
				}
				default -> {
					if (jj_2_1()) {
						jj_consume_token(IS);
						jj_consume_token(NULL);
						left = ComparisonExpression.createIsNull(left);
					} else {
						if (((jjNtk == -1) ? jj_ntk_f() : jjNtk) == IS) {
							jj_consume_token(IS);
							jj_consume_token(NOT);
							jj_consume_token(NULL);
							left = ComparisonExpression.createIsNotNull(left);
						} else {
							jj_consume_token(-1);
							throw new ParseException();
						}
					}
				}
			}
		}
		return left;
	}

	private Expression comparisonExpression() throws ParseException {
		Expression left;
		Expression right;
		Expression low;
		Expression high;
		String t, u;
		List<String> list;
		left = addExpression();
		label_4:
		while (true) {
			switch ((jjNtk == -1) ? jj_ntk_f() : jjNtk) {
				case NOT:
				case BETWEEN:
				case LIKE:
				case IN:
				case 30:
				case 31:
				case 32:
				case 33:
					break;
				default:
					break label_4;
			}
			switch ((jjNtk == -1) ? jj_ntk_f() : jjNtk) {
				case 30 -> {
					jj_consume_token(30);
					right = addExpression();
					left = ComparisonExpression.createGreaterThan(left, right);
				}
				case 31 -> {
					jj_consume_token(31);
					right = addExpression();
					left = ComparisonExpression.createGreaterThanEqual(left, right);
				}
				case 32 -> {
					jj_consume_token(32);
					right = addExpression();
					left = ComparisonExpression.createLessThan(left, right);
				}
				case 33 -> {
					jj_consume_token(33);
					right = addExpression();
					left = ComparisonExpression.createLessThanEqual(left, right);
				}
				case LIKE -> {
					u = null;
					jj_consume_token(LIKE);
					t = stringLiteral();
					if (((jjNtk == -1) ? jj_ntk_f() : jjNtk) == ESCAPE) {
						jj_consume_token(ESCAPE);
						u = stringLiteral();
					}
					left = ComparisonExpression.createLike(left, t, u);
				}
				default -> {
					if (jj_2_2()) {
						u = null;
						jj_consume_token(NOT);
						jj_consume_token(LIKE);
						t = stringLiteral();
						if (((jjNtk == -1) ? jj_ntk_f() : jjNtk) == ESCAPE) {
							jj_consume_token(ESCAPE);
							u = stringLiteral();
						}
						left = ComparisonExpression.createNotLike(left, t, u);
					} else {
						if (((jjNtk == -1) ? jj_ntk_f() : jjNtk) == BETWEEN) {
							jj_consume_token(BETWEEN);
							low = addExpression();
							jj_consume_token(AND);
							high = addExpression();
							left = ComparisonExpression.createBetween(left, low, high);
						} else {
							if (jj_2_3()) {
								jj_consume_token(NOT);
								jj_consume_token(BETWEEN);
								low = addExpression();
								jj_consume_token(AND);
								high = addExpression();
								left = ComparisonExpression.createNotBetween(left, low, high);
							} else {
								if (((jjNtk == -1) ? jj_ntk_f() : jjNtk) == IN) {
									jj_consume_token(IN);
									jj_consume_token(34);
									t = stringLiteral();
									list = new ArrayList<>();
									list.add(t);
									while (((jjNtk == -1) ? jj_ntk_f() : jjNtk) == 35) {
										jj_consume_token(35);
										t = stringLiteral();
										list.add(t);
									}
									jj_consume_token(36);
									left = ComparisonExpression.createInFilter(left, list);
								} else {
									if (jj_2_4()) {
										jj_consume_token(NOT);
										jj_consume_token(IN);
										jj_consume_token(34);
										t = stringLiteral();
										list = new ArrayList<>();
										list.add(t);
										while (((jjNtk == -1) ? jj_ntk_f() : jjNtk) == 35) {
											jj_consume_token(35);
											t = stringLiteral();
											list.add(t);
										}
										jj_consume_token(36);
										left = ComparisonExpression.createNotInFilter(left, list);
									} else {
										jj_consume_token(-1);
										throw new ParseException();
									}
								}
							}
						}
					}
				}
			}
		}
		return left;
	}

	private Expression addExpression() throws ParseException {
		Expression left;
		Expression right;
		left = multExpr();
		while (jj_2_5()) {
			switch ((jjNtk == -1) ? jj_ntk_f() : jjNtk) {
				case 37 -> {
					jj_consume_token(37);
					right = multExpr();
					left = ArithmeticExpression.createPlus(left, right);
				}
				case 38 -> {
					jj_consume_token(38);
					right = multExpr();
					left = ArithmeticExpression.createMinus(left, right);
				}
				default -> {
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
		}
		return left;
	}

	private Expression multExpr() throws ParseException {
		Expression left;
		Expression right;
		left = unaryExpr();
		label_8:
		while (true) {
			switch ((jjNtk == -1) ? jj_ntk_f() : jjNtk) {
				case 39:
				case 40:
				case 41:
					break;
				default:
					break label_8;
			}
			switch ((jjNtk == -1) ? jj_ntk_f() : jjNtk) {
				case 39 -> {
					jj_consume_token(39);
					right = unaryExpr();
					left = ArithmeticExpression.createMultiply(left, right);
				}
				case 40 -> {
					jj_consume_token(40);
					right = unaryExpr();
					left = ArithmeticExpression.createDivide(left, right);
				}
				case 41 -> {
					jj_consume_token(41);
					right = unaryExpr();
					left = ArithmeticExpression.createMod(left, right);
				}
				default -> {
					jj_consume_token(-1);
					throw new ParseException();
				}
			}
		}
		return left;
	}

	private Expression unaryExpr() throws ParseException {
		Expression left;
		if (jj_2_6()) {
			jj_consume_token(37);
			left = unaryExpr();
		} else {
			switch ((jjNtk == -1) ? jj_ntk_f() : jjNtk) {
				case 38 -> {
					jj_consume_token(38);
					left = unaryExpr();
					left = UnaryExpression.createNegate(left);
				}
				case NOT -> {
					jj_consume_token(NOT);
					left = unaryExpr();
					left = UnaryExpression.createNOT(asBooleanExpression(left));
				}

				default -> {
					if (jj_2_7()) {
						left = functionCallExpr();
					} else {
						switch ((jjNtk == -1) ? jj_ntk_f() : jjNtk) {
							case TRUE, FALSE, NULL, DECIMAL_LITERAL, HEX_LITERAL, OCTAL_LITERAL, FLOATING_POINT_LITERAL,
								 STRING_LITERAL, ID, 34 -> left = primaryExpr();
							default -> {
								jj_consume_token(-1);
								throw new ParseException();
							}
						}
					}
				}
			}
		}
		return left;
	}

	private Expression functionCallExpr() throws ParseException {
		Token func_name;
		Expression arg;
		List<Expression> args = new ArrayList<>();
		func_name = jj_consume_token(ID);
		jj_consume_token(34);
		arg = unaryExpr();
		args.add(arg);
		while (((jjNtk == -1) ? jj_ntk_f() : jjNtk) == 35) {
			jj_consume_token(35);
			arg = unaryExpr();
			args.add(arg);
		}
		jj_consume_token(36);
		try {
			return FunctionCallExpression.createFunctionCall(func_name.image, args);
		} catch (FunctionCallExpression.InvalidFunctionExpressionException inv_exc) {
			throw new Error("invalid function call expression", inv_exc);
		}
	}

	private Expression primaryExpr() throws ParseException {
		Expression left;
		switch ((jjNtk == -1) ? jj_ntk_f() : jjNtk) {
			case TRUE, FALSE, NULL, DECIMAL_LITERAL, HEX_LITERAL, OCTAL_LITERAL, FLOATING_POINT_LITERAL,
				 STRING_LITERAL -> left = literal();
			case ID -> left = variable();
			case 34 -> {
				jj_consume_token(34);
				left = orExpression();
				jj_consume_token(36);
			}
			default -> {
				jj_consume_token(-1);
				throw new ParseException();
			}
		}
		return left;
	}

	private ConstantExpression literal() throws ParseException {
		String s;
		ConstantExpression left;
		Token t;
		switch ((jjNtk == -1) ? jj_ntk_f() : jjNtk) {
			case STRING_LITERAL -> {
				s = stringLiteral();
				left = new ConstantExpression(s);
			}
			case DECIMAL_LITERAL -> {
				t = jj_consume_token(DECIMAL_LITERAL);
				left = ConstantExpression.createFromDecimal(t.image);
			}
			case HEX_LITERAL -> {
				t = jj_consume_token(HEX_LITERAL);
				left = ConstantExpression.createFromHex(t.image);
			}
			case OCTAL_LITERAL -> {
				t = jj_consume_token(OCTAL_LITERAL);
				left = ConstantExpression.createFromOctal(t.image);
			}
			case FLOATING_POINT_LITERAL -> {
				t = jj_consume_token(FLOATING_POINT_LITERAL);
				left = ConstantExpression.createFloat(t.image);
			}
			case TRUE -> {
				jj_consume_token(TRUE);
				left = ConstantExpression.TRUE;
			}
			case FALSE -> {
				jj_consume_token(FALSE);
				left = ConstantExpression.FALSE;
			}
			case NULL -> {
				jj_consume_token(NULL);
				left = ConstantExpression.NULL;
			}
			default -> {
				jj_consume_token(-1);
				throw new ParseException();
			}
		}
		return left;
	}

	private String stringLiteral() throws ParseException {
		Token t;
		StringBuilder rc = new StringBuilder();
		t = jj_consume_token(STRING_LITERAL);
		String image = t.image;
		for (int i = 1; i < image.length() - 1; i++) {
			char c = image.charAt(i);
			if (c == '\'')
				i++;
			rc.append(c);
		}
		return rc.toString();
	}

	private PropertyExpression variable() throws ParseException {
		Token t;
		PropertyExpression left;
		t = jj_consume_token(ID);
		left = new PropertyExpression(t.image);
		return left;
	}

	private boolean jj_2_1() {
		jjLa = 2;
		jjLastPos = jjScanPos = token;
		try {
			return (!jj_3_1());
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean jj_2_2() {
		jjLa = 2;
		jjLastPos = jjScanPos = token;
		try {
			return (!jj_3_2());
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean jj_2_3() {
		jjLa = 2;
		jjLastPos = jjScanPos = token;
		try {
			return (!jj_3_3());
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean jj_2_4() {
		jjLa = 2;
		jjLastPos = jjScanPos = token;
		try {
			return (!jj_3_4());
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean jj_2_5() {
		jjLa = 2147483647;
		jjLastPos = jjScanPos = token;
		try {
			return (!jj_3_5());
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean jj_2_6() {
		jjLa = 2147483647;
		jjLastPos = jjScanPos = token;
		try {
			return (!jj_3_6());
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean jj_2_7() {
		jjLa = 2147483647;
		jjLastPos = jjScanPos = token;
		try {
			return (!jj_3_7());
		} catch (LookaheadSuccess ls) {
			return true;
		}
	}

	private boolean jj_3R_unaryExpr_469_5_11() {
		Token xsp;
		xsp = jjScanPos;
		if (jj_3R_unaryExpr_470_9_13()) {
			jjScanPos = xsp;
			if (jj_3R_unaryExpr_473_9_14()) {
				jjScanPos = xsp;
				if (jj_3R_unaryExpr_478_9_15()) {
					jjScanPos = xsp;
					if (jj_3R_unaryExpr_483_9_16()) {
						jjScanPos = xsp;
						if (jj_3R_unaryExpr_488_9_17()) {
							jjScanPos = xsp;
							if (jj_3R_unaryExpr_493_13_18()) {
								jjScanPos = xsp;
								return jj_3R_unaryExpr_496_9_19();
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3R_comparisonExpression_358_17_59() {
		if (jj_scan_token(IN)) return true;
		if (jj_scan_token(34)) return true;
		if (jj_3R_stringLitteral_635_5_23()) return true;
		Token xsp;
		while (true) {
			xsp = jjScanPos;
			if (jj_3R_comparisonExpression_366_25_64()) {
				jjScanPos = xsp;
				break;
			}
		}
		return jj_scan_token(36);
	}

	private boolean jj_3R_equalityExpression_267_13_49() {
		return jj_scan_token(28) || jj_3R_comparisonExpression_305_5_45();
	}

	private boolean jj_3R_equalityExpression_267_13_46() {
		Token xsp;
		xsp = jjScanPos;
		if (jj_3R_equalityExpression_267_13_49()) {
			jjScanPos = xsp;
			if (jj_3R_equalityExpression_272_13_50()) {
				jjScanPos = xsp;
				if (jj_3_1()) {
					jjScanPos = xsp;
					return jj_3R_equalityExpression_283_13_51();
				}
			}
		}
		return false;
	}

	private boolean jj_3R_variable_655_5_31() {
		return jj_scan_token(ID);
	}

	private boolean jj_3_3() {
		return jj_scan_token(NOT) ||
				jj_scan_token(BETWEEN) ||
				jj_3R_addExpression_412_5_47() ||
				jj_scan_token(AND) ||
				jj_3R_addExpression_412_5_47();
	}

	private boolean jj_3R_primaryExpr_550_9_29() {
		return jj_scan_token(34) || jj_3R_orExpression_221_5_32() || jj_scan_token(36);
	}

	private boolean jj_3R_primaryExpr_548_9_28() {
		return jj_3R_variable_655_5_31();
	}

	private boolean jj_3R_multExpr_451_9_22() {
		return jj_scan_token(41) || jj_3R_unaryExpr_469_5_11();
	}

	private boolean jj_3R_primaryExpr_546_9_27() {
		return jj_3R_literal_566_5_30();
	}

	private boolean jj_3R_comparisonExpression_347_17_58() {
		return jj_scan_token(BETWEEN) || jj_3R_addExpression_412_5_47() || jj_scan_token(AND) || jj_3R_addExpression_412_5_47();
	}

	private boolean jj_3R_equalityExpression_263_5_43() {
		if (jj_3R_comparisonExpression_305_5_45()) return true;
		Token xsp;
		while (true) {
			xsp = jjScanPos;
			if (jj_3R_equalityExpression_267_13_46()) {
				jjScanPos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_multExpr_446_9_21() {
		if (jj_scan_token(40)) return true;
		return jj_3R_unaryExpr_469_5_11();
	}

	private boolean jj_3R_primaryExpr_545_5_25() {
		Token xsp;
		xsp = jjScanPos;
		if (jj_3R_primaryExpr_546_9_27()) {
			jjScanPos = xsp;
			if (jj_3R_primaryExpr_548_9_28()) {
				jjScanPos = xsp;
				return jj_3R_primaryExpr_550_9_29();
			}
		}
		return false;
	}

	private boolean jj_3R_comparisonExpression_333_23_62() {
		return jj_scan_token(ESCAPE) || jj_3R_stringLitteral_635_5_23();
	}

	private boolean jj_3_2() {
		if (jj_scan_token(NOT)) return true;
		if (jj_scan_token(LIKE)) return true;
		if (jj_3R_stringLitteral_635_5_23()) return true;
		Token xsp;
		xsp = jjScanPos;
		if (jj_3R_comparisonExpression_342_53_63()) jjScanPos = xsp;
		return false;
	}

	private boolean jj_3R_multExpr_441_9_12() {
		Token xsp;
		xsp = jjScanPos;
		if (jj_3R_multExpr_441_9_20()) {
			jjScanPos = xsp;
			if (jj_3R_multExpr_446_9_21()) {
				jjScanPos = xsp;
				return jj_3R_multExpr_451_9_22();
			}
		}
		return false;
	}

	private boolean jj_3R_multExpr_441_9_20() {
		return jj_scan_token(39) || jj_3R_unaryExpr_469_5_11();
	}

	private boolean jj_3R_stringLitteral_635_5_23() {
		return jj_scan_token(STRING_LITERAL);
	}

	private boolean jj_3R_andExpression_246_13_44() {
		return jj_scan_token(AND) || jj_3R_equalityExpression_263_5_43();
	}

	private boolean jj_3R_multExpr_439_5_10() {
		if (jj_3R_unaryExpr_469_5_11()) return true;
		Token xsp;
		while (true) {
			xsp = jjScanPos;
			if (jj_3R_multExpr_441_9_12()) {
				jjScanPos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_comparisonExpression_329_17_57() {
		if (jj_scan_token(LIKE)) return true;
		if (jj_3R_stringLitteral_635_5_23()) return true;
		Token xsp;
		xsp = jjScanPos;
		if (jj_3R_comparisonExpression_333_23_62()) jjScanPos = xsp;
		return false;
	}

	private boolean jj_3R_andExpression_243_5_41() {
		if (jj_3R_equalityExpression_263_5_43()) return true;
		Token xsp;
		while (true) {
			xsp = jjScanPos;
			if (jj_3R_andExpression_246_13_44()) {
				jjScanPos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_functionCallExpr_519_13_26() {
		return jj_scan_token(35) || jj_3R_unaryExpr_469_5_11();
	}

	private boolean jj_3R_comparisonExpression_324_17_56() {
		return jj_scan_token(33) || jj_3R_addExpression_412_5_47();
	}

	private boolean jj_3R_literal_616_9_40() {
		return jj_scan_token(NULL);
	}

	private boolean jj_3R_addExpression_421_13_61() {
		if (jj_scan_token(38)) return true;
		return jj_3R_multExpr_439_5_10();
	}

	private boolean jj_3_5() {
		Token xsp;
		xsp = jjScanPos;
		if (jj_scan_token(37)) {
			jjScanPos = xsp;
			if (jj_scan_token(38)) return true;
		}
		return jj_3R_multExpr_439_5_10();
	}

	private boolean jj_3R_comparisonExpression_319_17_55() {
		return jj_scan_token(32) || jj_3R_addExpression_412_5_47();
	}

	private boolean jj_3R_addExpression_416_13_60() {
		return jj_scan_token(37) || jj_3R_multExpr_439_5_10();
	}

	private boolean jj_3R_literal_609_9_39() {
		return jj_scan_token(FALSE);
	}

	private boolean jj_3R_orExpression_224_13_42() {
		return jj_scan_token(OR) || jj_3R_andExpression_243_5_41();
	}

	private boolean jj_3R_comparisonExpression_314_17_54() {
		return jj_scan_token(31) || jj_3R_addExpression_412_5_47();
	}

	private boolean jj_3R_addExpression_414_9_52() {
		Token xsp;
		xsp = jjScanPos;
		if (jj_3R_addExpression_416_13_60()) {
			jjScanPos = xsp;
			return jj_3R_addExpression_421_13_61();
		}
		return false;
	}

	private boolean jj_3_7() {
		if (jj_scan_token(ID)) return true;
		return jj_scan_token(34);
	}

	private boolean jj_3R_functionCallExpr_512_5_24() {
		if (jj_scan_token(ID) || jj_scan_token(34) || jj_3R_unaryExpr_469_5_11()) return true;
		Token xsp;
		while (true) {
			xsp = jjScanPos;
			if (jj_3R_functionCallExpr_519_13_26()) {
				jjScanPos = xsp;
				break;
			}
		}
		return jj_scan_token(36);
	}

	private boolean jj_3R_comparisonExpression_309_17_53() {
		return jj_scan_token(30) || jj_3R_addExpression_412_5_47();
	}

	private boolean jj_3R_comparisonExpression_309_17_48() {
		Token xsp;
		xsp = jjScanPos;
		if (jj_3R_comparisonExpression_309_17_53()) {
			jjScanPos = xsp;
			if (jj_3R_comparisonExpression_314_17_54()) {
				jjScanPos = xsp;
				if (jj_3R_comparisonExpression_319_17_55()) {
					jjScanPos = xsp;
					if (jj_3R_comparisonExpression_324_17_56()) {
						jjScanPos = xsp;
						if (jj_3R_comparisonExpression_329_17_57()) {
							jjScanPos = xsp;
							if (jj_3_2()) {
								jjScanPos = xsp;
								if (jj_3R_comparisonExpression_347_17_58()) {
									jjScanPos = xsp;
									if (jj_3_3()) {
										jjScanPos = xsp;
										if (jj_3R_comparisonExpression_358_17_59()) {
											jjScanPos = xsp;
											return jj_3_4();
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3R_literal_602_9_38() {
		return jj_scan_token(TRUE);
	}

	private boolean jj_3R_addExpression_412_5_47() {
		if (jj_3R_multExpr_439_5_10()) return true;
		Token xsp;
		while (true) {
			xsp = jjScanPos;
			if (jj_3R_addExpression_414_9_52()) {
				jjScanPos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_orExpression_221_5_32() {
		if (jj_3R_andExpression_243_5_41()) return true;
		Token xsp;
		while (true) {
			xsp = jjScanPos;
			if (jj_3R_orExpression_224_13_42()) {
				jjScanPos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_literal_595_9_37() {
		return jj_scan_token(FLOATING_POINT_LITERAL);
	}

	private boolean jj_3R_comparisonExpression_387_25_65() {
		return jj_scan_token(35) || jj_3R_stringLitteral_635_5_23();
	}

	private boolean jj_3R_unaryExpr_493_13_18() {
		return jj_3R_functionCallExpr_512_5_24();
	}

	private boolean jj_3R_unaryExpr_496_9_19() {
		return jj_3R_primaryExpr_545_5_25();
	}

	private boolean jj_3R_literal_588_9_36() {
		return jj_scan_token(OCTAL_LITERAL);
	}

	private boolean jj_3R_comparisonExpression_305_5_45() {
		if (jj_3R_addExpression_412_5_47()) return true;
		Token xsp;
		while (true) {
			xsp = jjScanPos;
			if (jj_3R_comparisonExpression_309_17_48()) {
				jjScanPos = xsp;
				break;
			}
		}
		return false;
	}

	private boolean jj_3R_unaryExpr_488_9_17() {
		if (jj_scan_token(XQUERY)) return true;
		return jj_3R_stringLitteral_635_5_23();
	}

	private boolean jj_3R_literal_581_9_35() {
		return jj_scan_token(HEX_LITERAL);
	}

	private boolean jj_3R_unaryExpr_483_9_16() {
		if (jj_scan_token(XPATH)) return true;
		return jj_3R_stringLitteral_635_5_23();
	}

	private boolean jj_3R_comparisonExpression_342_53_63() {
		if (jj_scan_token(ESCAPE)) return true;
		return jj_3R_stringLitteral_635_5_23();
	}

	private boolean jj_3_4() {
		if (jj_scan_token(NOT)) return true;
		if (jj_scan_token(IN)) return true;
		if (jj_scan_token(34)) return true;
		if (jj_3R_stringLitteral_635_5_23()) return true;
		Token xsp;
		while (true) {
			xsp = jjScanPos;
			if (jj_3R_comparisonExpression_387_25_65()) {
				jjScanPos = xsp;
				break;
			}
		}
		return jj_scan_token(36);
	}

	private boolean jj_3_6() {
		if (jj_scan_token(37)) return true;
		return jj_3R_unaryExpr_469_5_11();
	}

	private boolean jj_3R_literal_574_9_34() {
		return jj_scan_token(DECIMAL_LITERAL);
	}

	private boolean jj_3R_unaryExpr_478_9_15() {
		if (jj_scan_token(NOT)) return true;
		return jj_3R_unaryExpr_469_5_11();
	}

	private boolean jj_3R_comparisonExpression_366_25_64() {
		if (jj_scan_token(35)) return true;
		return jj_3R_stringLitteral_635_5_23();
	}

	private boolean jj_3R_equalityExpression_283_13_51() {
		if (jj_scan_token(IS)) return true;
		if (jj_scan_token(NOT)) return true;
		return jj_scan_token(NULL);
	}

	private boolean jj_3R_unaryExpr_473_9_14() {
		if (jj_scan_token(38)) return true;
		return jj_3R_unaryExpr_469_5_11();
	}

	private boolean jj_3R_literal_567_9_33() {
		return jj_3R_stringLitteral_635_5_23();
	}

	private boolean jj_3_1() {
		if (jj_scan_token(IS)) return true;
		return jj_scan_token(NULL);
	}

	private boolean jj_3R_unaryExpr_470_9_13() {
		if (jj_scan_token(37)) return true;
		return jj_3R_unaryExpr_469_5_11();
	}

	private boolean jj_3R_literal_566_5_30() {
		Token xsp;
		xsp = jjScanPos;
		if (jj_3R_literal_567_9_33()) {
			jjScanPos = xsp;
			if (jj_3R_literal_574_9_34()) {
				jjScanPos = xsp;
				if (jj_3R_literal_581_9_35()) {
					jjScanPos = xsp;
					if (jj_3R_literal_588_9_36()) {
						jjScanPos = xsp;
						if (jj_3R_literal_595_9_37()) {
							jjScanPos = xsp;
							if (jj_3R_literal_602_9_38()) {
								jjScanPos = xsp;
								if (jj_3R_literal_609_9_39()) {
									jjScanPos = xsp;
									return jj_3R_literal_616_9_40();
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean jj_3R_equalityExpression_272_13_50() {
		if (jj_scan_token(29)) return true;
		return jj_3R_comparisonExpression_305_5_45();
	}

	private final PredicateParserTokenManager toketSource;
	SimpleCharStream jjInputStream;
	private Token token;
	private int jjNtk;
	private Token jjScanPos, jjLastPos;
	private int jjLa;

	SqlPredicate(Reader stream) {
		jjInputStream = new SimpleCharStream(stream, 1, 1);
		toketSource = new PredicateParserTokenManager(jjInputStream);
		token = new Token();
		jjNtk = -1;
	}

	private Token jj_consume_token(int kind) throws ParseException {
		Token oldToken;
		if ((oldToken = token).next != null) token = token.next;
		else token = token.next = toketSource.getNextToken();
		jjNtk = -1;
		if (token.kind == kind) {
			return token;
		}
		token = oldToken;
		throw generateParseException();
	}

	static private final class LookaheadSuccess extends Error {
		@Override
		public Throwable fillInStackTrace() {
			return this;
		}
	}

	static private final LookaheadSuccess jj_ls = new LookaheadSuccess();

	private boolean jj_scan_token(int kind) {
		if (jjScanPos == jjLastPos) {
			jjLa--;
			if (jjScanPos.next == null) jjLastPos = jjScanPos = jjScanPos.next = toketSource.getNextToken();
			else jjLastPos = jjScanPos = jjScanPos.next;
		} else jjScanPos = jjScanPos.next;
		if (jjScanPos.kind != kind) return true;
		if (jjLa == 0 && jjScanPos == jjLastPos) throw jj_ls;
		return false;
	}

	private int jj_ntk_f() {
		Token jj_nt;
		if ((jj_nt = token.next) == null)
			return (jjNtk = (token.next = toketSource.getNextToken()).kind);
		else
			return (jjNtk = jj_nt.kind);
	}

	private ParseException generateParseException() {
		Token errortok = token.next;
		int line = errortok.beginLine, column = errortok.beginColumn;
		String mess = (errortok.kind == 0) ? tokenImage[0] : errortok.image;
		return new ParseException("Parse error at line " + line + ", column " + column + ".  Encountered: " + mess);
	}
}
