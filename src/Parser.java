import java.io.BufferedWriter;

/**
 * Parser
 */
public class Parser {
    protected LookAhead1 reader;
    protected BufferedWriter bw;

    protected Boolean validProblem;

    public Parser(LookAhead1 l, BufferedWriter bw) {
        this.bw = bw;
        this.reader = l;
        validProblem = true;
    }

    public void pNonTerm() {
        while(!reader.hasEnded()) {
            iNonTerm();
        }
    }

    public void iNonTerm() {
        Token t = reader.get();
        Symbol sym = t.getSymbol();

        switch(sym) {
            case COMMENT : commentLine(); break;
            case PROBLEM : problemLine(); break;
        }

        bw.newLine();
    }

    private void commentLine() {
        reader.eat(Symbol.COMMENT);
        String line = (StringToken)reader.pop().getString();
        bw.write("c " + line);
    }

    private void problemLine() {
        if(!validProblem)
            err("Problem line already defined or not defined prior to any clause.", 20);

        reader.eat(Symbol.PROBLEM);
        String line = "p ";

        if(!reader.check(Symbol.WORD))
            err("Problem line has no problem type.", 21);

        line += (StringToken) reader.pop().getString() + " ";

        if(!reader.check(Symbol.NUM))
            err("Problem line has no variable number defined.", 22);

        line += (IntToken) reader.pop().getValue() + " ";

        if (!reader.check(Symbol.NUM))
            err("Problem line has no clause number defined.", 23);

        line += (IntToken) reader.pop().getValue();

        bw.write(line);
    }

    private err(String comment, int exitId) {
        System.err.println(comment);
        System.exit(exitId);
    }

}