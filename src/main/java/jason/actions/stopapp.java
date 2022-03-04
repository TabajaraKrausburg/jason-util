package jason.actions;

import java.io.FileWriter;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class stopapp extends DefaultInternalAction {
    private static final long serialVersionUID = 5552929201215381277L;
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {        
        FileWriter f = new FileWriter(".stop___MAS");
        if (args.length > 0)
            f.write(String.valueOf(args[0]));
        // f.write(32);
        f.close();
        return true;
    }
    
}
