package opast;

import parselib.Env;

/**
 * MulOp
 */
public class AddOp extends AbstractOp {

    public AddOp(IntEntity l, IntEntity r) {
        super(l, r);
    }

    public AddOp(IntEntity l) {
        this(l, null);
    }

    public AddOp() {
        this(null, null);
    }

    @Override
    public int eval(Env e) throws Exception {
        return left.eval(e) + right.eval(e);
    }
}