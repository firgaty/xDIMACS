package opast;

import parselib.Env;

/**
 * MulOp
 */
public class ModOp extends AbstractOp {

    public ModOp(IntEntity l, IntEntity r) {
        super(l, r);
    }

    public ModOp(IntEntity l) {
        this(l, null);
    }

    public ModOp() {
        this(null);
    }

    @Override
    public int eval(Env e) throws Exception {
        return left.eval(e) % right.eval(e);
    }
}