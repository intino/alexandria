// Generated from /Users/oroncal/workspace/alexandria/messaging/message/src/io/intino/alexandria/message/parser/InlLexicon.g4 by ANTLR 4.8
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
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

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
			null, "'['", "']'", null, "'.'", null, null, null, null, "'\n\t'"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\rU\b\1\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\3\3\3\3\4\3\4\3\4"+
		"\3\4\3\4\3\5\3\5\3\6\3\6\5\6.\n\6\3\6\3\6\3\6\7\6\63\n\6\f\6\16\6\66\13"+
		"\6\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13"+
		"\3\13\3\f\6\fJ\n\f\r\f\16\fK\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\2\2\20"+
		"\4\3\6\4\b\5\n\6\f\7\16\b\20\t\22\n\24\13\26\f\30\r\32\2\34\2\36\2\4\2"+
		"\3\5\3\2\f\f\4\2C\\c|\3\2\62;\2U\2\4\3\2\2\2\2\6\3\2\2\2\2\b\3\2\2\2\2"+
		"\n\3\2\2\2\2\f\3\2\2\2\2\16\3\2\2\2\2\20\3\2\2\2\2\22\3\2\2\2\3\24\3\2"+
		"\2\2\3\26\3\2\2\2\3\30\3\2\2\2\4 \3\2\2\2\6\"\3\2\2\2\b$\3\2\2\2\n)\3"+
		"\2\2\2\f-\3\2\2\2\16\67\3\2\2\2\20:\3\2\2\2\22<\3\2\2\2\24>\3\2\2\2\26"+
		"C\3\2\2\2\30I\3\2\2\2\32O\3\2\2\2\34Q\3\2\2\2\36S\3\2\2\2 !\7]\2\2!\5"+
		"\3\2\2\2\"#\7_\2\2#\7\3\2\2\2$%\7<\2\2%&\b\4\2\2&\'\3\2\2\2\'(\b\4\3\2"+
		"(\t\3\2\2\2)*\7\60\2\2*\13\3\2\2\2+.\5\32\r\2,.\5\36\17\2-+\3\2\2\2-,"+
		"\3\2\2\2.\64\3\2\2\2/\63\5\34\16\2\60\63\5\32\r\2\61\63\5\36\17\2\62/"+
		"\3\2\2\2\62\60\3\2\2\2\62\61\3\2\2\2\63\66\3\2\2\2\64\62\3\2\2\2\64\65"+
		"\3\2\2\2\65\r\3\2\2\2\66\64\3\2\2\2\678\5\20\b\289\7\13\2\29\17\3\2\2"+
		"\2:;\7\f\2\2;\21\3\2\2\2<=\13\2\2\2=\23\3\2\2\2>?\7\f\2\2?@\7\13\2\2@"+
		"A\3\2\2\2AB\b\n\4\2B\25\3\2\2\2CD\7\f\2\2DE\b\13\5\2EF\3\2\2\2FG\b\13"+
		"\6\2G\27\3\2\2\2HJ\n\2\2\2IH\3\2\2\2JK\3\2\2\2KI\3\2\2\2KL\3\2\2\2LM\3"+
		"\2\2\2MN\b\f\7\2N\31\3\2\2\2OP\t\3\2\2P\33\3\2\2\2QR\t\4\2\2R\35\3\2\2"+
		"\2ST\7a\2\2T\37\3\2\2\2\b\2\3-\62\64K\b\3\4\2\4\3\2\3\n\3\3\13\4\4\2\2"+
		"\3\f\5";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}