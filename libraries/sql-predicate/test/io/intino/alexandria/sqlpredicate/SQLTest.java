package io.intino.alexandria.sqlpredicate;

import io.intino.alexandria.sqlpredicate.context.EvaluationContext;
import io.intino.alexandria.sqlpredicate.context.MapEvaluationContext;
import io.intino.alexandria.sqlpredicate.expressions.BooleanExpression;
import io.intino.alexandria.sqlpredicate.parser.InvalidExpressionException;
import junit.framework.TestCase;

public class SQLTest extends TestCase {

	public void testBooleanSelector() throws Exception {
		EvaluationContext context = createEvaluationContext();
		assertPredicate(context, "(trueProp OR falseProp) AND trueProp", true);
		assertPredicate(context, "(trueProp OR falseProp) AND falseProp", false);
		assertPredicate(context, "trueProp", true);
		assertPredicate(context, "(falseProp OR falseProp OR falseProp OR falseProp OR falseProp OR falseProp OR trueProp)", true);
		assertPredicate(context, "(falseProp OR falseProp OR falseProp OR falseProp OR falseProp OR falseProp OR falseProp)", false);
		assertPredicate(context, "(trueProp AND trueProp AND trueProp AND trueProp AND trueProp AND trueProp AND falseProp)", false);
		assertPredicate(context, "(trueProp AND trueProp AND trueProp AND trueProp AND trueProp AND trueProp AND trueProp)", true);
	}

	public void testBasicSelectors() throws Exception {
		EvaluationContext context = createEvaluationContext();
		assertPredicate(context, "name = 'James'", true);
		assertPredicate(context, "rank > 100", true);
		assertPredicate(context, "rank >= 123", true);
		assertPredicate(context, "rank >= 124", false);

	}

	public void testPropertyTypes() throws Exception {
		EvaluationContext context = createEvaluationContext();
		assertPredicate(context, "byteProp = 123", true);
		assertPredicate(context, "byteProp = 10", false);
		assertPredicate(context, "byteProp2 = 33", true);
		assertPredicate(context, "byteProp2 = 10", false);

		assertPredicate(context, "shortProp = 123", true);
		assertPredicate(context, "shortProp = 10", false);

		assertPredicate(context, "shortProp = 123", true);
		assertPredicate(context, "shortProp = 10", false);

		assertPredicate(context, "intProp = 123", true);
		assertPredicate(context, "intProp = 10", false);

		assertPredicate(context, "longProp = 123", true);
		assertPredicate(context, "longProp = 10", false);

		assertPredicate(context, "floatProp = 123", true);
		assertPredicate(context, "floatProp = 10", false);

		assertPredicate(context, "doubleProp = 123", true);
		assertPredicate(context, "doubleProp = 10", false);
	}

	public void testAndSelectors() throws Exception {
		EvaluationContext context = createEvaluationContext();

		assertPredicate(context, "name = 'James' and rank < 200", true);
		assertPredicate(context, "name = 'James' and rank > 200", false);
		assertPredicate(context, "name = 'Foo' and rank < 200", false);
		assertPredicate(context, "unknown = 'Foo' and anotherUnknown < 200", false);
	}

	public void testOrSelectors() throws Exception {
		EvaluationContext context = createEvaluationContext();

		assertPredicate(context, "name = 'James' or rank < 200", true);
		assertPredicate(context, "name = 'James' or rank > 200", true);
		assertPredicate(context, "name = 'Foo' or rank < 200", true);
		assertPredicate(context, "name = 'Foo' or rank > 200", false);
		assertPredicate(context, "unknown = 'Foo' or anotherUnknown < 200", false);
	}

	public void testPlus() throws Exception {
		EvaluationContext context = createEvaluationContext();

		assertPredicate(context, "rank + 2 = 125", true);
		assertPredicate(context, "(rank + 2) = 125", true);
		assertPredicate(context, "125 = (rank + 2)", true);
		assertPredicate(context, "rank + version = 125", true);
		assertPredicate(context, "rank + 2 < 124", false);
		assertPredicate(context, "name + '!' = 'James!'", true);
	}

	public void testMinus() throws Exception {
		EvaluationContext context = createEvaluationContext();

		assertPredicate(context, "rank - 2 = 121", true);
		assertPredicate(context, "rank - version = 121", true);
		assertPredicate(context, "rank - 2 > 122", false);
	}

	public void testMultiply() throws Exception {
		EvaluationContext context = createEvaluationContext();

		assertPredicate(context, "rank * 2 = 246", true);
		assertPredicate(context, "rank * version = 246", true);
		assertPredicate(context, "rank * 2 < 130", false);
	}

	public void testDivide() throws Exception {
		EvaluationContext context = createEvaluationContext();
		assertPredicate(context, "rank / version = 61.5", true);
		assertPredicate(context, "rank / 3 > 100.0", false);
		assertPredicate(context, "rank / 3 > 100", false);
		assertPredicate(context, "version / 2 = 1", true);

	}

	public void testBetween() throws Exception {
		EvaluationContext context = createEvaluationContext();
		assertPredicate(context, "rank between 100 and 150", true);
		assertPredicate(context, "rank between 10 and 120", false);
	}

	public void testIn() throws Exception {
		EvaluationContext context = createEvaluationContext();
		assertPredicate(context, "name in ('James', 'Bob', 'Gromit')", true);
		assertPredicate(context, "name in ('Bob', 'James', 'Gromit')", true);
		assertPredicate(context, "name in ('Gromit', 'Bob', 'James')", true);
		assertPredicate(context, "name in ('Gromit', 'Bob', 'Cheddar')", false);
		assertPredicate(context, "name not in ('Gromit', 'Bob', 'Cheddar')", true);
	}

	public void testIsNull() throws Exception {
		EvaluationContext context = createEvaluationContext();
		assertPredicate(context, "dummy is null", true);
		assertPredicate(context, "dummy is not null", false);
		assertPredicate(context, "name is not null", true);
		assertPredicate(context, "name is null", false);
	}

	public void testLike() throws Exception {
		MapEvaluationContext context = createEvaluationContext();
		context.addProperty("modelClassId", "com.whatever.something.foo.bar");
		context.addProperty("modelInstanceId", "170");
		context.addProperty("modelRequestError", "abc");
		context.addProperty("modelCorrelatedClientId", "whatever");
		assertPredicate(context, "modelClassId LIKE 'com.whatever.something.%' AND modelInstanceId = '170' AND (modelRequestError IS NULL OR modelCorrelatedClientId = 'whatever')", true);
		context.addProperty("modelCorrelatedClientId", "shouldFailNow");
		assertPredicate(context, "modelClassId LIKE 'com.whatever.something.%' AND modelInstanceId = '170' AND (modelRequestError IS NULL OR modelCorrelatedClientId = 'whatever')", false);
		context = createEvaluationContext();
		context.addProperty("modelClassId", "com.whatever.something.foo.bar");
		context.addProperty("modelInstanceId", "170");
		context.addProperty("modelCorrelatedClientId", "shouldNotMatch");
		assertPredicate(context, "modelClassId LIKE 'com.whatever.something.%' AND modelInstanceId = '170' AND (modelRequestError IS NULL OR modelCorrelatedClientId = 'whatever')", true);
	}

	public void testMoreUseCases() throws Exception {
		MapEvaluationContext context = createEvaluationContext();
		assertPredicate(context, "SessionserverId=1870414179", false);
		context.addProperty("SessionserverId", 1870414179);
		assertPredicate(context, "SessionserverId=1870414179", true);
		context.addProperty("SessionserverId", 1234);
		assertPredicate(context, "SessionserverId=1870414179", false);
		assertPredicate(context, "Command NOT IN ('MirrorLobbyRequest', 'MirrorLobbyReply')", false);
		context.addProperty("Command", "Cheese");
		assertPredicate(context, "Command NOT IN ('MirrorLobbyRequest', 'MirrorLobbyReply')", true);
		context.addProperty("Command", "MirrorLobbyRequest");
		assertPredicate(context, "Command NOT IN ('MirrorLobbyRequest', 'MirrorLobbyReply')", false);
		context.addProperty("Command", "MirrorLobbyReply");
		assertPredicate(context, "Command NOT IN ('MirrorLobbyRequest', 'MirrorLobbyReply')", false);
	}

	public void testFloatComparisons() throws Exception {
		EvaluationContext context = createEvaluationContext();
		assertPredicate(context, "1.0 < 1.1", true);
		assertPredicate(context, "-1.1 < 1.0", true);
		assertPredicate(context, "1.0E1 < 1.1E1", true);
		assertPredicate(context, "-1.1E1 < 1.0E1", true);
		assertPredicate(context, "1. < 1.1", true);
		assertPredicate(context, "-1.1 < 1.", true);
		assertPredicate(context, "1.E1 < 1.1E1", true);
		assertPredicate(context, "-1.1E1 < 1.E1", true);
		assertPredicate(context, ".1 < .5", true);
		assertPredicate(context, "-.5 < .1", true);
		assertPredicate(context, ".1E1 < .5E1", true);
		assertPredicate(context, "-.5E1 < .1E1", true);
		assertPredicate(context, "4E10 < 5E10", true);
		assertPredicate(context, "5E8 < 5E10", true);
		assertPredicate(context, "-4E10 < 2E10", true);
		assertPredicate(context, "-5E8 < 2E2", true);
		assertPredicate(context, "4E+10 < 5E+10", true);
		assertPredicate(context, "4E-10 < 5E-10", true);
	}

	public void testStringQuoteParsing() throws Exception {
		EvaluationContext context = createEvaluationContext();
		assertPredicate(context, "quote = '''In God We Trust'''", true);
	}

	public void testLikeComparisons() throws Exception {
		EvaluationContext context = createEvaluationContext();
		assertPredicate(context, "quote LIKE '''In G_d We Trust'''", true);
		assertPredicate(context, "quote LIKE '''In Gd_ We Trust'''", false);
		assertPredicate(context, "quote NOT LIKE '''In G_d We Trust'''", false);
		assertPredicate(context, "quote NOT LIKE '''In Gd_ We Trust'''", true);
		assertPredicate(context, "foo LIKE '%oo'", true);
		assertPredicate(context, "foo LIKE '%ar'", false);
		assertPredicate(context, "foo NOT LIKE '%oo'", false);
		assertPredicate(context, "foo NOT LIKE '%ar'", true);
		assertPredicate(context, "foo LIKE '!_%' ESCAPE '!'", true);
		assertPredicate(context, "quote LIKE '!_%' ESCAPE '!'", false);
		assertPredicate(context, "foo NOT LIKE '!_%' ESCAPE '!'", false);
		assertPredicate(context, "quote NOT LIKE '!_%' ESCAPE '!'", true);
		assertPredicate(context, "punctuation LIKE '!#$&()*+,-./:;<=>?@[\\]^`{|}~'", true);
	}

	public void testSpecialEscapeLiteral() throws Exception {
		EvaluationContext context = createEvaluationContext();
		assertPredicate(context, "foo LIKE '%_%' ESCAPE '%'", true);
		assertPredicate(context, "endingUnderScore LIKE '_D7xlJIQn$_' ESCAPE '$'", true);
		assertPredicate(context, "endingUnderScore LIKE '_D7xlJIQn__' ESCAPE '_'", true);
		assertPredicate(context, "endingUnderScore LIKE '%D7xlJIQn%_' ESCAPE '%'", true);
		assertPredicate(context, "endingUnderScore LIKE '%D7xlJIQn%'  ESCAPE '%'", true);
		// literal '%' at the end, no match
		assertPredicate(context, "endingUnderScore LIKE '%D7xlJIQn%%'  ESCAPE '%'", false);
		assertPredicate(context, "endingUnderScore LIKE '_D7xlJIQn\\_' ESCAPE '\\'", true);
		assertPredicate(context, "endingUnderScore LIKE '%D7xlJIQn\\_' ESCAPE '\\'", true);
	}

	public void testInvalidSelector() {
		assertInvalidSelector("3+5");
		assertInvalidSelector("True AND 3+5");
		assertInvalidSelector("=TEST 'test'");
		assertInvalidSelector("prop1 = prop2 foo AND string = 'Test'");
		assertInvalidSelector("a = 1 AMD  b = 2");
	}

	public void testFunctionSelector() throws Exception {
		MapEvaluationContext context = createEvaluationContext();
		assertPredicate(context, "REGEX('1870414179', SessionserverId)", false);
		context.addProperty("SessionserverId", 1870414179);
		assertPredicate(context, "REGEX('1870414179', SessionserverId)", true);
		assertPredicate(context, "REGEX('[0-9]*', SessionserverId)", true);
		assertPredicate(context, "REGEX('^[1-8]*$', SessionserverId)", false);
		assertPredicate(context, "REGEX('^[1-8]*$', SessionserverId)", false);
		assertPredicate(context, "INLIST(SPLIT('Tom,Dick,George',','), name)", false);
		assertPredicate(context, "INLIST(SPLIT('Tom,James,George',','), name)", true);
		assertPredicate(context, "INLIST(MAKELIST('Tom','Dick','George'), name)", false);
		assertPredicate(context, "INLIST(MAKELIST('Tom','James','George'), name)", true);
	}

	protected MapEvaluationContext createEvaluationContext() {
		MapEvaluationContext context = new MapEvaluationContext();
		context.addProperty("name", "James");
		context.addProperty("location", "London");
		context.addProperty("byteProp", (byte) 123);
		context.addProperty("byteProp2", (byte) 33);
		context.addProperty("shortProp", (short) 123);
		context.addProperty("intProp", 123);
		context.addProperty("longProp", 123);
		context.addProperty("floatProp", 123);
		context.addProperty("doubleProp", 123);
		context.addProperty("rank", 123);
		context.addProperty("version", 2);
		context.addProperty("quote", "'In God We Trust'");
		context.addProperty("foo", "_foo");
		context.addProperty("punctuation", "!#$&()*+,-./:;<=>?@[\\]^`{|}~");
		context.addProperty("endingUnderScore", "XD7xlJIQn_");
		context.addProperty("trueProp", true);
		context.addProperty("falseProp", false);
		return context;
	}

	protected void assertInvalidSelector(String text) {
		try {
			SqlPredicate.parseSQL(text);
			fail("Created a valid selector");
		} catch (InvalidExpressionException ignored) {
		}
	}

	protected void assertPredicate(EvaluationContext context, String text, boolean expected) throws Exception {
		BooleanExpression selector = SqlPredicate.parseSQL(text);
		assertNotNull("Created a valid selector", selector);
		boolean value = selector.matches(context);
		assertEquals("Selector for: " + text, expected, value);
	}
}