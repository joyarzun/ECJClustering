import ec.*;
import ec.gp.*;
import ec.util.*;

public class Add_Maxce extends GPNode {
	public String toString() { return "Add_Maxce"; }
	
	public int expectedChildren() { return 0; }
	
    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
    	MyGPData d = ((MyGPData)(input));
		Punto punto_maxce = null;
		punto_maxce = ((MyProblem)problem).instancia.Add_Maxce();
		
		d.punto = punto_maxce;
		d.condicion = (d.punto != null);
    }

}