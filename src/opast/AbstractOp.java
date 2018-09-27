package opast;

import parselib.Env;

/**
 * AbstractOp
 */
abstract public class AbstractOp implements IntEntity {
    protected IntEntity left, right;

    public AbstractOp (IntEntity left, IntEntity right) {
        this.left = left;
        this.right = right;
    }

    public AbstractOp (IntEntity left) {
        this(left, null);
    }

    public AbstractOp () {
        this(null, null);
    }

    abstract public int eval(Env e) throws Exception;

    public void setLeft(IntEntity left) {
        this.left = left;
    }
    public void setRight(IntEntity right) {
        this.right = right;
    }
}