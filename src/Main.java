/**
 * Main
 */

import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        /*
            args:
            1 - options
            2 - file path, must end with extention ".xdimacs"
            3 - output file name (default is filename.cnf)
            
            if no options, index --, if more than one option, ++ for every option.
        */

        if(args.length == 0) {
            System.err.println("No file passed as argument.");
            System.exit(10);
        }

        int index = 0;

        for(int i = 0; i < args.length; i ++) {
            if(s[0] != "-") {
                index = i;
                break;
            }
        }

        if(index == 0) {
            System.err.println("No file passed as argument.");
            System.exit(10);
        }

        String[] spl = args[index].split(".");
        
        if(spl[spl.length - 1] != "xdimacs") {
            System.err.println("Wrong file type.");
            System.exit(11);
        }

        Path inPath = Paths.get(args[index]), 
            outPath;

        if(index + 1 < args.length) {
            spl = args[index + 1].split(".");
            if(spl[spl.length - 1] == "cnf")
                outPath = Paths.get(args[index + 1]);
            else 
                outPath = Paths.get(args[index + 1] + ".cnf");
        } else {
            spl[spl.length - 1] = "cnf";
            outPath = Paths.get(String.join(".", spl));
        }
        
        Lexer lexer = null;
        LookAhead1 look = null;
        Parser parser = null;
        LinkedList<int[]> clauses = new LinkedList<>();
        
        try {
            File fin = new File(inPath);
            FileReader finr = new FileReader(fin);
            BufferedReader reader = new BufferedReader(fir);
            lexer = new Lexer(reader);
        } catch (Exception e) {
            System.err.println("Error while opening File.");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            File fout = new File(outPath);
            FileWriter foutw = new FileWriter(fout);
            BufferedReader writer = new BufferedWriter(foutw);
            lexer = new Lexer(reader);
        } catch (Exception e) {
            System.err.println("Error while creating File.");
            e.printStackTrace();
            System.exit(1);
        }


        try {
            look = new LookAhead1(lexer);
            parser = new Parser(look, writer);
        } catch (Exception e) {
            System.err.println("Error while opening parser.");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            parser.pNonTerm();
        } catch (Exception e) {
            System.err.println("Error while parsing.");
            e.printStackTrace();
            System.exit(1);
            return;
        }

        writer.close();
    }
}