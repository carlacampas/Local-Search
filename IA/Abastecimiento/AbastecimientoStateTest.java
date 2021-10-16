package Abastecimiento;

import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Distribucion;
import IA.Gasolina.Gasolineras;
import IA.Gasolina.Gasolinera;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbastecimientoStateTest {
	
	private Gasolineras gasolineras;
	private CentrosDistribucion centrosDistibucion;
	@BeforeEach
	public void setUp () {
		gasolineras = new Gasolineras(0, 0);
         centrosDistibucion = new CentrosDistribucion(0, 0, 0);
	}
    @Test
    @DisplayName("Simple distance test")
    public void testCalcularDistancia () {
    	ArrayList<Integer> peticions = new ArrayList <Integer> (2);
    	gasolineras.add(new Gasolinera(0, 0, peticions));
    	gasolineras.add(new Gasolinera (5, 0, peticions));
    	
    	centrosDistibucion.add(new Distribucion (1, 1));
    	centrosDistibucion.add(new Distribucion (5, 1));
    	
    	AbastecimientoState as = new AbastecimientoState (gasolineras, centrosDistibucion);
    	
    	
    	int[] solu = {2, 6, 5, 1}; 
    	int i = 0;
    	for (Gasolinera g: gasolineras) {
    		Pair <Integer, Integer> c1 = new Pair <Integer, Integer> (g.getCoordX(), g.getCoordY());
    		for (Distribucion cd : centrosDistibucion) {
    			Pair <Integer, Integer> c2 = new Pair <Integer, Integer> (cd.getCoordX(), cd.getCoordY());
    			assertEquals (solu[i], as.calcularDistancia(c1, c2), "Distances should be equal");
    			i++;
    		}
    	}
    }
    @Test
    @DisplayName("Simple distance test")
    public void testCalcularDistnacias() {
        CentrosDistribucion centrosDistibucion = new CentrosDistribucion(0, 0, 0);
        
        centrosDistibucion.add(new Distribucion(0, 0));
        
        AbastecimientoState as = new AbastecimientoState (gasolineras, centrosDistibucion);
        
        // int c, pair
        int[] solu = {440, 6, 5}; 
        int[] codeSol = new int[3];
        // first test: one way trip, empty array
        Pair <Integer, Integer> p = new Pair <Integer, Integer> (100, 100);
        codeSol[0] = as.calcularDistancias(0, p);
        as.assignaPeticion(0, p);
        
        // second test: two way trip, one petition inside array
        p = new Pair <Integer, Integer> (50, 50);
        
        // third test: one way trip where 2+ are already in the array
        // km negatius
        for (int i=0; i<3; i++) {
        	assertEquals (solu[i], codeSol[i], "Processed distances should be equal");
        }
    }
    
    //actualizaDistancia
    //assignaPeticion
    //intercambiaPeticiones
    //intercambioOrden
    //cambiaPeticion
}
