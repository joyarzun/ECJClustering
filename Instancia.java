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
	private ArrayList<Punto> LSP = new ArrayList<Punto>();//Lista de secuencia de puntos
	private ArrayList<Punto> LSP_ORI = new ArrayList<Punto>();//Lista de secuencia de puntos
	private ArrayList<Conjunto> LCP = new ArrayList<Conjunto>();//Lista de conjuntos de puntos
	
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
	
	
	
	//Si usamos 1 - s_avg(xi) se puede considerar como error ya que cuando e = 0 entonces s = 1, lo cual hace un agrupamiento perfecto.
	public double error() throws Exception{
		return 1 - this.s_avg();
	}
	
	//PROMEDIO DE SILUETA EN TODA LA INSTANCIA
	public double s_avg() throws Exception{
		if(!this.isLoad) throw new Exception("La instancia no tiene puntos cargados");
		if(LCP.size() == 0) throw new Exception("No hay grupos procesados");
		
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
	
	public double d(Punto xi, Conjunto Ct) throws Exception{
		if(!this.isLoad) throw new Exception("La instancia no tiene puntos cargados");
		if(LCP.size() == 0) throw new Exception("No hay grupos procesados");
		if(Ct.getConjunto().contains(xi)) throw new Exception("El Punto elegido esta en el conjunto elegido");
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
	
	public Instancia(String path, String filename){
		this.filename = filename;
		this.path = path;
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