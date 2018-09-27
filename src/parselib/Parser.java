package parselib;

import java.io.BufferedWriter;
import java.lang.ClassCastException;
import opast.*;

/**
 * Parser
 */
public class Parser {
    protected LookAhead1 reader;
    protected BufferedWriter bw;

    protected Boolean problemDefined;

    protected long clauseNb;
    protected Env env;

    public Parser(LookAhead1 l, BufferedWriter bw) {
        this.bw = bw;
        this.reader = l;
        problemDefined = false;
        this.clauseNb = 0;
        this.env = new Env();
    }

    public void mainLoop() throws Exception{
        while(!reader.hasEnded()) {
            blockLine(this.env);
        }
    }

    public void blockLine(Env e) throws Exception {
        Token t = reader.get();
        Symbol sym = t.getSymbol();

        switch(sym) {
            case COMMENT : commentLine(); break;
            case PROBLEM : problemLine(); break;
            case ZERO :
            case NUM :
                if(!problemDefined)
                    err("Problem not defined prior to first clause.", 24);
                clause(e);
                break;
            case AND : 
                if(e.parentIsOr())
                    Main.err("Can't use an AND loop inside an OR loop in CNF form.", 201);
                    andLoop(e);
                    break;
            case OR :
                orLoop(e);
                break;
            case VAR : varLine(e); break;
            case RCURV : 
            default : System.out.println("EOF.");
                System.exit(0);
        }

        bw.newLine();
    }

    private void andLoop(Env e) {

    }

    private void orLoop(Env e) {

    }

    private void commentLine() throws Exception {
        // System.out.println("II : " + reader.get().toString());
        String line = ((CommentToken)reader.pop()).getString();
        
        if (Main.getVerbose())
            System.out.println("c " + line);

        bw.write("c " + line);
    }

    private void problemLine() throws Exception {
        if(problemDefined)
            err("Problem line already defined", 20);
        problemDefined = true;

        reader.eat(Symbol.PROBLEM);
        String line = "p ";

        if(!reader.check(Symbol.WORD))
            err("Problem line has no problem type.", 21);

        line += ((WordToken) reader.pop()).getString() + " ";

        if(!reader.check(Symbol.NUM))
            err("Problem line has no variable number defined.", 22);

        line += ((IntToken) reader.pop()).getValue() + " ";

        if (!reader.check(Symbol.NUM))
            err("Problem line has no clause number defined.", 23);

        line += ((IntToken) reader.pop()).getValue();

        if(Main.getVerbose()) System.out.println(line);
        bw.write(line);
    }

    private void clause(Env e) throws Exception {
        clauseNb ++;

        Symbol sym = reader.get().getSymbol();

        String clause = "";

        if(sym == Symbol.ZERO)
            System.out.println("II : Empty clause No " + clauseNb);

        while(sym != Symbol.ZERO) {
            if(sym == Symbol.NUM)
                clause += ((IntToken) reader.pop()).getValue() + " ";
            else if (sym == Symbol.NEG) {
                clause += "-";
                reader.pop();
            } else err("Char in clause No " + Long.toString(clauseNb), 31);

            sym = reader.get().getSymbol();
        }

        reader.eat(Symbol.ZERO);

        if (Main.getVerbose())
            System.out.println(clause + "0");

        bw.write(clause + "0");
    }


    /**
     * A var line is defined like so :
     * 
     *     var name value;
     */
    private void varLine(Env e) throws Exception {
        reader.eat(Symbol.VAR);

        Token t = reader.pop();
        Symbol sym = t.getSymbol();

        if(!(sym == Symbol.WORD))
            Main.err("Var has no name.", 100);
        
        String name = ((WordToken) t).getString();

        sym = reader.pop().getSymbol();

        if(!(sym == Symbol.ASSIGN))
            Main.err("Var not assigned any value.", 101);
        
        t = reader.pop();
        sym = t.getSymbol();
        int val;

        if(sym == Symbol.NUM || sym == Symbol.ZERO)
            val = exprEval().eval(e);
        else
            Main.err("Var has no value.", 102);
        
        sym = reader.pop().getSymbol();

        if(sym != Symbol.SEMICOLON)
            Main.err("Missing ';'.", 103);

        e.addVar(name, val);
    }

    private IntEntity exprEval() {
        return exprEval(null);
    }

    private IntEntity exprEval(IntEntity left) {
        IntEntity right;
        AbstractOp op;

        if(left == null)
            left = parseExpr();
        
        boolean hasPriority = false;
        Symbol sym = reader.pop().getSymbol();

        switch (sym) {
            case GT :
            case LT :
            case LEQ :
            case GEQ :
            case EQ :
            case SEMICOLON :
            case LPAR :
            case NOT :
            case LAND :
            case LOR : 
                return left;
                break;
            case ADD :
                op = new AddOp(left);
                break;
            case MUL :
                op = new MulOp(left);
                hasPriority = true;
                break;
            case MOD :
                op = new ModOp(left);
                hasPriority = true;
                break;
            default:
                Main.err("No valid Operator.", 33);
                break;
        } // TODO put in seperated function.

        right = parseExpr();
        sym = reader.get().getSymbol();

        // if(sym == Symbol.SEMICOLON) {
        //     op.setRight(right);
        //     return op;
        // } else if (hasPriority || sym == ADD) {
        //     op.setRight(right);         
        // } else {
           
        // }

        switch (sym) {
            case SEMICOLON :
            case GT :
            case LT :
            case LEQ :
            case GEQ :
            case EQ :
            case NOT :
            case LAND :
            case LOR : 
                op.setRight(right);
                return op;
                break;
            default:
                if(hasPriority || sym == ADD) {
                    op.setRight(right);
                    return exprEval(op);
                }
                switch (sym) {
                    case MUL :
                    case MOD :
                        op.setRight(exprEval());
                        return op;
                    break;
                    default:
                        Main.err("No valid operator.", 34);
                        break;
                }
                break;
        }
    }

    private IntEntity parseExpr() {
        Symbol sym = reader.pop().getSymbol();

        if(sym == Symbol.LPAR)
            sym = reader.pop().getSymbol();
        
        switch(sym) {
            case LPAR : return exprEval(); break; // Parenthesis
            case NUM : 
                return new Val(((IntToken) reader.pop()).getValue());
                break;
            case WORD : 
                return new Var(((WordToken) reader.pop().getString()));
                break;
            case ZERO :
                reader.pop();
                return new Val(0);
                break;
            default :
                Main.err("Error while parsing expression : not a number or var.", 32);
        }
    }
}