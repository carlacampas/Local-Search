package Abastecimiento;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Distribucion;
import IA.Gasolina.Gasolinera;
import IA.Gasolina.Gasolineras;

public class IntercambioOrdenTests {

    @Test
    @DisplayName("Intercambio Orden con 2 peticiones")
    void IntercambioOrden2Peticiones() {
    	
    	Pair<AbastecimientoState,Integer> st = crearEstado();
    	AbastecimientoState estado = st.a;
        
        // Crear nuevas asignaciones (solamente queremos 2)
    	ArrayList<Peticion> peticions0 = new ArrayList<Peticion>();
    	peticions0.add(new Peticion(new Pair<Integer, Integer>(0, 3)));
    	peticions0.add(new Peticion(new Pair<Integer, Integer>(1, 5)));
    	
    	ArrayList<ArrayList<Peticion>> peticions = new ArrayList<ArrayList<Peticion>>();
    	peticions.add(peticions0);
    	
    	estado.setAssigments(peticions);
    	
    	Distribucion d0 = estado.centrosDistribucion.get(0);
    	Gasolinera g0 = estado.gasolineras.get(0);
    	Gasolinera g1 = estado.gasolineras.get(1);
    	
        // Distancia recorrida por el camion 0
        Integer distancia0 =
        		calcularDistancias(d0.getCoordX(), d0.getCoordY(), g0.getCoordX(), g0.getCoordY()) +
        		calcularDistancias(g0.getCoordX(), g0.getCoordY(), g1.getCoordX(), g1.getCoordY()) +
        		calcularDistancias(g1.getCoordX(), g1.getCoordY(), d0.getCoordX(), d0.getCoordY());
        
    	ArrayList<Integer> distancias = new ArrayList<Integer>(Arrays.asList(AbastecimientoState.maxDist - distancia0));
        estado.setDistances(new ArrayList<Integer>(distancias));
    	estado.setDistTraveled(distancia0);
        
        // Intercambiar peticion 0 y peticion 1 del camion 0
        boolean done = estado.intercambioOrden(0, 1, 0);
        
        assertEquals (false, done, "Change is done successfully");
        
    	assertEquals (1, estado.getAsignaciones().get(0).get(0).get().a, "First petition should have id 1");
    	assertEquals (5, estado.getAsignaciones().get(0).get(0).get().b, "First petition should have 5 days");
    	
    	assertEquals (0, estado.getAsignaciones().get(0).get(1).get().a, "second petition should have id 0");
    	assertEquals (3, estado.getAsignaciones().get(0).get(1).get().b, "Second petition should have 3 days");
    	
    	assertEquals (distancia0, estado.getDistTraveled(), "Distance traveled should be the same");
    	assertEquals (AbastecimientoState.maxDist - distancia0, estado.getDistancias().get(0), "Distance traveled should be the same");
    }
    
    
    @Test
    @DisplayName("Intercambio Orden con 3 peticiones (cambiar primera y segunda)")
    void IntercambioOrden1y2() {
    	
    	Pair<AbastecimientoState,Integer> st = crearEstado();
    	AbastecimientoState estado = st.a;
    	Integer distancia0 = st.b;
    	
        // Intercambiar peticion 0 y peticion 1 del camion 0
        boolean done = estado.intercambioOrden(0, 1, 0);
        
        assertEquals (false, done, "Change is done successfully");
        
    	assertEquals (1, estado.getAsignaciones().get(0).get(0).get().a, "First petition should have id 1");
    	assertEquals (5, estado.getAsignaciones().get(0).get(0).get().b, "First petition should have 5 days");
    	
    	assertEquals (0, estado.getAsignaciones().get(0).get(1).get().a, "second petition should have id 0");
    	assertEquals (3, estado.getAsignaciones().get(0).get(1).get().b, "Second petition should have 3 days");
    	
    	assertEquals (distancia0, estado.getDistTraveled(), "Distance traveled should be the same");
    	assertEquals (AbastecimientoState.maxDist - distancia0, estado.getDistancias().get(0), "Distance traveled should be the same");
    }
    
    
    @Test
    @DisplayName("Intercambio Orden con 3 peticiones (cambiar primera y tercera)")
    void IntercambioOrden1y3() {
    	
    	Pair<AbastecimientoState,Integer> st = crearEstado();
    	AbastecimientoState estado = st.a;
    	
        boolean done = estado.intercambioOrden(0, 2, 0);
        
        assertEquals (false, done, "Change is done successfully");
        
    	assertEquals (2, estado.getAsignaciones().get(0).get(0).get().a, "First petition should have id 2");
    	assertEquals (7, estado.getAsignaciones().get(0).get(0).get().b, "First petition should have 7 days");
    	
    	assertEquals (0, estado.getAsignaciones().get(0).get(2).get().a, "Third petition should have id 0");
    	assertEquals (3, estado.getAsignaciones().get(0).get(2).get().b, "Third petition should have 3 days");
    	
    	Integer distancia0 = 200;
    	
    	assertEquals (distancia0, estado.getDistTraveled(), "Distance traveled should be the same");
    	assertEquals (AbastecimientoState.maxDist - distancia0, estado.getDistancias().get(0), "Distance available should be the same");
    }
    
    
    @Test
    @DisplayName("Intercambio Orden con 3 peticiones (cambiar segunda y tercera)")
    void IntercambioOrden2y3() {
    	Pair<AbastecimientoState,Integer> st = crearEstado();
    	AbastecimientoState estado = st.a;
    	
        boolean done = estado.intercambioOrden(1, 2, 0);
        
        assertEquals (false, done, "Change is done successfully");
        
    	assertEquals (2, estado.getAsignaciones().get(0).get(1).get().a, "Second petition should have id 2");
    	assertEquals (7, estado.getAsignaciones().get(0).get(1).get().b, "Second petition should have 7 days");
    	
    	assertEquals (1, estado.getAsignaciones().get(0).get(2).get().a, "Third petition should have id 1");
    	assertEquals (5, estado.getAsignaciones().get(0).get(2).get().b, "Third petition should have 5 days");
    	
    	Integer distancia0 = 200;
    	
    	assertEquals (distancia0, estado.getDistTraveled(), "Distance traveled should be the same");
    	assertEquals (AbastecimientoState.maxDist - distancia0, estado.getDistancias().get(0), "Distance available should be the same");
    }
    
    @Test
    @DisplayName("Intercambio Orden con 3 peticiones (cambiar segunda y tercera) sobrepasando el máximo de distancia")
    void IntercambioOrdenSobrepasaDisancia() {
    	Pair<AbastecimientoState,Integer> st = crearEstado();
    	
    	AbastecimientoState estado = st.a;
    	estado.gasolineras.get(0).setCoordX(800);
    	
    	Integer distancia0 = 1700;
    	
    	ArrayList<Integer> distancias = new ArrayList<Integer>(Arrays.asList(AbastecimientoState.maxDist - distancia0));
        estado.setDistances(new ArrayList<Integer>(distancias));
    	estado.setDistTraveled(distancia0);
    	
        boolean done = estado.intercambioOrden(1, 2, 0);
        
        assertEquals (true, done, "Change wasn't done because it exceeded the maximum distance");
    }
        
    
    int calcularDistancias(int fromX, int fromY, int toX, int toY) {
        return Math.abs (fromX - toX) + Math.abs (fromY - toY);
    }
    
    Pair<AbastecimientoState, Integer> crearEstado() {
		CentrosDistribucion centrosDistribucion = new CentrosDistribucion(1, 1, 1);
        Gasolineras gasolineras = new Gasolineras(3, 1);
        
        centrosDistribucion.get(0).setCoordX(0);
        centrosDistribucion.get(0).setCoordY(0);
        
        gasolineras.get(0).setCoordX(-50);
        gasolineras.get(0).setCoordY(0);
        gasolineras.get(0).setPeticiones(new ArrayList<Integer>(Arrays.asList(0)));
        
        gasolineras.get(1).setCoordX(-25);
        gasolineras.get(1).setCoordY(0);
        gasolineras.get(1).setPeticiones(new ArrayList<Integer>(Arrays.asList(0)));
        
        gasolineras.get(2).setCoordX(25);
        gasolineras.get(2).setCoordY(0);
        gasolineras.get(2).setPeticiones(new ArrayList<Integer>(Arrays.asList(0)));
        
        // Crear asignaciones para el camión
    	ArrayList<Peticion> peticions0 = new ArrayList<Peticion>();
    	peticions0.add(new Peticion(new Pair<Integer, Integer>(0, 3)));
    	peticions0.add(new Peticion(new Pair<Integer, Integer>(1, 5)));
    	peticions0.add(new Peticion(new Pair<Integer, Integer>(2, 7)));
    	
    	ArrayList<ArrayList<Peticion>> peticions = new ArrayList<ArrayList<Peticion>>();
    	peticions.add(peticions0);
    	
    	AbastecimientoState estado = new AbastecimientoState(gasolineras, centrosDistribucion);
    	estado.setAssigments(peticions);
    	
    	Distribucion d0 = centrosDistribucion.get(0);
    	
    	Gasolinera g0 = gasolineras.get(0);
        Gasolinera g1 = gasolineras.get(1);
        Gasolinera g2 = gasolineras.get(2);
        
        // Distancia recorrida por el camion 0
        Integer distancia0 =
        		calcularDistancias(d0.getCoordX(), d0.getCoordY(), g0.getCoordX(), g0.getCoordY()) +
        		calcularDistancias(g0.getCoordX(), g0.getCoordY(), g1.getCoordX(), g1.getCoordY()) +
        		calcularDistancias(g1.getCoordX(), g1.getCoordY(), d0.getCoordX(), d0.getCoordY()) +
				calcularDistancias(d0.getCoordX(), d0.getCoordY(), g2.getCoordX(), g2.getCoordY()) +
				calcularDistancias(g2.getCoordX(), g2.getCoordY(), d0.getCoordX(), d0.getCoordY());
        
    	ArrayList<Integer> distancias = new ArrayList<Integer>(Arrays.asList(AbastecimientoState.maxDist - distancia0));
        estado.setDistances(new ArrayList<Integer>(distancias));
    	estado.setDistTraveled(distancia0);
    	
    	return new Pair<AbastecimientoState, Integer>(estado, distancia0);
	}
}