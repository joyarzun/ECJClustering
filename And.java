import ec.*;
import ec.gp.*;

public class And extends GPNode{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3809619002033827584L;


	public String toString() { return "And"; }
	
	
	public int expectedChildren() { return 2; }
	
	
	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem)
	{
		MyGPData result = (MyGPData)(input);
		
		MyProblem myproblem = (MyProblem)problem;
		
        //We evaluate our left children.
        children[0].eval(state,thread,input,stack,individual,myproblem);
		MyGPData leftdata = (MyGPData)(input);
		
        if (leftdata.condicion == false)//Short circuit for AND, we must not continue evaluating the boolean expression (left AND right).
        {
        	result.condicion = false; 
        	result.punto = null;
            
            return;
        }
		
		//We evaluate our right children.
        children[1].eval(state,thread,input,stack,individual,myproblem);
		MyGPData rightdata = (MyGPData)(input);
		
        if (rightdata.condicion == false)//Short circuit for AND, we must not continue evaluating the boolean expression (left AND right).
        {
        	result.condicion = false; 
        	result.punto = null;
            
            return;
        }
		
    	result.condicion = true; 
    	result.punto = null;
	}

}