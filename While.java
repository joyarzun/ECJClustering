import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ec.*;
import ec.gp.*;

public class While extends GPNode{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4002771103570543933L;
	private static Logger logNormal = LogManager.getLogger("logNormal");
	public Singleton sin = Singleton.getInstance();


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
		double erroranterior = Double.MAX_VALUE;
		int counterwhile = 0;
		int counterwhilemax = myproblem.instancia.getLSP_ORI().size()*2;
		
		while(condicion(state, thread, input, stack, individual, myproblem) && counterwhilemax > counterwhile){
			//We evaluate our right children.
	        children[1].eval(state,thread,input,stack,individual,myproblem);
			done = true;
			
			
			double error = (myproblem.instancia.error()+myproblem.instancia.noAgrupados())/2;
			if(sin.selogea) logNormal.debug(counter + " ERROR ACTUAL: "+ error + " ANTERIOR: " + erroranterior + " EVAL: " + (Math.abs(error - erroranterior) <= 1E-12) + " " +myproblem.instancia.getLCP().get(0).size());
			//SI EL ERROR NO CAMBIA
			if (Math.abs(error - erroranterior) <= 1E-12){
				counter++;
				if(counter == 2) break;
			}
			else counter = 0;
			
			erroranterior = error;
			counterwhile++;
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