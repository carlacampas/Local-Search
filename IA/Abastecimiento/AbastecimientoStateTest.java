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
    @DisplayName("Processed distance test")
    public void testCalcularDistnacias() {        
        centrosDistibucion.add(new Distribucion(0, 0));
        ArrayList<Integer> peticiones = new ArrayList <Integer> (1);
        
        // first test: one way trip, empty array
        gasolineras.add(new Gasolinera(60, 60, peticiones));
        // second test: two way trip, one petition inside array
        gasolineras.add(new Gasolinera(74, 88, peticiones));
        // third test: one way trip where 2+ are already in the array
        gasolineras.add(new Gasolinera(50, 50, peticiones));
        // fourth test: km negatius
        gasolineras.add(new Gasolinera(100, 100, peticiones));
        
        AbastecimientoState as = new AbastecimientoState (gasolineras, centrosDistibucion);
        
        int[] solu = {400, 316, 116, -84}; 
        
        for (int i=0; i<solu.length; i++) {
	        assertEquals (solu[i], as.calcularDistancias(0, new Pair <Integer, Integer> (i, 0)), "Processed distances should be equal");
	        as.assignaPeticion(0, new Pair <Integer, Integer> (i, 0));
        }
    }
    
    //actualizaDistancia
    //assignaPeticion
    //intercambiaPeticiones
    //intercambioOrden
    //cambiaPeticion
}
