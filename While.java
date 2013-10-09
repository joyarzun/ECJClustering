import ec.*;
import ec.gp.*;
import ec.util.*;

public class While extends GPNode{
	public String toString() { return "While"; }
	
	
	public int expectedChildren() { return 2; }
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * while(leftSon)
	 * {
	 * 		rightSon
	 * }
	 **/
	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem)
	{
		MyGPData result = (MyGPData)(input);
		
		MyProblem myproblem = (MyProblem)problem;
		
        int counter = 0;
        boolean done = false;
		double erroranterior = 2;
		
		while(condicion(state, thread, input, stack, individual, myproblem) && myproblem.instancia.getLSP().size() > 0){
			//We evaluate our right children.
	        children[1].eval(state,thread,input,stack,individual,myproblem);
			done = true;
			
			double error = myproblem.instancia.fitness();
			//SI EL ERROR NO CAMBIA
			if (Math.abs(error - erroranterior) <= 1e-6){
				counter++;
				if(counter == 2) break;
			}
			else counter = 0;
			
			erroranterior = error;
		}
		

		
    	result.condicion = done; 
    	result.punto = null;
	}
	
	
    private boolean condicion(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final MyProblem problem)
    {
        if (input == null)
        {
        	return false;
        }
        
        //We evaluate our left son.
        children[0].eval(state, thread, input, stack, individual, problem);

        MyGPData result = (MyGPData)(input);
        
        return result.condicion;
    }

}