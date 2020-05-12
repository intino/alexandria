lexer grammar InlLexicon;
@header{
}
@lexer::members {
}

LSQUARE    			: '[';
RSQUARE   			: ']';
COLON               : ':' {setType(COLON);} -> mode(ATTR_MODE);
UNDERDASH           : '_';
DOT		            : '.';
DIGIT               : [0-9];
LETTER              : 'a'..'z' | 'A'..'Z' | 'ñ' | 'Ñ' | 'á'| 'é'| 'í' | 'ó' | 'ú'| 'Á'| 'É'| 'Í' | 'Ó' | 'Ú';

IDENTIFIER          : (LETTER | UNDERDASH) (DIGIT | LETTER | UNDERDASH)*;

NEWLINE_INDENT		: NEWLINE INDENT;
INDENT              :'\t';
NEWLINE             : ('\r'? '\n' | '\r');
SP                  : (' ' | INDENT);

UNKNOWN_TOKEN: . ;

ATTR_BEGIN        : '%ATTR_BEGIN%';
ATTR_END          : '%ATTR_END%';

mode ATTR_MODE;
	A_NEWLINE_INDENT:'\n\t'             {   setType(NEWLINE_INDENT); };
	A_NEWLINE:'\n'                      {   setType(NEWLINE); } -> mode(DEFAULT_MODE);
	A_SP:' ' 							{   setType(SP); };
	VALUE:~'\n'+                  	{   setType(VALUE); };


