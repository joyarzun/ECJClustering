import ec.*;
import ec.gp.*;
import ec.util.*;

public class Move_Min extends GPNode {
	public String toString() { return "Move_Min"; }
	
	public int expectedChildren() { return 0; }
	
    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
    	MyGPData d = ((MyGPData)(input));
		
		((MyProblem)problem).instancia.Move_Min();
		
		d.punto = null;
		d.condicion = true;
    }

}