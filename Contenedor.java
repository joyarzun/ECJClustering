import java.util.ArrayList;

public class Contenedor {
	private static Contenedor INSTANCE = null;
	private static final String INSTANCIAS_PATH = ".//dataset//";
	// private static final String[] instancias_file = {"iris.data", "ruspini.data", "vowel.data"};
	private static final String[] instancias_file = {"test.data"};
	private static boolean isLoad = false;
	public static ArrayList<Instancia> instancias = new ArrayList<Instancia>();
	public static int cantidaderrores = 0;
	public static final double alfa = 0.5;
	public static final double beta = 0.5;
	
	public static boolean isLoad(){
		return isLoad;
	}
	
	
	public static boolean load(){
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
	
	
	// Private constructor suppresses 
    private Contenedor() {}

    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple 
    private synchronized static void createInstance() {
		if (INSTANCE == null) {
            // Sólo se accede a la zona sincronizada
            // cuando la instancia no está creada
            synchronized(Contenedor.class) {
                // En la zona sincronizada sería necesario volver
                // a comprobar que no se ha creado la instancia
                if (INSTANCE == null) { 
                    INSTANCE = new Contenedor();
                }
            }
        }
    }

    public static Contenedor getInstance() {
        createInstance();
        return INSTANCE;
    }
	
	//El método "clone" es sobreescrito por el siguiente que arroja una excepción:
	public Object clone() throws CloneNotSupportedException {
	        throw new CloneNotSupportedException(); 
	}
	
}