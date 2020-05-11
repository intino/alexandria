// Generated from /Users/oroncal/workspace/alexandria/messaging/message/src/io/intino/alexandria/message/parser/InlLexicon.g4 by ANTLR 4.8
package io.intino.alexandria.message.parser;

	import static io.intino.alexandria.message.parser.InlGrammar.CHARACTER;

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
		LSQUARE=1, RSQUARE=2, COLON=3, UNDERDASH=4, DOT=5, DIGIT=6, LETTER=7, 
		IDENTIFIER=8, NEWLINE_INDENT=9, INDENT=10, NEWLINE=11, SP=12, UNKNOWN_TOKEN=13, 
		ATTR_BEGIN=14, ATTR_END=15, A_NEWLINE_INDENT=16, A_NEWLINE=17, A_SP=18, 
		A_CHARACTER=19;
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
			"LSQUARE", "RSQUARE", "COLON", "UNDERDASH", "DOT", "DIGIT", "LETTER", 
			"IDENTIFIER", "NEWLINE_INDENT", "INDENT", "NEWLINE", "SP", "UNKNOWN_TOKEN", 
			"ATTR_BEGIN", "ATTR_END", "A_NEWLINE_INDENT", "A_NEWLINE", "A_SP", "A_CHARACTER"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'['", "']'", null, "'_'", "'.'", null, null, null, null, "'\t'", 
			null, null, null, "'%ATTR_BEGIN%'", "'%ATTR_END%'", "'\n\t'", null, "' '"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LSQUARE", "RSQUARE", "COLON", "UNDERDASH", "DOT", "DIGIT", "LETTER", 
			"IDENTIFIER", "NEWLINE_INDENT", "INDENT", "NEWLINE", "SP", "UNKNOWN_TOKEN", 
			"ATTR_BEGIN", "ATTR_END", "A_NEWLINE_INDENT", "A_NEWLINE", "A_SP", "A_CHARACTER"
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
		case 15:
			A_NEWLINE_INDENT_action((RuleContext)_localctx, actionIndex);
			break;
		case 16:
			A_NEWLINE_action((RuleContext)_localctx, actionIndex);
			break;
		case 17:
			A_SP_action((RuleContext)_localctx, actionIndex);
			break;
		case 18:
			A_CHARACTER_action((RuleContext)_localctx, actionIndex);
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
	private void A_SP_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:
			   setType(SP); 
			break;
		}
	}
	private void A_CHARACTER_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 4:
			   setType(CHARACTER); 
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\25\u0081\b\1\b\1"+
		"\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t"+
		"\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4"+
		"\22\t\22\4\23\t\23\4\24\t\24\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3"+
		"\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\5\t>\n\t\3\t\3\t\3\t\7\tC\n\t\f\t\16"+
		"\tF\13\t\3\n\3\n\3\n\3\13\3\13\3\f\5\fN\n\f\3\f\3\f\5\fR\n\f\3\r\3\r\5"+
		"\rV\n\r\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3"+
		"\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3"+
		"\24\3\24\2\2\25\4\3\6\4\b\5\n\6\f\7\16\b\20\t\22\n\24\13\26\f\30\r\32"+
		"\16\34\17\36\20 \21\"\22$\23&\24(\25\4\2\3\4\3\2\62;\20\2C\\c|\u00c3\u00c3"+
		"\u00cb\u00cb\u00cf\u00cf\u00d3\u00d3\u00d5\u00d5\u00dc\u00dc\u00e3\u00e3"+
		"\u00eb\u00eb\u00ef\u00ef\u00f3\u00f3\u00f5\u00f5\u00fc\u00fc\2\u0086\2"+
		"\4\3\2\2\2\2\6\3\2\2\2\2\b\3\2\2\2\2\n\3\2\2\2\2\f\3\2\2\2\2\16\3\2\2"+
		"\2\2\20\3\2\2\2\2\22\3\2\2\2\2\24\3\2\2\2\2\26\3\2\2\2\2\30\3\2\2\2\2"+
		"\32\3\2\2\2\2\34\3\2\2\2\2\36\3\2\2\2\2 \3\2\2\2\3\"\3\2\2\2\3$\3\2\2"+
		"\2\3&\3\2\2\2\3(\3\2\2\2\4*\3\2\2\2\6,\3\2\2\2\b.\3\2\2\2\n\63\3\2\2\2"+
		"\f\65\3\2\2\2\16\67\3\2\2\2\209\3\2\2\2\22=\3\2\2\2\24G\3\2\2\2\26J\3"+
		"\2\2\2\30Q\3\2\2\2\32U\3\2\2\2\34W\3\2\2\2\36Y\3\2\2\2 f\3\2\2\2\"q\3"+
		"\2\2\2$v\3\2\2\2&{\3\2\2\2(~\3\2\2\2*+\7]\2\2+\5\3\2\2\2,-\7_\2\2-\7\3"+
		"\2\2\2./\7<\2\2/\60\b\4\2\2\60\61\3\2\2\2\61\62\b\4\3\2\62\t\3\2\2\2\63"+
		"\64\7a\2\2\64\13\3\2\2\2\65\66\7\60\2\2\66\r\3\2\2\2\678\t\2\2\28\17\3"+
		"\2\2\29:\t\3\2\2:\21\3\2\2\2;>\5\20\b\2<>\5\n\5\2=;\3\2\2\2=<\3\2\2\2"+
		">D\3\2\2\2?C\5\16\7\2@C\5\20\b\2AC\5\n\5\2B?\3\2\2\2B@\3\2\2\2BA\3\2\2"+
		"\2CF\3\2\2\2DB\3\2\2\2DE\3\2\2\2E\23\3\2\2\2FD\3\2\2\2GH\5\30\f\2HI\5"+
		"\26\13\2I\25\3\2\2\2JK\7\13\2\2K\27\3\2\2\2LN\7\17\2\2ML\3\2\2\2MN\3\2"+
		"\2\2NO\3\2\2\2OR\7\f\2\2PR\7\17\2\2QM\3\2\2\2QP\3\2\2\2R\31\3\2\2\2SV"+
		"\7\"\2\2TV\5\26\13\2US\3\2\2\2UT\3\2\2\2V\33\3\2\2\2WX\13\2\2\2X\35\3"+
		"\2\2\2YZ\7\'\2\2Z[\7C\2\2[\\\7V\2\2\\]\7V\2\2]^\7T\2\2^_\7a\2\2_`\7D\2"+
		"\2`a\7G\2\2ab\7I\2\2bc\7K\2\2cd\7P\2\2de\7\'\2\2e\37\3\2\2\2fg\7\'\2\2"+
		"gh\7C\2\2hi\7V\2\2ij\7V\2\2jk\7T\2\2kl\7a\2\2lm\7G\2\2mn\7P\2\2no\7F\2"+
		"\2op\7\'\2\2p!\3\2\2\2qr\7\f\2\2rs\7\13\2\2st\3\2\2\2tu\b\21\4\2u#\3\2"+
		"\2\2vw\7\f\2\2wx\b\22\5\2xy\3\2\2\2yz\b\22\6\2z%\3\2\2\2{|\7\"\2\2|}\b"+
		"\23\7\2}\'\3\2\2\2~\177\13\2\2\2\177\u0080\b\24\b\2\u0080)\3\2\2\2\n\2"+
		"\3=BDMQU\t\3\4\2\4\3\2\3\21\3\3\22\4\4\2\2\3\23\5\3\24\6";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}