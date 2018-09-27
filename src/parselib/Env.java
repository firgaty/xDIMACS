package parselib;

import java.util.HashMap;

/**
 * Env
 */
public class Env {

    protected HashMap<String, Integer>  vars;
    int level;
    boolean parentIsOr;

    public Env(HashMap<String, Integer> vars, int level, boolean parentIsOr) {
        this.vars = vars;
        this.level = level;
        this.parentIsOr = parentIsOr;
    }

    public Env(HashMap<String, Integer> vars, int level) {
        this(vars, level, false);
    }

    public Env(HashMap<String, Integer> vars) {
        this(vars, 0);
    }

    public Env() {
        this(new HashMap<>());
    }

    public Env(Env e) {
        this.level = e.getLevel();
        this.vars = e.getVars();
        parentIsOr = false;
    }

    public Env clone() {
        return new Env(this.vars.clone(), level, parentIsOr);
    }

    public void addVar(String name, int val) {
        if(vars.containsKey(name)) 
            Main.err("Var already exists in the scope", 50);
        this.vars.put(name, val);
    }

    public void setVar(String name, int val) {
        if(!vars.containsKey(name))
        addVar(name, val);
        this.vars.replace(name, val);
    }

    public int get(String name) {
        return vars.get(name);
    }

    public boolean has(String name) {
        return vars.containsKey(name);
    }

    public HashMap<String, Integer> getVars() {
        return vars;
    }

    public int getLevel() {
        return level;
    }

    public boolean parentIsOr() {
        return parentIsOr;
    }

    public void setParentIsOr(boolean b) {
        this.parentIsOr = b;
    }

    public int incrementLevel() {
        this.level += 1;
        return level;
    }

    public int decrementLevel() {
        this.level -= 1;
        return level;
    }
}