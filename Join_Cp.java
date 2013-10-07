import ec.*;
import ec.gp.*;
import ec.util.*;

public class Join_Cp extends GPNode {
	public String toString() { return "Join_Cp"; }
	
	public int expectedChildren() { return 0; }
	
    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
    	MyGPData d = ((MyGPData)(input));
		
		((MyProblem)problem).instancia.Join_Cp();
		
		d.punto = null;
		d.condicion = true;
    }

}