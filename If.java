import ec.*;
import ec.gp.*;

public class If extends GPNode{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7861074072468456085L;


	public String toString() { return "If"; }
	
	
	public int expectedChildren() { return 2; }
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * If (left son)
	 * {
	 * 		rightSson
	 * 		return true;
	 * }
	 * else
	 * {
	 * 		return false;
	 * }
	 **/
	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem)
	{
		MyGPData result = (MyGPData)(input);
		
		MyProblem myproblem = (MyProblem)problem;
		
        //We evaluate our left children.
        children[0].eval(state,thread,input,stack,individual,myproblem);
		MyGPData leftdata = (MyGPData)(input);
		
        if (leftdata.condicion == true)
        {
			//We evaluate our right children.
	        children[1].eval(state,thread,input,stack,individual,myproblem);
			result.condicion = true; 
        	result.punto = null;
            
            return;
        }
		
    	result.condicion = false; 
    	// result.punto = null;
	}

}