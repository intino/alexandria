// Generated from /Users/oroncal/workspace/alexandria/messaging/message/src/io/intino/alexandria/message/parser/InlLexicon.g4 by ANTLR 4.10.1
package io.intino.alexandria.message.parser;


import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class InlLexicon extends Lexer {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LSQUARE=1, RSQUARE=2, COLON=3, DOT=4, IDENTIFIER=5, NEWLINE_INDENT=6, 
		NEWLINE=7, UNKNOWN_TOKEN=8, A_NEWLINE_INDENT=9, A_NEWLINE=10, VALUE=11;
	public static final int
		ATTR_MODE=1;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE", "ATTR_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"LSQUARE", "RSQUARE", "COLON", "DOT", "IDENTIFIER", "NEWLINE_INDENT", 
			"NEWLINE", "UNKNOWN_TOKEN", "A_NEWLINE_INDENT", "A_NEWLINE", "VALUE", 
			"LETTER", "DIGIT", "UNDERDASH"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'['", "']'", null, "'.'", null, null, null, null, "'\\n\\t'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LSQUARE", "RSQUARE", "COLON", "DOT", "IDENTIFIER", "NEWLINE_INDENT", 
			"NEWLINE", "UNKNOWN_TOKEN", "A_NEWLINE_INDENT", "A_NEWLINE", "VALUE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}




	public InlLexicon(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "InlLexicon.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 2:
			COLON_action((RuleContext)_localctx, actionIndex);
			break;
		case 8:
			A_NEWLINE_INDENT_action((RuleContext)_localctx, actionIndex);
			break;
		case 9:
			A_NEWLINE_action((RuleContext)_localctx, actionIndex);
			break;
		case 10:
			VALUE_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void COLON_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			setType(COLON);
			break;
		}
	}
	private void A_NEWLINE_INDENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			   setType(NEWLINE_INDENT); 
			break;
		}
	}
	private void A_NEWLINE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:
			   setType(NEWLINE); 
			break;
		}
	}
	private void VALUE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:
			   setType(VALUE); 
			break;
		}
	}

	public static final String _serializedATN =
		"\u0004\u0000\u000bS\u0006\uffff\uffff\u0006\uffff\uffff\u0002\u0000\u0007"+
		"\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007"+
		"\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007"+
		"\u0006\u0002\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n"+
		"\u0007\n\u0002\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0001"+
		"\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001"+
		"\u0004\u0003\u0004,\b\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0005"+
		"\u00041\b\u0004\n\u0004\f\u00044\t\u0004\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b"+
		"\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\n\u0004\nH\b\n\u000b\n\f\nI\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001"+
		"\f\u0001\f\u0001\r\u0001\r\u0000\u0000\u000e\u0002\u0001\u0004\u0002\u0006"+
		"\u0003\b\u0004\n\u0005\f\u0006\u000e\u0007\u0010\b\u0012\t\u0014\n\u0016"+
		"\u000b\u0018\u0000\u001a\u0000\u001c\u0000\u0002\u0000\u0001\u0003\u0001"+
		"\u0000\n\n\u0002\u0000AZaz\u0001\u000009S\u0000\u0002\u0001\u0000\u0000"+
		"\u0000\u0000\u0004\u0001\u0000\u0000\u0000\u0000\u0006\u0001\u0000\u0000"+
		"\u0000\u0000\b\u0001\u0000\u0000\u0000\u0000\n\u0001\u0000\u0000\u0000"+
		"\u0000\f\u0001\u0000\u0000\u0000\u0000\u000e\u0001\u0000\u0000\u0000\u0000"+
		"\u0010\u0001\u0000\u0000\u0000\u0001\u0012\u0001\u0000\u0000\u0000\u0001"+
		"\u0014\u0001\u0000\u0000\u0000\u0001\u0016\u0001\u0000\u0000\u0000\u0002"+
		"\u001e\u0001\u0000\u0000\u0000\u0004 \u0001\u0000\u0000\u0000\u0006\""+
		"\u0001\u0000\u0000\u0000\b\'\u0001\u0000\u0000\u0000\n+\u0001\u0000\u0000"+
		"\u0000\f5\u0001\u0000\u0000\u0000\u000e8\u0001\u0000\u0000\u0000\u0010"+
		":\u0001\u0000\u0000\u0000\u0012<\u0001\u0000\u0000\u0000\u0014A\u0001"+
		"\u0000\u0000\u0000\u0016G\u0001\u0000\u0000\u0000\u0018M\u0001\u0000\u0000"+
		"\u0000\u001aO\u0001\u0000\u0000\u0000\u001cQ\u0001\u0000\u0000\u0000\u001e"+
		"\u001f\u0005[\u0000\u0000\u001f\u0003\u0001\u0000\u0000\u0000 !\u0005"+
		"]\u0000\u0000!\u0005\u0001\u0000\u0000\u0000\"#\u0005:\u0000\u0000#$\u0006"+
		"\u0002\u0000\u0000$%\u0001\u0000\u0000\u0000%&\u0006\u0002\u0001\u0000"+
		"&\u0007\u0001\u0000\u0000\u0000\'(\u0005.\u0000\u0000(\t\u0001\u0000\u0000"+
		"\u0000),\u0003\u0018\u000b\u0000*,\u0003\u001c\r\u0000+)\u0001\u0000\u0000"+
		"\u0000+*\u0001\u0000\u0000\u0000,2\u0001\u0000\u0000\u0000-1\u0003\u001a"+
		"\f\u0000.1\u0003\u0018\u000b\u0000/1\u0003\u001c\r\u00000-\u0001\u0000"+
		"\u0000\u00000.\u0001\u0000\u0000\u00000/\u0001\u0000\u0000\u000014\u0001"+
		"\u0000\u0000\u000020\u0001\u0000\u0000\u000023\u0001\u0000\u0000\u0000"+
		"3\u000b\u0001\u0000\u0000\u000042\u0001\u0000\u0000\u000056\u0003\u000e"+
		"\u0006\u000067\u0005\t\u0000\u00007\r\u0001\u0000\u0000\u000089\u0005"+
		"\n\u0000\u00009\u000f\u0001\u0000\u0000\u0000:;\t\u0000\u0000\u0000;\u0011"+
		"\u0001\u0000\u0000\u0000<=\u0005\n\u0000\u0000=>\u0005\t\u0000\u0000>"+
		"?\u0001\u0000\u0000\u0000?@\u0006\b\u0002\u0000@\u0013\u0001\u0000\u0000"+
		"\u0000AB\u0005\n\u0000\u0000BC\u0006\t\u0003\u0000CD\u0001\u0000\u0000"+
		"\u0000DE\u0006\t\u0004\u0000E\u0015\u0001\u0000\u0000\u0000FH\b\u0000"+
		"\u0000\u0000GF\u0001\u0000\u0000\u0000HI\u0001\u0000\u0000\u0000IG\u0001"+
		"\u0000\u0000\u0000IJ\u0001\u0000\u0000\u0000JK\u0001\u0000\u0000\u0000"+
		"KL\u0006\n\u0005\u0000L\u0017\u0001\u0000\u0000\u0000MN\u0007\u0001\u0000"+
		"\u0000N\u0019\u0001\u0000\u0000\u0000OP\u0007\u0002\u0000\u0000P\u001b"+
		"\u0001\u0000\u0000\u0000QR\u0005_\u0000\u0000R\u001d\u0001\u0000\u0000"+
		"\u0000\u0006\u0000\u0001+02I\u0006\u0001\u0002\u0000\u0002\u0001\u0000"+
		"\u0001\b\u0001\u0001\t\u0002\u0002\u0000\u0000\u0001\n\u0003";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}