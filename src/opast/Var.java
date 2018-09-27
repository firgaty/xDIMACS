package opast;

import parselib.Env;

/**
 * Var
 */
public class Var implements IntEntity {
    String name;
    
    public Var(String name) {
        this.name = name;
    }

    @Override
    public int eval(Env e) throws Exception {
        if(!e.has(name)) throw new Exception();
        return e.get(name);
    }
}