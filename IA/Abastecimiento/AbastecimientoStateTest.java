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
    @Test
    @DisplayName("Simple distance test")
    void testCalcularDistancia () {
    	Gasolineras gasolineras = null;
        CentrosDistribucion centrosDistibucion = null;
        
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
}
