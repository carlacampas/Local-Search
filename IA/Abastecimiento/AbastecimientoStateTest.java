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
    @DisplayName("Processed distance test - testCalcularDistancias")
    public void testCalcularDistancias() {        
        centrosDistibucion.add(new Distribucion(0, 0));
        ArrayList<Integer> peticiones = new ArrayList <Integer> (1);
        
        // first test: one way trip, empty array
        gasolineras.add(new Gasolinera(60, 60, peticiones));
        // second test: two way trip, one petition inside array
        gasolineras.add(new Gasolinera(74, 88, peticiones));
        // third test: one way trip where 2+ are already in the array
        gasolineras.add(new Gasolinera(50, 50, peticiones));
        // fourth test: negative km
        gasolineras.add(new Gasolinera(100, 100, peticiones));
        
        AbastecimientoState as = new AbastecimientoState (gasolineras, centrosDistibucion);
        
        int[] solu = {400, 316, 116, -84}; 
        
        for (int i=0; i<solu.length; i++) {
	        assertEquals (solu[i], as.calcularDistancias(0, new Pair <Integer, Integer> (i, 0)), "Processed distances should be equal");
	        as.asignaPeticion(0, new Pair <Integer, Integer> (i, 0));
        }
    }
    
    @Test
    @DisplayName("Processed distance test - testActualizaDistancia")
    public void testActualizaDistancia () {
    	
    }
    
    @Test
    @DisplayName("Operators test - testAsignaPeticion")
    public void asignaPeticion () {
    	centrosDistibucion.add(new Distribucion(0, 0));
    	centrosDistibucion.add(new Distribucion(5, 0));
    	
    	AbastecimientoState as = new AbastecimientoState (gasolineras, centrosDistibucion);
    	
    	// Test correct return value.
    	ArrayList<Integer> peticiones = new ArrayList <Integer> (1);
    	gasolineras.add(new Gasolinera(60, 60, peticiones));
    	gasolineras.add(new Gasolinera(74, 88, peticiones));
    	gasolineras.add(new Gasolinera(50, 50, peticiones));
    	gasolineras.add(new Gasolinera(100, 100, peticiones));
    	
    	// first test: true, empty petition set
    	assertEquals (true, as.asignaPeticion (0, new Pair <Integer, Integer> (0, 0)), "Empty assignation list should be true");
    	
    	// second test: true, not empty petition set
    	assertEquals (true, as.asignaPeticion (0, new Pair <Integer, Integer> (1, 0)), "No criteria broken list should be true");
    	assertEquals (true, as.asignaPeticion (0, new Pair <Integer, Integer> (2, 0)), "No criteria broken list should be true");
    	
    	// third test: false, not enough km
    	assertEquals (false, as.asignaPeticion (0, new Pair <Integer, Integer> (3, 0)), "Too many KM");
    	
    	// fourth test: false, max trips taken
    	int newGasoStart = 4;
    	for (int i=1; i<=10; i++) {
    		gasolineras.add(new Gasolinera (i, i, peticiones));
    		as.asignaPeticion (1, new Pair <Integer, Integer> (newGasoStart++, 0));
    	}
    	
    	gasolineras.add(new Gasolinera (11, 11, peticiones));
    	assertEquals (false, as.asignaPeticion (1, new Pair <Integer, Integer> (11, 0)), "Too many trips");
    	
    	// Test petitions solutions are as should be
    	ArrayList <Peticion> pet = as.getAsignaciones().get(0);
    	assertEquals (3, pet.size(), "Should have 3 assigs");
    	for (int i=0; i<3; i++) {
    		Peticion p = pet.get(i);
    		assertEquals (i, p.get().a, "Petition keys should be the same");
    	}
    	
    	pet = as.getAsignaciones().get(1);
    	assertEquals (10, pet.size(), "Should have 10 assigs");
    	for (int i=0; i<10; i++) {
    		Peticion p = pet.get(i);
    		assertEquals (i+4, p.get().a, "Petition keys should be the same");
    	}
    }
    
    @Test
    @DisplayName("Operators test - testIntercambiaPeticiones")
    public void testIntercambiaPeticiones () {
    	// test funciona camiones distintos
    	// test misma peticion funciona
    	// test mismo camion funciona
    	// test distancia demasiado grande
    }
    
    @Test
    @DisplayName("Operators test - testIntercambioOrden")
    public void testIntercambioOrden () {
    	// do we really need this? si yes mismos tests que arriba
    }
    
    @Test
    @DisplayName("Operators test - testCambiaPeticion")
    public void testCambiaPeticion () {
    	// test funciona
    	// test demasiado grande
    }
    
    @Test
    @DisplayName("Simple distance test - testCalcularDistancia")
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
    
    private void noErrors (AbastecimientoState as) {
    	for (int i=0; i<as.getAsignaciones().size(); i++) {
        	ArrayList <Peticion> assigs = as.getAsignaciones().get(i);
        	assertEquals (true, assigs.size() <= 10, "No es poden fer mes de 5 viatges");
        	assertEquals (true, as.getDistancias().get(i) > 0, "Distancia ha de ser positiva");
	        for (Peticion p : assigs) {
	        	assertEquals (true, gasolineras.size() > p.get().a, "Gasolinera ha d'existir");
	        	assertEquals (true, gasolineras.get(p.get().a).getPeticiones().size() > p.get().b, "Gasolinera ha de tindre peticio");
	        }
    	}
    }
    
    @Test
    @DisplayName("Initial State Test - testGenerateInitialSolution1")
    public void testGenerateInitialSolution1 () {
    	gasolineras = new Gasolineras(100, 5);
        centrosDistibucion = new CentrosDistribucion(10, 2, 5);
        
        AbastecimientoState as = new AbastecimientoState (gasolineras, centrosDistibucion);
        as.generateInitialSolution1 ();
        
        noErrors(as);
    }
    
    @Test
    @DisplayName("Organizar Peticiones Test - testOrganizarPeticiones")
    public void testOrganizarPeticiones () {
    	// test 0
    	// test normal	
    }
    
    @Test
    @DisplayName("Initial State Test - testGenerateInitialSolution2")
    public void testGenerateInitialSolution2 () {
    	gasolineras = new Gasolineras(100, 5);
        centrosDistibucion = new CentrosDistribucion(10, 2, 5);
        
        AbastecimientoState as = new AbastecimientoState (gasolineras, centrosDistibucion);
        as.generateInitialSolution2 ();
        
        noErrors(as);
    	// test totes les gasolineres en diferentes posicions
    	// test dos camions mateixes coords
        // test dos camions dif coords
    	
    	// comprobar que no hay errores
        
        
        for (int i=0; i<as.getAsignaciones().size(); i++) {
        	ArrayList <Peticion> assigs = as.getAsignaciones().get(i);
        	System.out.println ("(" + i + ") --> " + as.getDistancias().get(i) + ": ");
	        for (Peticion p : assigs) {
	        	System.out.print ("(" + p.get().a + "," + p.get().b + ")");
	        }
	        System.out.println();
    	}
        
        noErrors(as);
    }
    
    @Test
    @DisplayName("Initial State Test - testGenerateInitialSolution3")
    public void testGenerateInitialSolution3 () {
    	// test totes les gasolineres en diferentes posicions
    	// test dos camions i differentes gasolineres
    }
    
    // comprobar que no hay errores
}
