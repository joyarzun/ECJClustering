import ec.*;
import ec.gp.*;
import ec.util.*;

public class Or extends GPNode{
	public String toString() { return "Or"; }
	
	
	public int expectedChildren() { return 2; }
	
	
	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem)
	{
		MyGPData result = (MyGPData)(input);
		
		MyProblem myproblem = (MyProblem)problem;
		
        //We evaluate our left children.
        children[0].eval(state,thread,input,stack,individual,myproblem);
		MyGPData leftdata = (MyGPData)(input);
		
        if (leftdata.condicion == true)//Short circuit for OR, we must not continue evaluating the boolean expression (left OR right).
        {
        	result.condicion = true; 
        	result.punto = null;
            
            return;
        }
		
		//We evaluate our right children.
        children[1].eval(state,thread,input,stack,individual,myproblem);
		MyGPData rightdata = (MyGPData)(input);
		
        if (rightdata.condicion == true)//Short circuit for OR, we must not continue evaluating the boolean expression (left OR right).
        {
        	result.condicion = true; 
        	result.punto = null;
            
            return;
        }
		
    	result.condicion = false; 
    	result.punto = null;
	}

}