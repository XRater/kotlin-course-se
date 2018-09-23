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

returnStatement : 'retrun' expression;

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

atomic : literal | identifier | '(' expression ')' | functionCall;

literal returns [double value]
    : x=Number {$value = Double.parseDouble($x.text);}
    ;

identifier returns [String value]
    : x=Id {$value = $x.text;}
    ;

Id
    :    (('a'..'z')|('A'..'Z')|'_') (('a'..'z')|('A'..'Z')|('0'..'9')|'_')*
    ;

Number
    :    ('0'..'9')+ ('.' ('0'..'9')+)?
    ;

WS : (' ' | '\t' | '\r'| '\n') -> skip;
