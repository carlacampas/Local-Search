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
    	centrosDistibucion.add(new Distribucion(6, 100));
    	
    	AbastecimientoState as = new AbastecimientoState (gasolineras, centrosDistibucion);
    	
    	gasolineras.add(new Gasolinera(80, 60, new ArrayList<Integer>(1)));	//oldPCase1
    	gasolineras.add(new Gasolinera(40, 80, new ArrayList<Integer>(1)));	//newP
    	gasolineras.add(new Gasolinera(100, 40, new ArrayList<Integer>(1)));//oldPCase2
    	gasolineras.add(new Gasolinera(40, 50, new ArrayList<Integer>(1)));	//oldPCase3
    	gasolineras.add(new Gasolinera(10, 20, new ArrayList<Integer>(1)));	//extra
    		
    	Pair<Integer, Integer> oldPCase1 = new Pair <Integer, Integer> (0,0);
    	Pair<Integer, Integer> oldPCase2 = new Pair <Integer, Integer> (2,0);
    	Pair<Integer, Integer> oldPCase3 = new Pair <Integer, Integer> (3,0);
    	Pair<Integer, Integer> extra = new Pair <Integer, Integer> (4,0);
    	Pair<Integer, Integer> newP = new Pair <Integer, Integer> (1,0);

    	//caso 1: una sola petición asignada (cae por tanto en posición par, index 0)
    	as.asignaPeticion(0, oldPCase1);
    	    	
    	//640 - 2*(74+40) + 2*(74+40) - 2*54 
    	//640 --> 532
    	assertEquals (532, as.actualizaDistancia(oldPCase1, newP, 0), "Incorrect distance update (case 1)");
    	
    	
    	//caso 2: más de una petición y posición de oldP es par
    	as.asignaPeticion(0, oldPCase3);		  //acts like an extra here
    	as.asignaPeticion(0, oldPCase2);		  //index 2 ^^^
    	as.asignaPeticion(0, extra);
    	
    	//640 - (114+50+84 + 154+110+84)+(154+110+84)-(54+90+84) 
    	//44 --> 164
    	assertEquals (164, as.actualizaDistancia(oldPCase2, newP, 0), "Incorrect distance update (case 2)");
    	
    	//caso 3: indice de la petición impar 
    	//no nos importa si está por el medio o es la ultima, la posición impar siempre completa un viaje
    	
    	//640 - (114+50+84 + 154+110+84)+(114+50+84)-(114+60+54)
    	//44 --> 64
    	assertEquals (64, as.actualizaDistancia(oldPCase3, newP, 0), "Incorrect distance update (case 3)");
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
    	centrosDistibucion.add(new Distribucion(0, 0));
    	centrosDistibucion.add(new Distribucion(5, 0));

    	AbastecimientoState as = new AbastecimientoState (gasolineras, centrosDistibucion);
    	
    	gasolineras.add(new Gasolinera(60, 60, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(74, 88, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(50, 50, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(100, 100, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(75, 75, new ArrayList <Integer> (1)));
    	
    	// test funciona camiones distintos
    	//Test 1: intercambio en arraylists de size 1
    	as.asignaPeticion(0, new Pair <Integer, Integer> (0, 0));
    	as.asignaPeticion(1, new Pair <Integer, Integer> (3, 0));	

    	//*******uncomment this test and comment following one******
    	//assertEquals (true, as.intercambiaPeticiones(0, 0, 0, 1), "Test 1: Intercambio de peticiones debería efectuarse");
    	
    	//Test 2: intercambio no se efectua por exceso de km
    	as.asignaPeticion(0, new Pair <Integer, Integer> (1, 0));
    	as.asignaPeticion(0, new Pair <Integer, Integer> (2, 0));
    	as.asignaPeticion(0, new Pair <Integer, Integer> (4, 0));
	
    	//640 - ((120+42+162) + (100+50+150)) = 16
    	//640 - ((120+42+166) + (100+100+200)) = -88
    	
    	//******uncomment this test and comment previous one******
    	assertEquals(false, as.intercambiaPeticiones(3, 0, 0, 1), "Test 2: Swap should not complete due to too many km");
    	
    	// test misma peticion funciona	???
    	
    	// test mismo camion funciona
    	//Test 3: intercambio peticiones mismo camión
    	assertEquals(false, as.intercambiaPeticiones(0, 1, 0, 0), "Test 3: Operator should not handle swaps of same truck's assignments");
    	
    }
    
    @Test
    @DisplayName("Operators test - testIntercambioOrden")
    public void testIntercambioOrden () {
    	centrosDistibucion.add(new Distribucion(1, 1));
    	centrosDistibucion.add(new Distribucion(200, 0));

    	AbastecimientoState as = new AbastecimientoState (gasolineras, centrosDistibucion);
    	
    	gasolineras.add(new Gasolinera(60, 60, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(74, 88, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(50, 50, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(0, 20, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(0, 0, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(250, 0, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(250, 20, new ArrayList <Integer> (1)));
    	
    	
    	//Test 1: same petition
    	as.asignaPeticion(0, new Pair<Integer, Integer> (0,0));
    	as.asignaPeticion(0, new Pair<Integer, Integer> (1,0));
    	
    	assertEquals(true, as.intercambioOrden(0,0,0), "Issue swapping same petition. Nothing should change and returns true.");
    	assertEquals(true, as.intercambioOrden(1,1,0), "Issue swapping same petition. Nothing should change and returns true.");
    	
    	//Test 2: different petition
    	as.asignaPeticion(0, new Pair<Integer, Integer> (2,0));
    	
    	assertEquals(true, as.intercambioOrden(0,2,0), "Swap should complete.");
    	
    	//Test 3: too many km
    	//640-(200+20+220)-(50+20+70)
    	//640-(220+250+70)-(200+250+50)
    	for (int i = 3; i < 7; i++) {
    		assertEquals(true,as.asignaPeticion(1, new Pair<Integer, Integer> (i,0)), "no entra");
    	}
    	assertEquals(false, as.intercambioOrden(0,2,1), "Swap should not complete. Excessive km");

    }
    
    @Test
    @DisplayName("Operators test - testCambiaPeticion")
    public void testCambiaPeticion () {
    	centrosDistibucion.add(new Distribucion(0, 0));
    	centrosDistibucion.add(new Distribucion(5, 0));

    	AbastecimientoState as = new AbastecimientoState (gasolineras, centrosDistibucion);

    	gasolineras.add(new Gasolinera(60, 60, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(74, 88, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(50, 50, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(100, 100, new ArrayList <Integer> (1)));

    	
    	//Test 1: same truck
    	as.asignaPeticion(0, new Pair <Integer, Integer> (0,0));
    	//assertEquals(true, as.cambiaPeticion(0, 0, 0), "Should be true, since nothing changes");
    	
    	// Tes2: different trucks
    	as.asignaPeticion(0, new Pair <Integer, Integer> (1,0));
    	as.asignaPeticion(1, new Pair <Integer, Integer> (2,0));
    	as.asignaPeticion(1, new Pair <Integer, Integer> (3,0));
    	
    	    	
    	assertEquals(true, as.cambiaPeticion(0, 1, 0), "Should be true, no restrictions are met");
    	
    	assertEquals(76, as.getDistancias().get(0));
    	assertEquals(250, as.getDistancias().get(1));

    	//Test 3: Too many km
    	
    	assertEquals(false, as.cambiaPeticion(0, 1, 0), "Cannot make change since c(0) exceeds maxDist");
    	//assertEquals(1, as.getDistancias().get(0));

    }
    
    @Test
    @DisplayName("Operators test - testCambiaPeticionNoAsig")
    public void testCambiaPeticionNoAsig () {
    	centrosDistibucion.add(new Distribucion(0, 0));
    	centrosDistibucion.add(new Distribucion(10, 20));
    	
    	AbastecimientoState as = new AbastecimientoState (gasolineras, centrosDistibucion);

    	gasolineras.add(new Gasolinera(60, 60, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(74, 88, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(50, 50, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(70, 70, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(100, 100, new ArrayList <Integer> (1)));
    	
    	gasolineras.add(new Gasolinera(34, 24, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(70, 15, new ArrayList <Integer> (1)));
    	gasolineras.add(new Gasolinera(63, 50, new ArrayList <Integer> (1)));

    	
    	//Test 1: Swap successful
    	assertEquals(true, as.asignaPeticion(1, new Pair <Integer, Integer> (5,0)));
    	assertEquals(true, as.asignaPeticion(1, new Pair <Integer, Integer> (6,0)));
    	
    	assertEquals(true, as.cambioPeticionNoAsig(1, 1, new Pair <Integer, Integer>(7,0)), "Swap should execute successfully");
    	
    	// Test 2: exceeds km
    	assertEquals(true, as.asignaPeticion(0, new Pair <Integer, Integer> (0,0)));
    	assertEquals(true, as.asignaPeticion(0, new Pair <Integer, Integer> (1,0)));
    	assertEquals(true, as.asignaPeticion(0, new Pair <Integer, Integer> (2,0)));
    	assertEquals(true, as.asignaPeticion(0, new Pair <Integer, Integer> (3,0)));
    	
    	assertEquals(false, as.cambioPeticionNoAsig(3, 0, new Pair <Integer, Integer>(4,0)), "New peticion exceeds km limit");
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
        
        noErrors(as);
    }
    
    /*@Test
    @DisplayName("Initial State Test - testGenerateInitialSolution3")
    public void testGenerateInitialSolution3 () {
    	gasolineras = new Gasolineras(100, 5);
        centrosDistibucion = new CentrosDistribucion(10, 2, 5);
        
        AbastecimientoState as = new AbastecimientoState (gasolineras, centrosDistibucion);
        as.generateInitialSolution3 ();
        
        noErrors(as);
    	// test totes les gasolineres en diferentes posicions
    	// test dos camions i differentes gasolineres
    
    }*/
}
