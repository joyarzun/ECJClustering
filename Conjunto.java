import java.util.ArrayList;

public class Conjunto
{
	private ArrayList<Punto> C;
	
	public Conjunto(){
		C = new ArrayList<Punto>();
	}
	
	public ArrayList<Punto> getConjunto(){
		return C;
	}
	
	public boolean addPunto(Punto p){
		return C.add(p);
	}
	
	public Punto getPunto(int pos){
		return C.get(pos);
	}
	
	public int getSize(){
		return C.size();
	}
	
	public Punto removePunto(int pos){
		return C.remove(pos);
	}
}