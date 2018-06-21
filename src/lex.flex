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

number = [0-9]*
// operator = "+" | "-" | "/" | "*"
blank = "\n" | "\r" | " " | "\t"
alpha = [a-zA-Z]
line = ^!.*
word = {alpha}*

%state COMMENT

%%
<YYINITIAL> {
    "c"             {yybegin(COMMENT);}
    "("             {return new Token(Symbol.LPAR);}
    ")"             {return new Token(Symbol.RPAR);}
    "p"             {return new Token(Symbol.PROBLEM);}
    "-"             {return new Token(Symbol.NEG);}
    ","             {return new Token(Symbol.COMMA);}
    ";"             {return new Token(Symbol.SEMICOLON);}
    " 0"            {return new Token(Symbol.ZERO);}
    {word}          {return new Token(Symbol.WORD);}
    {number}        {return new IntToken(Integer.parseInt(yytext()));}
    {blank}         {}
}
<COMMENT> {
    {line}            {return new Token(Symbol.LINE);yybegin(YYINITIAL);}
}
[^]             {throw new LexerException();}
