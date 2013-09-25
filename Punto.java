import java.lang.Math;
import java.util.Arrays;

public class Punto
{
	public double[] componente;
	public String grupo;
	public boolean hasSolution;
	
	public double distancia(Punto p1) throws Exception{
		Punto p2 = this;
		if(p1.componente.length == 0 || p2.componente.length == 0) throw new Exception("Esta intentando medir distancia de un punto que no tiene componentes");
		if(p1.componente.length != p2.componente.length) throw new Exception("Esta intentando medir distancia de puntos con distintas dimensiones");
		if(p1 == p2 || p1.equals(p2)) return 0;
		
		double suma = 0;
		for (int i = 0; i < p1.componente.length; i++) {
			suma += Math.pow(p2.componente[i] - p1.componente[i] , 2);
		}
		
		double resultado = Math.sqrt(suma);
		return resultado;
	}
	
	
	
	public Punto(int dimension) throws Exception{
		if(dimension > 0) this.componente = new double[dimension];
		else throw new Exception("La dimension esta incorrecta");
		this.hasSolution = false;
	}
	
	public Punto(int dimension, String grupo, boolean hasSolution) throws Exception{//PODRIA ESTAR UN PUNTO SIN COMPONENTES "CORRECTAS"
		if(dimension > 0) this.componente = new double[dimension];
		else throw new Exception("La dimension esta incorrecta");
		this.grupo = grupo;
		this.hasSolution = hasSolution;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((componente == null) ? 0 : componente.hashCode());
		result = prime * result + ((grupo == null) ? 0 : grupo.hashCode());
		result = prime * result + (hasSolution ? 1231 : 1237);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || this.getClass() != obj.getClass()) return false;
		
		Punto other = (Punto) obj;

		return 
		Arrays.equals(this.componente, other.componente) && this.hasSolution == other.hasSolution &&
		(this.grupo == other.grupo || (this.grupo != null && this.grupo.equals(other.grupo)));
	}
	
	public Punto copy(Punto p) throws Exception{
		Punto copia = new Punto(p.componente.length, p.grupo, p.hasSolution);
		for (int i = 0; i < p.componente.length; i++) {
			copia.componente[i] = p.componente[i];
		}
		
		return copia;
	}
	
	public Punto copy() throws Exception{
		Punto copia = new Punto(this.componente.length, this.grupo, this.hasSolution);
		for (int i = 0; i < this.componente.length; i++) {
			copia.componente[i] = this.componente[i];
		}
		
		return copia;
	}
}