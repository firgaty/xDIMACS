package opast;

import parselib.Env;

/**
 * MulOp
 */
public class MulOp extends AbstractOp {

    public MulOp(IntEntity l, IntEntity r) {
        super(l, r);
    }

    public MulOp(IntEntity l) {
        this(l, null);
    }

    public MulOp() {
        this(null);
    }
    
    @Override
    public int eval(Env e) throws Exception {
        return left.eval(e) * right.eval(e);
    }
}