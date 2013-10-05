import ec.*;
import ec.gp.*;
import ec.util.*;

public class Not extends GPNode{
	public String toString() { return "Not"; }
	
	
	public int expectedChildren() { return 1; }
	
	
	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem)
	{
		MyGPData result = (MyGPData)(input);
		
		MyProblem myproblem = (MyProblem)problem;
		
        //We evaluate our left children.
        children[0].eval(state,thread,input,stack,individual,myproblem);
		MyGPData data = (MyGPData)(input);
		
		
        if (data.condicion == true)
        {
			result.condicion = false; 
        	result.punto = null;
            
            return;
        }
		else{
			result.condicion = true; 
        	result.punto = null;
            
            return;
		}
	}

}