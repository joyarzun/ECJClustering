import ec.*;
import ec.gp.*;
import ec.util.*;

public class Add_Mindot extends GPNode {
	public String toString() { return "Add_Mindot"; }
	
	public int expectedChildren() { return 0; }
	
    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
    	MyGPData d = ((MyGPData)(input));
		Punto punto_mindot = ((MyProblem)problem).instancia.Add_Mindot();
		d.punto = punto_mindot;
		d.condicion = (d.punto != null);
    }

}