import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.lang.Math;

public class Instancia{
	private String filename;
	private String path;
	private boolean isLoad;
	private boolean hasSolution;
	private int dimension;
	private double alfa;
	private double beta;
	private ArrayList<Punto> LSP = new ArrayList<Punto>();//Lista de secuencia de puntos
	private ArrayList<Punto> LSP_ORI = new ArrayList<Punto>();//Lista de secuencia de puntos
	private ArrayList<Conjunto> LCP = new ArrayList<Conjunto>();//Lista de conjuntos de puntos
	private ArrayList<Punto> LCC = new ArrayList<Punto>();//Lista de centros de conjuntos. Contiene por cada conjunto de LCP, una coordenada que es el centro geométrico de dicho conjunto
	private Punto CE;
	
	public double mejorfitness = Double.MAX_VALUE;
	public ArrayList<Conjunto> mejorLCP = new ArrayList<Conjunto>();
	
	//DIVIDE LSP COMO LA TESIS DE DIB EJEMPLO 1
	public void dividirDIB1() throws Exception{
		Conjunto c1 = new Conjunto();
		Conjunto c2 = new Conjunto();
		Conjunto c3 = new Conjunto();
		Conjunto c4 = new Conjunto();
		Conjunto c5 = new Conjunto();
		
		c1.addPunto(LSP.remove(0));
		c1.addPunto(LSP.remove(0));
		c2.addPunto(LSP.remove(0));
		c2.addPunto(LSP.remove(0));
		c3.addPunto(LSP.remove(0));
		c3.addPunto(LSP.remove(0));
		c4.addPunto(LSP.remove(0));
		c4.addPunto(LSP.remove(0));
		c5.addPunto(LSP.remove(0));
		c5.addPunto(LSP.remove(0));
		
		LCP.add(c1);
		LCP.add(c2);
		LCP.add(c3);
		LCP.add(c4);
		LCP.add(c5);
	}
	
	//DIVIDE LSP COMO LA TESIS DE DIB EJEMPLO 2
	public void dividirDIB2() throws Exception{
		Conjunto c1 = new Conjunto();
		Conjunto c2 = new Conjunto();
		Conjunto c3 = new Conjunto();
		Conjunto c4 = new Conjunto();
		
		c1.addPunto(LSP.remove(0));
		c1.addPunto(LSP.remove(0));
		c2.addPunto(LSP.remove(0));
		c2.addPunto(LSP.remove(0));
		c3.addPunto(LSP.remove(0));
		c3.addPunto(LSP.remove(0));
		c4.addPunto(LSP.remove(0));
		c4.addPunto(LSP.remove(0));
		c4.addPunto(LSP.remove(0));
		c4.addPunto(LSP.remove(0));
		
		LCP.add(c1);
		LCP.add(c2);
		LCP.add(c3);
		LCP.add(c4);
	}
	
	
	//DIVIDE LSP COMO LA TESIS DE DIB EJEMPLO 3
	public void dividirDIB3() throws Exception{
		Conjunto c1 = new Conjunto();
		Conjunto c2 = new Conjunto();
		Conjunto c3 = new Conjunto();
		
		c1.addPunto(LSP.remove(0));
		c1.addPunto(LSP.remove(0));
		c2.addPunto(LSP.remove(0));
		c2.addPunto(LSP.remove(0));
		c3.addPunto(LSP.remove(0));
		c3.addPunto(LSP.remove(0));
		c3.addPunto(LSP.remove(0));
		c3.addPunto(LSP.remove(0));
		c3.addPunto(LSP.remove(0));
		c3.addPunto(LSP.remove(0));
		
		LCP.add(c1);
		LCP.add(c2);
		LCP.add(c3);
	}
	
	
	//DIVIDE LSP COMO LA TESIS DE DIB EJEMPLO 4
	public void dividirDIB4() throws Exception{
		Conjunto c1 = new Conjunto();
		Conjunto c2 = new Conjunto();
		
		c1.addPunto(LSP.remove(0));
		c1.addPunto(LSP.remove(0));
		c1.addPunto(LSP.remove(0));
		c1.addPunto(LSP.remove(0));
		c2.addPunto(LSP.remove(0));
		c2.addPunto(LSP.remove(0));
		c2.addPunto(LSP.remove(0));
		c2.addPunto(LSP.remove(0));
		c2.addPunto(LSP.remove(0));
		c2.addPunto(LSP.remove(0));
		
		LCP.add(c1);
		LCP.add(c2);
	}
	
	
	
	//DIVIDE LSP EN DOS CONJUNTOS PARA HACER PRUEBAS
	public void dividirLSP() throws Exception{
		if(isLoad && LSP.size() > 1){
			int mitad = LSP.size()/2;
			Conjunto c1 = new Conjunto();
			Conjunto c2 = new Conjunto();
			for (int i = 0; i < mitad; i++) {
				c1.addPunto(LSP.get(0));
				LSP.remove(0);
			}
			
			mitad = LSP.size();
			for (int i = 0; i < mitad; i++) {
				c2.addPunto(LSP.get(0));
				LSP.remove(0);
			}
			LCP.add(c1);
			LCP.add(c2);
		}
		else throw new Exception("La instancia no esta cargada o no tiene más de dos puntos");
		
	}
	
	
	//DIVIDE LSP EN UN CONJUNTOS PARA HACER PRUEBAS
	public void dividirUNO() throws Exception{
		if(isLoad){
			Conjunto c1 = new Conjunto();
			int counter = LSP.size();
			for (int i = 0; i < counter; i++) {
				c1.addPunto(LSP.get(0));
				LSP.remove(0);
			}
			LCP.add(c1);
		}
		else throw new Exception("La instancia no esta cargada o no tiene más de dos puntos");
		
	}
	
	
	//Toma un punto de LSP y lo inserta en el conjunto de LCP donde la distancia al punto de LCP sea la mínima
	public Punto Add_Mindot(){
		if(LSP.size() == 0) return null;
		Punto p = LSP.remove(0);
		
		if(LCP.size() == 0){//SI NO HAY CONJUNTOS SE CREA UNO
			Conjunto c = new Conjunto();
			c.addPunto(p);
			LCP.add(c);
		}
		else{//SI HAY ENTONCES SE INSERTA EN EL DE DISTANCIA MINIMA
			double dis_min = Double.MAX_VALUE;
			Conjunto con_dis_min = null;
			for(Conjunto c : LCP){
				if(c.getConjunto().size() > 0){
					double dis_aux = 0;
				
					try {
						dis_aux = this.d(p, c);
					}
					catch (Exception e) {
						e.printStackTrace(System.out);
						System.exit(0);
					}
				
					if(dis_aux < dis_min){
						dis_min = dis_aux;
						con_dis_min = c;
					}
				}
			}
			
			if(con_dis_min != null) con_dis_min.addPunto(p);
			else return null;
		}
		
		return p;
	}
	
	//ADD_NEXTOP: Toma el punto siguiente en LSP y lo inserta en un nuevo conjunto
	public Punto Add_Nextop(){
		if(LSP.size() == 0) return null;
		Punto p = LSP.remove(0);
		
		Conjunto c = new Conjunto();
		c.addPunto(p);
		LCP.add(c);
		
		return p;
	}
	
	
	//ADD_MINCENTER: Toma un punto de LSP y lo inserta en el conjunto LCP donde la distancia al centro del conjunto, dada por LCC, sea mínima.
	public Punto Add_Mincenter(){
		if(LSP.size() == 0) return null;
		Punto p = LSP.remove(0);
		
		if(LCP.size() == 0){//SI NO HAY CONJUNTOS SE CREA UNO
			Conjunto c = new Conjunto();
			c.addPunto(p);
			LCP.add(c);
		}
		else{//SI HAY ENTONCES SE INSERTA EN EL DE DISTANCIA MINIMA
			this.updateLCC();
			double dis_min = Double.MAX_VALUE;
			int index = 0;
			for (int i = 0; i > LCC.size(); i++) {
				double dis_medida = 0;
				try {
					dis_medida = LCC.get(i).distancia(p);
				}
				catch (Exception e) {
					e.printStackTrace(System.out);
					System.exit(0);
				}
				if(dis_min > dis_medida){
					dis_min = dis_medida;
					index = i;
				}
			}
			LCP.get(index).addPunto(p);
		}
		
		return p;
	}
	
	
	//ADD_MINCE: Toma un punto de LSP que esté más cercano a CE y lo inserta al conjunto más cercano a CE.
	public Punto Add_Mince(){
		if(LSP.size() == 0 || !isLoad) return null;
		
		this.updateCE();
		double min_dis = Double.MAX_VALUE;
		int index = 0;
		for(int i = 0; i < LSP.size(); i++){
			double dis_medida = 0;
			try {
				dis_medida = LSP.get(i).distancia(CE);
			}
			catch (Exception e) {
				e.printStackTrace(System.out);
				System.exit(0);
			}
			if(dis_medida < min_dis){
				min_dis = dis_medida;
				index = i;
			}
		}
		Punto p = LSP.remove(index);
		
		if(LCP.size() == 0){
			Conjunto c = new Conjunto();
			c.addPunto(p);
			LCP.add(c);
		}
		else{
			Conjunto con_min = null;
			min_dis = Double.MAX_VALUE;
			for(Conjunto c : LCP){
				if(c.getConjunto().size() > 0){
					double dis_medida = 0;
					try {
						dis_medida = this.d(CE, c);
					}
					catch (Exception e) {
						e.printStackTrace(System.out);
						System.exit(0);
					}
					if(dis_medida < min_dis){
						min_dis = dis_medida;
						con_min = c;
					}
				}
			}
			if(con_min != null) con_min.addPunto(p);
			else return null;
		}
		
		return p;
	}
	
	
	//ADD_MAXCE: Toma el punto de LSP que esté más lejano a CE y lo inserta al conjunto más lejano de CE
	public Punto Add_Maxce(){
		if(LSP.size() == 0 || !isLoad) return null;
		
		this.updateCE();
		double max_dis = 0;
		int index = 0;
		for(int i = 0; i < LSP.size(); i++){
			double dis_medida = 0;
			try {
				dis_medida = LSP.get(i).distancia(CE);
			}
			catch (Exception e) {
				e.printStackTrace(System.out);
				System.exit(0);
			}
			
			if(dis_medida > max_dis){
				max_dis = dis_medida;
				index = i;
			}
		}
		Punto p = LSP.remove(index);
		
		if(LCP.size() == 0){
			Conjunto c = new Conjunto();
			c.addPunto(p);
			LCP.add(c);
		}
		else{
			Conjunto con_min = null;
			max_dis = 0;
			for(Conjunto c : LCP){
				if(c.getConjunto().size() > 0){
					double dis_medida = 0;
					try {
						dis_medida = this.d(CE, c);
					}
					catch (Exception e) {
						e.printStackTrace(System.out);
						System.exit(0);
					}
					
					if(dis_medida > max_dis){
						max_dis = dis_medida;
						con_min = c;
					}
				}
			}
			if(con_min != null) con_min.addPunto(p);
			else return null;
		}
		
		return p;
	}
	
	//Crea un conjunto con un punto de LSP. Si hay más de X conjuntos vacios se podria evitar esta operación.
	public void Create_Cp(){
		if(!isLoad) return;
		
		if(LCP.size() <= 30){
			Conjunto c = new Conjunto();
			if(LSP.size() > 0) c.addPunto(LSP.remove(0));
			else return;
			LCP.add(c);
		}
	}
	
	
	//JOIN_CP: Une conjuntos cercanos. Sus centros LCC tienen distancia minima.
	public void Join_Cp(){
		updateLCC();
		if(LCC.size() < 2) return;
		
		double dis = Double.MAX_VALUE;
		int p1 = -1;
		int p2 = -1;
		for (int i = 0; i < LCC.size()-1; i++) {
			for(int u = i+1; u < LCC.size(); u++){
				double dis_medida = 0;
				try {
					dis_medida = LCC.get(i).distancia(LCC.get(u));
				}
				catch (Exception e) {
					e.printStackTrace(System.out);
					System.exit(0);
				}
				
				if(dis_medida < dis){
					dis = dis_medida;
					p1 = i;
					p2 = u;
				}
			}
		}
		
		if(dis != Double.MAX_VALUE){
			try {
				joinCP(p1,p2);
			}
			catch (Exception e) {
				return;
			}
		}
	}
	
	//MOVE_MIN: Se eligen dos conjunto (A, B), donde su LCC es mínima, se elige un punto de A tal que la distancia del punto al CC de B sea mínima. El punto se mueve al otro conjunto (B). Si el conjunto A queda vacío, se elimina.
	public void Move_Min(){
		updateLCC();
		if(LCC.size() < 2) return;
		
		double dis = Double.MAX_VALUE;
		int p1 = -1;
		int p2 = -1;
		for (int i = 0; i < LCC.size()-1; i++) {
			for(int u = i+1; u < LCC.size(); u++){
				double dis_medida = 0;
				try {
					dis_medida = LCC.get(i).distancia(LCC.get(u));
				}
				catch (Exception e) {
					e.printStackTrace(System.out);
					System.exit(0);
				}
				
				if(dis_medida < dis){
					dis = dis_medida;
					p1 = i;
					p2 = u;
				}
			}
		}
		
		//SE ELIGE EL PUNTO DE p1 QUE ESTA MAS CERCANO AL CC DE p2 Y SE MUEVE A p2 
		if(dis != Double.MAX_VALUE){
			dis = Double.MAX_VALUE;
			Punto p_move = null;
			
			for(Punto p : LCP.get(p1).getConjunto()){
				double dis_medida = 0;
				try {
					dis_medida = p.distancia(LCC.get(p2));
				}
				catch (Exception e) {
					e.printStackTrace(System.out);
					System.exit(0);
				}
				
				if(dis_medida < dis){
					dis = dis_medida;
					p_move = p;
				}
			}
			
			moveDot(p_move, LCP.get(p1), LCP.get(p2));
		}
	}
	
	
	//Si usamos 1 - s_avg(xi) se puede considerar como error ya que cuando e = 0 entonces s = 1, lo cual hace un agrupamiento perfecto.
	public double fitness(){
		double fitness = 1;
		try {
			//CALCULA INDICE SILUETA
			double error = (1 - this.s_avg())/2;
			//SI NO LOS AGRUPA TODOS
			double no_agrupados = 1;
			if(LSP_ORI.size() != 0){
				no_agrupados = LSP.size()/LSP_ORI.size();
			}
			//SI HAY UN SOLO CONJUNTO ENTONCES EL ERROR ES 5
			if(LCP.size() != 1) fitness = error*alfa + no_agrupados*beta;
			else fitness = 5;
		}
		catch (Exception e) {
			Contenedor.getInstance().cantidaderrores++;
			return 10;
			
			// e.printStackTrace(System.out);
			// System.exit(0);
		}
		if(mejorfitness > fitness){
			mejorfitness = fitness;
			mejorLCP.clear();
			mejorLCP = new ArrayList<Conjunto>(LCP);
		}
		return fitness;
	}
	
	//PROMEDIO DE SILUETA EN TODA LA INSTANCIA
	public double s_avg() throws Exception{
		if(!this.isLoad) throw new Exception("La instancia no tiene puntos cargados");
		if(LCP.size() == 0) throw new Exception("No hay grupos procesados");
		//TODO: ES NECESARIO QUE LSP == 0 PARA EVALUAR EL ERROR?
		
		double suma = 0;
		int count = 0;
		for(Conjunto c : LCP){
			for(Punto p : c.getConjunto()){
				suma += this.s(p);
				count++;
			}
		}
		
		if(count == 0) throw new Exception("Los conjuntos no tienen puntos");
		return suma/count;
	}
	
	//INDICE SILUETA
	public double s(Punto xi) throws Exception{
		if(!this.isLoad) throw new Exception("La instancia no tiene puntos cargados");
		if(LCP.size() == 0) throw new Exception("No hay grupos procesados");
		
		double a = this.a(xi);
		double b = this.b(xi);
		double max_ab = Math.max(a, b);
		if(max_ab == 0) throw new Exception("Silueta: a y b no pueden ser cero al mismo tiempo");
		return (b - a)/max_ab;
	}
	
	//USADO EN INDICE SILUETA
	public double b(Punto xi) throws Exception{
		if(!this.isLoad) throw new Exception("La instancia no tiene puntos cargados");
		if(LCP.size() == 0) throw new Exception("No hay grupos procesados");
		
		int index = 0;
		double min = Double.MAX_VALUE;
		for(Conjunto c : LCP){
			if(c.getConjunto().contains(xi)){
				index++;
				continue;
			}
			double d = this.d(xi, c);
			if(min > d) min = d;
			index++;
		}
		
		return min;
	}
	
	//Calcula el prom de distancia de un punto a un conjunto. El punto no debe estar en el conjunto 
	public double d(Punto xi, Conjunto Ct) throws Exception{
		if(!this.isLoad) throw new Exception("La instancia no tiene puntos cargados");
		if(LCP.size() == 0) throw new Exception("No hay grupos procesados");
		// if(Ct.getConjunto().contains(xi)) return Double.MAX_VALUE;
		// if(Ct.getConjunto().contains(xi)) throw new Exception("El Punto elegido esta en el conjunto elegido");
		if(Ct.getConjunto().size() == 0) throw new Exception("El conjunto no tiene elementos");
		
		//SI EL CONJUNTO TIENE UN ELEMENTO ENTONCES DEVUELVE 0
		if(Ct.getConjunto().size() == 1) return 0;
		
		double suma = 0;
		for(Punto p : Ct.getConjunto()){
			suma += xi.distancia(p);
		}
		double resultado = suma / Ct.getConjunto().size();
		return resultado;
	}
	
	public double a(Punto xi) throws Exception{
		if(!this.isLoad) throw new Exception("La instancia no tiene puntos cargados");
		if(LCP.size() == 0) throw new Exception("No hay grupos procesados");
		
		//SE BUSCA EL CONJUNTO AL CUAL PERTENECE EL PUNTO Xi
		int index_conjunto = 0;
		boolean encontrado = false;
		for(Conjunto c : LCP){
			for(Punto p : c.getConjunto()){
				if(p.equals(xi)){
					encontrado = true;
					break;
				}
			}
			if(encontrado) break;
			index_conjunto++;
		}
		
		//SI NO SE ENCUENTRA ERROR
		if(!encontrado) throw new Exception("No se encontro el punto para la función a(Xi)");
		
		Conjunto c = LCP.get(index_conjunto);
		
		//SI EL CONJUNTO TIENE UN ELEMENTO ENTONCES DEVUELVE 0
		if(c.getConjunto().size() == 1) return 0;
		//SI NO HAY ELEMENTOS => ERROR
		if(c.getConjunto().size() == 0) throw new Exception("El conjunto no tiene elementos");
		
		
		double suma = 0;
		for(Punto p : c.getConjunto()){
			if( !xi.equals(p) ) suma += xi.distancia(p);
		}
		double resultado = suma / (c.getConjunto().size() - 1);
		return resultado;
	}
	
	public Instancia(){
		isLoad = false;
	}
	
	public Instancia(String path, String filename, double alfa, double beta) throws Exception{
		this.filename = filename;
		this.path = path;
		this.alfa = alfa;
		this.beta = beta;
		if(this.alfa + this.beta != 1) throw new Exception("ALFA o BETA estan mal");
		isLoad = false;
	}
	
	public boolean load() throws Exception{
		if(!isLoad){
			if(filename != null && path != null){
				Scanner scanner = null;
				try{
					scanner =  new Scanner(new FileInputStream(path+filename), "UTF8");
				}
				catch(IOException e){
					e.printStackTrace(System.out);
					System.exit(0);
				}
				boolean firstline = true;
				int cant = 0;
				int fila = 1;
				while (scanner.hasNextLine()){
					String line = scanner.nextLine();
					if(firstline){
						firstline = false;
						String[] tokens = line.split(",");
						if(tokens.length != 3) throw new Exception("La instancia " + filename + " no tiene bien definida la primera linea");
						dimension = Integer.parseInt(tokens[0]);
						if(!tokens[1].equals("SI") && !tokens[1].equals("NO")) throw new Exception("La instancia " + filename + " no tiene bien definida la primera linea");
						hasSolution = tokens[1].equals("SI");
						cant = (hasSolution)?dimension+1:dimension;
						//FALTA EL NUM DE FILAS
					}
					else{
						String[] tokens = line.split(",");
						if(tokens.length != cant) throw new Exception("La instancia " + filename + " tiene problemas en la fila " + fila + ". Dimension");
						Punto p;
						if(hasSolution)	p = new Punto(dimension, tokens[tokens.length - 1], true);
						else p = new Punto(dimension);
						
						for (int i = 0; i < dimension; i++) {
							p.componente[i] = Double.parseDouble(tokens[i]);
						}
						LSP.add(p);
						LSP_ORI.add(p);
					}
					
					
					fila++;
		    	}
				
				isLoad = true;
				return isLoad;
			}
			else throw new Exception("Filename or Path is not defined");
		}
		else return isLoad;
	}
	
	public void recargar(){
		if(isLoad){
			LCP.clear();
			LSP.clear();
			LSP = new ArrayList<Punto>(LSP_ORI);
		}
	}
	
	public void updateLCC(){
		if(LCC.size() > 0) LCC.clear();
		for(Conjunto c : LCP){
			int count = 0;
			Punto suma = null;
			
			try {
				suma = (new Punto(dimension)).zeros();
			}
			catch (Exception e) {
				return;
			}
			
			for(Punto p : c.getConjunto()){
				try {
					suma = suma.suma(p);
				}
				catch (Exception e) {
					e.printStackTrace(System.out);
					System.exit(0);
				}
				
				count++;
			}
			if(count != 0){
				try {
					suma = suma.dividir(count);
				}
				catch (Exception e) {
					e.printStackTrace(System.out);
					System.exit(0);
				}
			}
			LCC.add(suma);
		}
	}
	
	public Punto updateCE(){
		if(isLoad){
			this.updateLCC();
			Punto suma = null;
			
			try {
				suma = (new Punto(dimension)).zeros();
			}
			catch (Exception e) {
				return null;
			}
			
			int count = 0;
			for(Punto p : LCC){
				try {
					suma = suma.suma(p);
				}
				catch (Exception e) {
					e.printStackTrace(System.out);
					System.exit(0);
				}
				
				count++;
			}
			if(count != 0){
				try {
					suma = suma.dividir(count);
				}
				catch (Exception e) {
					e.printStackTrace(System.out);
					System.exit(0);
				}
			}
			
			this.CE = suma;
			
			return this.CE;
		}
		else return null;
	}
	
	//UNE CONJUNTOS
	private void joinCP(int p1, int p2) throws Exception{
		if(p1 < 0 || p1 > LCP.size()-1) new Exception("p1 esta fuera de rango");
		if(p2 < 0 || p2 > LCP.size()-1) new Exception("p2 esta fuera de rango");
		if(p1 == p2) new Exception("p1 y p2 son iguales");
		
		for(Punto p : LCP.get(p2).getConjunto()){
			if(!LCP.get(p1).getConjunto().contains(p)) LCP.get(p1).addPunto(p);
		}
		
		LCP.remove(p2);
	}
	
	//MUEVE UN PUNTO DESDE UN CONJUNTO A OTRO. SI EL CONJUNTO QUEDA VACIO SE ELIMINA
	private void moveDot(Punto punto, Conjunto c_desde, Conjunto c_hasta){
		c_hasta.addPunto(punto);
		c_desde.getConjunto().remove(punto);
		if(c_desde.getConjunto().size() == 0) LCP.remove(c_desde);
	}
	
	public void setFilename(String filename){
		this.filename = filename;
	}
	
	public String getFilename(){
		return this.filename;
	}
	
	public void setPath(String path){
		this.path = path;
	}
	
	public String getPath(){
		return this.path;
	}
	
	public ArrayList<Punto> getLSP(){
		return this.LSP;
	}
	
	public ArrayList<Conjunto> getLCP(){
		return this.LCP;
	}
}