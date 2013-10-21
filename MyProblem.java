import java.util.ArrayList;

import ec.util.*;
import ec.*;
import ec.gp.*;
import ec.gp.koza.*;
import ec.simple.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyProblem extends GPProblem implements SimpleProblemForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2446963225855949037L;
	private static Logger logNormal = LogManager.getLogger("logNormal");
	private static Logger logResumido = LogManager.getLogger("logResumido");
	public Instancia instancia;
	public Singleton sin = Singleton.getInstance();
	public Contenedor contenedor;
	
	public static final String P_DATA = "data";
	
	
	public void setup(final EvolutionState state, final Parameter base)
    {
		logNormal.info("setup problem!");
	    logResumido.info("setup problem!");
	    System.out.println("setup problem!");
		// very important, remember this
	    super.setup(state,base);

	    // verify our input is the right class (or subclasses from it)
	    if (!(input instanceof MyGPData)) state.output.fatal("GPData class must subclass from " + MyGPData.class, base.push(P_DATA), null);

	    
	    sin.load(Contenedor.INSTANCIAS_PATH, Contenedor.instancias_file, Contenedor.alfa, Contenedor.beta);
//	    contenedor = new Contenedor(sin.instancias);
    }
	
	public void evaluate(final EvolutionState state,final Individual ind,final int subpopulation,final int threadnum)
    {
    	if (!ind.evaluated)
		{
    		if(sin.selogea){
    			logNormal.debug("evaluando****** \n");
    			logResumido.debug("evaluando****** \n");
    		}
			MyGPData input = (MyGPData)(this.input);
			
			
			int hits = 0;
            double sum = 0.0;
            double result;
            ArrayList<SuperConjunto> LCP_ = new ArrayList<SuperConjunto>();
        	ArrayList<Conjunto> LSP_ = new ArrayList<Conjunto>();
        	ArrayList<Double> error = new ArrayList<Double>();
        	ArrayList<Double> no_agrupados = new ArrayList<Double>();
			
			for(Instancia i : contenedor.instancias){
				if(sin.selogea) logNormal.debug(i.toString());
				instancia = i;
				instancia.recargar();
                ((GPIndividual)ind).trees[0].child.eval(state,threadnum,input,stack,((GPIndividual)ind),this);
			
				result = instancia.fitness(((GPIndividual)ind).size());
				//GUARDAMOS COMO QUEDO, POR SI ES EL MEJOR
				LCP_.add(new SuperConjunto(instancia.getLCP()));
				LSP_.add(new Conjunto(instancia.getLSP()));
				error.add(instancia.error());
				no_agrupados.add(instancia.noAgrupados());
				//HIT
				if (result <= 0.01) hits++;
				sum += result;
			}
			if(sin.fitness > sum){
				sin.fitness = sum;
				sin.mejoresLCP = new ArrayList<SuperConjunto>(LCP_);
				sin.quedaLSP = new ArrayList<Conjunto>(LSP_);
				sin.error = new ArrayList<Double>(error);
				sin.no_agrupados = new ArrayList<Double>(no_agrupados);
			}
			
			// the fitness better be KozaFitness!
            KozaFitness f = ((KozaFitness)ind.fitness);
            f.setStandardizedFitness(state,(float)sum);
            f.hits = hits;
            ind.evaluated = true;
            if(sin.selogea){
            	logNormal.debug("fin evaluaci—n*****, fitness {}", sum);
            	logResumido.debug("fin evaluaci—n*****, fitness {}", sum);
            }
		}
    }
	
	public synchronized void describe(EvolutionState state, Individual ind, int subpopulation,int threadnum, int log){
		for(int i = 0; i < sin.mejoresLCP.size(); i++){
			state.output.println(contenedor.instancias.get(i).toString(), 0);
			state.output.println("Error " + sin.error.get(i) + " ,ajustado " + sin.error.get(i)*Contenedor.alfa, 0);
			state.output.println("No agrupados " + sin.no_agrupados.get(i) + " ,ajustado " + sin.no_agrupados.get(i)*Contenedor.beta, 0);
			state.output.println("LCP\n" + sin.mejoresLCP.get(i).toString(), 0);
			state.output.println("LSP\n" + sin.quedaLSP.get(i).toString(), 0);
		}
		long totalElapsed = sin.Add_MindotElapsedTime+sin.Create_CpElapsedTime+sin.Join_CpElapsedTime+sin.Move_MinElapsedTime;
		state.output.println("Add_Min " + (double)sin.Add_MindotElapsedTime/sin.Add_MindotCounter + " (" + sin.Add_MindotElapsedTime +"|"+ (double)sin.Add_MindotElapsedTime/totalElapsed +"|"+ sin.Add_MindotCounter + ")", 0);
		state.output.println("Create_Cp " + (double)sin.Create_CpElapsedTime/sin.Create_CpCounter + " (" + sin.Create_CpElapsedTime +"|"+ (double)sin.Create_CpElapsedTime/totalElapsed +"|"+sin.Create_CpCounter + ")", 0);
		state.output.println("Join_Cp " + (double)sin.Join_CpElapsedTime/sin.Join_CpCounter + " (" + sin.Join_CpElapsedTime +"|"+ (double)sin.Join_CpElapsedTime/totalElapsed +"|"+sin.Join_CpCounter + ")", 0);
		state.output.println("Move_Min " + (double)sin.Move_MinElapsedTime/sin.Move_MinCounter + " (" + sin.Move_MinElapsedTime +"|"+ (double)sin.Move_MinElapsedTime/totalElapsed +"|"+sin.Move_MinCounter + ")", 0);
		
		ind.evaluated = false;
		sin.selogea = true;
		
		if(sin.selogea){
			String treelatex = ((GPIndividual)ind).trees[0].child.makeLatexTree();
			String fitness = ((GPIndividual)ind).fitness.fitnessToStringForHumans();
			logNormal.debug("\n" + fitness + "\n" + treelatex + "\n");
		}
		this.evaluate(state, ind, subpopulation, threadnum);
	}

	/* (non-Javadoc)
	 * @see ec.gp.GPProblem#clone()
	 */
	@Override
	public MyProblem clone() {
		MyProblem p = (MyProblem)super.clone();
		p.contenedor = new Contenedor(sin.instancias);
		return p;
	}
	
	
	
	
//	public void finishEvaluating(EvolutionState state, int thread){
//		System.out.println("finishEvaluating");
//	}
}