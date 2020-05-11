parser grammar InlGrammar;
options { tokenVocab=InlLexicon; }

@members {
}
root: (message NEWLINE*)* EOF;
message: type body?;
body:NEWLINE (attribute NEWLINE*)*;
type: LSQUARE typeName RSQUARE;
attribute: IDENTIFIER SP? COLON SP? (value | multilineValue);

multilineValue: (NEWLINE_INDENT value)+;
value: (CHARACTER | SP)+;
typeName: hierarchy* IDENTIFIER;
hierarchy: IDENTIFIER (DOT);