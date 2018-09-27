package opast;

import parselib.Env;

/**
 * Val
 */
public class Val implements IntEntity {
    int val;

    public Val(int val) {
        this.val = val;
    }

    @Override
    public int eval(Env e) throws Exception {
        return val;
    }
}