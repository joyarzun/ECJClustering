import ec.util.*;
import ec.*;
import ec.gp.*;

public class MyGPData extends GPData
{
	public Punto punto;
	public boolean condicion;
	
	public void copyTo(final GPData gpd)   // copy my stuff to another DoubleData
    {
		((MyGPData)gpd).punto = punto;
		((MyGPData)gpd).condicion = condicion;
    }
}