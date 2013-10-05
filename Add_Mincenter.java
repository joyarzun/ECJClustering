import ec.*;
import ec.gp.*;
import ec.util.*;

public class Add_Mincenter extends GPNode {
	public String toString() { return "Add_Mincenter"; }
	
	public int expectedChildren() { return 0; }
	
    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
    	MyGPData d = ((MyGPData)(input));
		
		Punto punto_mincenter = null;
		try {
			punto_mincenter = ((MyProblem)problem).instancia.Add_Mincenter();
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			System.exit(0);
		}
		 
		d.punto = punto_mincenter;
		d.condicion = (d.punto != null);
    }

}