

public class Contenedor {
	public static final String INSTANCIAS_PATH = ".//dataset//";
	// private  final String[] instancias_file = {"iris.data", "ruspini.data", "vowel.data"};
	public static final String[] instancias_file = {"test.data","ruspini.data"};
	private boolean isLoad = false;
	public ConjuntoInstancia instancias = new ConjuntoInstancia();
	public int cantidaderrores = 0;
	public static final double alfa = 0.92;
	public static final double beta = 0.04;
	
	public Contenedor(ConjuntoInstancia instancias2) {
		instancias = new ConjuntoInstancia(instancias2);
		isLoad = true;
	}


	public Contenedor() {
		// TODO Auto-generated constructor stub
	}


	public  boolean isLoad(){
		return isLoad;
	}
	
	
	public  boolean load(){
		if(!isLoad){
			for (int a=0; a < instancias_file.length; a++) {
				try{
					Instancia ins = new Instancia(INSTANCIAS_PATH, instancias_file[a], alfa, beta);
					ins.load();
					instancias.add(ins);
				}
				catch (Exception e) {
					e.printStackTrace(System.out);
					return false;
				}
			}
			isLoad = true;
		}
		return isLoad;
	}
}