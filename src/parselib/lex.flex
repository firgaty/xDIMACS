package parselib;

%%
%public
%class Lexer
%unicode
%type Token
%line
%column

%yylexthrow LexerException

%{
public class LexerException extends Exception {

  public LexerException () {
    super("Lexer Exception, unexpected caracter found at line " + yyline + " and column " + yycolumn + ".");
  }
}
%}

number = [0-9][0-9]*
// operator = "+" | "-" | "/" | "*"
blank = "\n" | "\r" | " " | "\t"
alpha = [a-zA-Z]
line = .*
word = {alpha}{alpha}*

%state COMMENT
%%
<YYINITIAL> {
    "AND"           {return new Token(Symbol.AND);} // AND loop.
    "OR"            {return new Token(Symbol.OR);} // OR loop.
    "c "            {yybegin(COMMENT);}
    "("             {return new Token(Symbol.LPAR);}
    ")"             {return new Token(Symbol.RPAR);}
    "p "            {return new Token(Symbol.PROBLEM);}
    "IF"            {return new Token(Symbol.IF);}
    "-"             {return new Token(Symbol.NEG);}
    ","             {return new Token(Symbol.COMMA);}
    ";"             {return new Token(Symbol.SEMICOLON);}
    ":"             {return new Token(Symbol.COLON);}
    "0 "|"0\n"      {return new Token(Symbol.ZERO);}
    "|"             {return new Token(Symbol.LOR);} // Logical OR.
    "&"             {return new Token(Symbol.LAND);} // Logical AND.
    "!"             {return new Token(Symbol.NOT);}
    "+"             {return new Token(Symbol.ADD);}
    "*"             {return new Token(Symbol.MUL);}
    "%"             {return new Token(Symbol.MOD);}
    "<"             {return new Token(Symbol.LT);}
    ">"             {return new Token(Symbol.GT);}
    "<="            {return new Token(Symbol.GEQ);}
    ">="            {return new Token(Symbol.LEQ);}
    "="             {return new Token(Symbol.EQ);}
    ":="            {return new Token(Symbol.ASSIGN);}
    "var"           {return new Token(Symbol.VAR);}
    {word}          {return new WordToken(yytext());}
    {number}        {return new IntToken(Integer.parseInt(yytext()));}
    "\n"            {return new Token(Symbol.ENDL);}
    {blank}         {}
    [^]             {throw new LexerException();}
}
<COMMENT> {
    "\n"            {yybegin(YYINITIAL);}
    {line}          {return new CommentToken(yytext());}
}

