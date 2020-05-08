parser grammar InlGrammar;
options { tokenVocab=InlLexicon; }

root: (message NEWLINE*)* EOF;
message: type body?;
body:NEWLINE (attribute NEWLINE*)*;
type: LSQUARE typeName RSQUARE;
attribute: IDENTIFIER COLON SP CHARACTER+;
typeName: hierarchy* IDENTIFIER;
hierarchy: IDENTIFIER (DOT);