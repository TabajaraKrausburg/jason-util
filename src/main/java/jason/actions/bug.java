package jason.actions;

import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class bug extends DefaultInternalAction{
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // throw new Exception("This is a bug");
        // throw new Error("This is a bug");
        throw new JasonException("This is a bug");
    }
}
