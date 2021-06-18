lexer grammar InlLexicon;
@header{
}
@lexer::members {
}

LSQUARE    			: '[';
RSQUARE   			: ']';
COLON               : ':' {setType(COLON);} -> mode(ATTR_MODE);

DOT		            : '.';
IDENTIFIER          : (LETTER | UNDERDASH) (DIGIT | LETTER | UNDERDASH)*;

NEWLINE_INDENT		: NEWLINE '\t';
NEWLINE             : ('\n');

UNKNOWN_TOKEN: . ;

mode ATTR_MODE;
	A_NEWLINE_INDENT:'\n\t'             {   setType(NEWLINE_INDENT); };
	A_NEWLINE:'\n'                      {   setType(NEWLINE); } -> mode(DEFAULT_MODE);
	VALUE:~'\n'+                  		{   setType(VALUE); };


fragment LETTER              : 'a'..'z' | 'A'..'Z';
fragment DIGIT               : [0-9];
fragment UNDERDASH           : '_';