import java.util.ArrayList;



public class Singleton{
	public double fitness = Double.MAX_VALUE;
	public ArrayList<Double> error;
	public ArrayList<Double> no_agrupados;
	public ArrayList<SuperConjunto> mejoresLCP;
	public ArrayList<Conjunto> quedaLSP;
	public boolean selogea = false;
	public long Add_MindotElapsedTime = 0;
	public long Add_MindotCounter = 0;
	public long Create_CpElapsedTime = 0;
	public long Create_CpCounter = 0;
	public long Join_CpCounter = 0;
	public long Join_CpElapsedTime = 0;
	public long Move_MinElapsedTime = 0;
	public long Move_MinCounter = 0;
	public ConjuntoInstancia instancias = new ConjuntoInstancia();
	
	
	
	
	//SINGLETON SETTING
    private static Singleton instance = null;

    private Singleton(){}

    public static synchronized Singleton getInstance(){
        if (instance == null){
            instance = new Singleton();
        }
        return instance;
    }

	public void load(String instanciasPath, String[] instanciasFile, double alfa, double beta) {
		for (int a=0; a < instanciasFile.length; a++) {
			try{
				Instancia ins = new Instancia(instanciasPath, instanciasFile[a], alfa, beta);
				ins.load();
				instancias.add(ins);
			}
			catch (Exception e) {
				e.printStackTrace(System.out);
				System.exit(0);
			}
		}
	}
}