import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
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
	private static Logger logEstadistica = LogManager.getLogger("logEstadistica");
	public Instancia instancia;
	public Singleton sin = Singleton.getInstance();
	public Contenedor contenedor;
	
	public static final String P_DATA = "data";
	
	
	public void setup(final EvolutionState state, final Parameter base)
    {
		logNormal.info("setup problem!");
	    logResumido.info("setup problem!");
	    logEstadistica.info("setup problem!");
		// very important, remember this
	    super.setup(state,base);

	    // verify our input is the right class (or subclasses from it)
	    if (!(input instanceof MyGPData)) state.output.fatal("GPData class must subclass from " + MyGPData.class, base.push(P_DATA), null);

	    
	    sin.load(Contenedor.INSTANCIAS_PATH, Contenedor.instancias_file, Contenedor.alfa, Contenedor.beta, Contenedor.gama);
	    
	    
	}
	
	public void evaluate(final EvolutionState state,final Individual ind,final int subpopulation,final int threadnum)
    {
    	if (!ind.evaluated)
		{
    		if(sin.selogea){
    			logNormal.debug("evaluando****** \n");
//    			logResumido.debug("evaluando****** \n");
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
				if(sin.selogea){
					logNormal.debug(i.toString());
					System.out.println("\n" + i.toString());
				}
				instancia = i;
				instancia.recargar();
				long startTime = System.currentTimeMillis();
                ((GPIndividual)ind).trees[0].child.eval(state,threadnum,input,stack,((GPIndividual)ind),this);
                int totaltime = (int) (System.currentTimeMillis() - startTime);
			
				result = instancia.fitness(((GPIndividual)ind).size(), totaltime);
				//GUARDAMOS COMO QUEDO, POR SI ES EL MEJOR
				LCP_.add(new SuperConjunto(instancia.getLCP()));
				LSP_.add(new Conjunto(instancia.getLSP()));
				error.add(instancia.error());
				no_agrupados.add(instancia.noAgrupados());
				//HIT
				if (result <= 0.01) hits++;
				sum += result;
			}
			
			sum = sum/contenedor.instancias.size();
			
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
            	logNormal.debug("fin evaluación *****, fitness {}\n\n", sum);
//            	logResumido.debug("fin evaluación *****, fitness {}\n\n", sum);
            }
		}
    }
	
	public void describe(EvolutionState state, Individual ind, int subpopulation,int threadnum, int log){
		//GUARDO EL MEJOR IND
		state.output.println("SAVE BEST",0);
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new FileWriter("bestInd.txt", true));
			((GPIndividual)ind).printIndividual(state, printWriter);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		printWriter.close();
		
		//SE EVALUA Y LOGEA LA INFO DEL MEJOR INDIVIDUO
		ind.evaluated = false;
		sin.selogea = true;
		String tamNodo = "Tamaño del nodo: " + ((GPIndividual)ind).size();
		
		if(sin.selogea){
			String treelatex = ((GPIndividual)ind).trees[0].child.makeLatexTree();
			String treegraphviz = ((GPIndividual)ind).trees[0].child.makeGraphvizTree();
			String fitness = ((GPIndividual)ind).fitness.fitnessToStringForHumans();
			
			logNormal.debug("\n" + fitness + "\n" + treelatex + "\n" + tamNodo + "\n" + treegraphviz + "\n");
			logResumido.debug("\n" + fitness + "\n" + treelatex + "\n" + tamNodo + "\n" + treegraphviz + "\n");
		}
		
		state.output.println(tamNodo, 0);
		for(int i = 0; i < sin.mejoresLCP.size(); i++){
			state.output.println(contenedor.instancias.get(i).toString(), 0);
			state.output.println("Error " + sin.error.get(i) + " ,ajustado " + sin.error.get(i)*Contenedor.alfa, 0);
			state.output.println("No agrupados " + sin.no_agrupados.get(i) + " ,ajustado " + sin.no_agrupados.get(i)*Contenedor.beta, 0);
			state.output.println("LCP\n" + sin.mejoresLCP.get(i).toString(), 0);
			state.output.println("LSP\n" + sin.quedaLSP.get(i).toString(), 0);
			if(sin.selogea){
				logResumido.debug(contenedor.instancias.get(i).toString());
				logResumido.debug("Error " + sin.error.get(i) + " ,ajustado " + sin.error.get(i)*Contenedor.alfa);
				logResumido.debug("No agrupados " + sin.no_agrupados.get(i) + " ,ajustado " + sin.no_agrupados.get(i)*Contenedor.beta);
				logResumido.debug("LCP\n" + sin.mejoresLCP.get(i).toString());
				logResumido.debug("LSP\n" + sin.quedaLSP.get(i).toString());
			}
			
		}
		long totalElapsed = sin.Add_MindotElapsedTime+sin.Create_CpElapsedTime+sin.Join_CpElapsedTime+sin.Move_MinElapsedTime;
		state.output.println("Add_Min " + (double)sin.Add_MindotElapsedTime/sin.Add_MindotCounter + " (" + sin.Add_MindotElapsedTime +"|"+ (double)sin.Add_MindotElapsedTime/totalElapsed +"|"+ sin.Add_MindotCounter + ")", 0);
		state.output.println("Create_Cp " + (double)sin.Create_CpElapsedTime/sin.Create_CpCounter + " (" + sin.Create_CpElapsedTime +"|"+ (double)sin.Create_CpElapsedTime/totalElapsed +"|"+sin.Create_CpCounter + ")", 0);
		state.output.println("Join_Cp " + (double)sin.Join_CpElapsedTime/sin.Join_CpCounter + " (" + sin.Join_CpElapsedTime +"|"+ (double)sin.Join_CpElapsedTime/totalElapsed +"|"+sin.Join_CpCounter + ")", 0);
		state.output.println("Move_Min " + (double)sin.Move_MinElapsedTime/sin.Move_MinCounter + " (" + sin.Move_MinElapsedTime +"|"+ (double)sin.Move_MinElapsedTime/totalElapsed +"|"+sin.Move_MinCounter + ")", 0);
		
		
//		this.evaluate(state, ind, subpopulation, threadnum);
		if(sin.selogea) logResumido.debug("Termino");
		
		//SI EL FITNEES ES MEJOR QUE 0.5 SE USA INSTANCIA DE VALIDACION
		KozaFitness f = ((KozaFitness)((GPIndividual)ind).fitness);
		if(f.standardizedFitness() <= 0.5){
			state.output.println("\nUSANDO INSTANCIAS DE VALIDACION************\n",0);
			logResumido.debug("\nUSANDO INSTANCIAS DE VALIDACION***************\n");
			contenedor.loadvalidacion();
			ind.evaluated = false;
			sin.reset();
			this.evaluate(state, ind, subpopulation, threadnum);
			String fitness = ((GPIndividual)ind).fitness.fitnessToStringForHumans();
			state.output.println("\n" + fitness + "\n" + tamNodo + "\n",0);
			logResumido.debug("\n" + fitness + "\n" + tamNodo + "\n");
			
			for(int i = 0; i < sin.mejoresLCP.size(); i++){
				state.output.println(contenedor.instancias.get(i).toString(), 0);
				state.output.println("Error " + sin.error.get(i) + " ,ajustado " + sin.error.get(i)*Contenedor.alfa, 0);
				state.output.println("No agrupados " + sin.no_agrupados.get(i) + " ,ajustado " + sin.no_agrupados.get(i)*Contenedor.beta, 0);
				state.output.println("LCP\n" + sin.mejoresLCP.get(i).toString(), 0);
				state.output.println("LSP\n" + sin.quedaLSP.get(i).toString(), 0);
				if(sin.selogea){
					logResumido.debug(contenedor.instancias.get(i).toString());
					logResumido.debug("Error " + sin.error.get(i) + " ,ajustado " + sin.error.get(i)*Contenedor.alfa);
					logResumido.debug("No agrupados " + sin.no_agrupados.get(i) + " ,ajustado " + sin.no_agrupados.get(i)*Contenedor.beta);
					logResumido.debug("LCP\n" + sin.mejoresLCP.get(i).toString());
					logResumido.debug("LSP\n" + sin.quedaLSP.get(i).toString());
				}
				
			}
		}
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
	
	
	public void prepareToEvaluate(final EvolutionState state, final int threadnum){
		if(state.generation == 0){
			GPIndividual ind = (GPIndividual) state.population.subpops[0].individuals[0];
		    LineNumberReader reader = null;
		    try {
				reader = new LineNumberReader(new FileReader("bestIndofAll.txt"));
				ind.readIndividual(state, reader);
				ind.evaluated = false;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public void finishEvaluating(EvolutionState state, int thread){
		if(thread == 0){
			//LOG ESTADISTICA
			MySimpleStatistics s = (MySimpleStatistics) state.statistics;
			GPIndividual indbest = (GPIndividual) s.best_of_run[0];
			GPIndividual indworst = (GPIndividual) s.worst_of_run[0];
			float avg_f = s.avg_of_run[0];
			String log = "";
			if(indbest != null){
				KozaFitness f = ((KozaFitness)indbest.fitness);
				log += f.standardizedFitness();
			}
			if(indworst != null){
				KozaFitness f = ((KozaFitness)indworst.fitness);
				log += " " + f.standardizedFitness();
			}
			if(state.generation != 0) log += " " + avg_f;
			if(state.generation != 0) logEstadistica.info(log);
		}
	}
}
