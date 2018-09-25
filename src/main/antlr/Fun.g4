grammar Fun;

file : block EOF;

block : statement*;

blockWithBraces : '{' block '}';

statement :
            variable
          | expression
          | assignment
          | function
          | ifStatement
          | whileStatement
          | returnStatement;

variable : 'var' identifier ('=' expression)?;

function : 'fun' identifier '(' parameterNames ')' blockWithBraces;

parameterNames : (identifier (',' identifier)*)?;

whileStatement : 'while' '(' expression ')' blockWithBraces;

returnStatement : 'return' expression;

ifStatement : 'if' '(' expression ')' blockWithBraces ('else' blockWithBraces)?;

assignment : identifier '=' expression;

functionCall : identifier '(' arguments ')';

arguments : (expression (',' expression)*)?;

expression : logical;

logical : equality (('||' | '&&') equality)*;

equality : comparison (('==' | '!=') comparison)*;

comparison : addition (('<' | '>' | '<=' | '>=') addition)*;

addition : multiplication (('+' | '-') multiplication)*;

multiplication : atomic (('*' | '/' | '%') atomic)*;

atomic : functionCall | literal | identifier | '(' expression ')';

literal returns [int value]
    : x=Number {$value = Integer.parseInt($x.text);}
    ;

identifier returns [String value]
    : x=Id {$value = $x.text;}
    ;

Id
    :    (('a'..'z')|('A'..'Z')|'_') (('a'..'z')|('A'..'Z')|('0'..'9')|'_')*
    ;

Number
    :    '-'?('1'..'9')('0'..'9')* | '0'
    ;

WS : (' ' | '\t' | '\r'| '\n') -> skip;

LINE_COMMENT
    : '//' ~[\r\n]* -> skip
;