import ec.*;
import ec.gp.*;
import ec.util.*;

public class Add_Mince extends GPNode {
	public String toString() { return "Add_Mince"; }
	
	public int expectedChildren() { return 0; }
	
    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
    	MyGPData d = ((MyGPData)(input));
		Punto punto_mince = null;
		try {
			punto_mince = ((MyProblem)problem).instancia.Add_Mince();
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			System.exit(0);
		}
		
		d.punto = punto_mince;
		d.condicion = (d.punto != null);
    }

}