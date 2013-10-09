import java.util.ArrayList;
import ec.util.*;
import ec.*;
import ec.gp.*;
import ec.gp.koza.*;
import ec.simple.*;

public class MyProblem extends GPProblem implements SimpleProblemForm
{
	public Contenedor contenedor = Contenedor.getInstance();
	public Instancia instancia;
	
	public static final String P_DATA = "data";
	
	
	public void setup(final EvolutionState state, final Parameter base)
    {
	    // very important, remember this
	    super.setup(state,base);

	    // verify our input is the right class (or subclasses from it)
	    if (!(input instanceof MyGPData)) state.output.fatal("GPData class must subclass from " + MyGPData.class, base.push(P_DATA), null);
		
		contenedor.load();
    }
	
	public void evaluate(final EvolutionState state,final Individual ind,final int subpopulation,final int threadnum)
    {
    	if (!ind.evaluated)
		{
			MyGPData input = (MyGPData)(this.input);
			
			int hits = 0;
            double sum = 0.0;
            double expectedResult;
            double result;
			
			for(Instancia i : contenedor.instancias){
				instancia = i;
				instancia.recargar();
                ((GPIndividual)ind).trees[0].child.eval(state,threadnum,input,stack,((GPIndividual)ind),this);
				result = instancia.fitness();
				//HIT
				if (result <= 0.01) hits++;
				sum += result;
			}
			// the fitness better be KozaFitness!
            KozaFitness f = ((KozaFitness)ind.fitness);
            f.setStandardizedFitness(state,(float)sum);
            f.hits = hits;
            ind.evaluated = true;
		}
    }
	
	public void describe(EvolutionState state, Individual ind, int subpopulation,int threadnum, int log){
		state.output.println("****** Conjuntos " + contenedor.instancias.get(0).getLCP().size(), 0);
		state.output.println("****** errores " + contenedor.cantidaderrores, 0);
		int count = 0;
		for(Conjunto c : contenedor.instancias.get(0).getLCP()){
			for(Punto p : c.getConjunto()){
				state.output.println("C"+count + " " + p.toString(), 0);
			}
			count++;
		}
	}
	
	// public void finishEvaluating(EvolutionState state, int thread){
// 		Contenedor c = Contenedor.getInstance();
// 		
// 		state.output.println("***********finishEvaluating " + c.test, 0);
// 		c.test++;
// 	}
}