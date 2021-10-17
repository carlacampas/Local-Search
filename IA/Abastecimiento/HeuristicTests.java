package Abastecimiento;

import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Distribucion;
import IA.Gasolina.Gasolineras;
import IA.Gasolina.Gasolinera;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeuristicTests {
    @Test
    @DisplayName("Test computeProfits")
    void computeProfitsTest() {

    	// Crear centros de distribucion y gasolineras en coordenadas aleatorias
        CentrosDistribucion centrosDistribucion = new CentrosDistribucion(2, 1, 1);
        Gasolineras gasolineras = new Gasolineras(3, 1);
        
        // Crear peticiones de las gasolineras
        gasolineras.get(0).setPeticiones(new ArrayList<Integer>(Arrays.asList(2, 0)));	
        gasolineras.get(1).setPeticiones(new ArrayList<Integer>(Arrays.asList(1, 2))); // la de 2 -> desatendida
        gasolineras.get(2).setPeticiones(new ArrayList<Integer>(Arrays.asList(3, 4))); // la de 4 -> desatendida
    	
        // Crear asignaciones para los camiones
    	ArrayList<Peticion> peticions0 = new ArrayList<Peticion>();
    	peticions0.add(new Peticion(new Pair<Integer, Integer>(0, 0)));
    	peticions0.add(new Peticion(new Pair<Integer, Integer>(1, 0)));
    	
    	ArrayList<Peticion> peticions1 = new ArrayList<Peticion>();
    	peticions1.add(new Peticion(new Pair<Integer, Integer>(2, 0)));
    	peticions1.add(new Peticion(new Pair<Integer, Integer>(0, 1)));
    	
    	ArrayList<ArrayList<Peticion>> peticions = new ArrayList<ArrayList<Peticion>>();
    	peticions.add(peticions0);
    	peticions.add(peticions1);
    	
    	// Crear estado
    	AbastecimientoState estado = new AbastecimientoState(gasolineras, centrosDistribucion);
    	estado.setAssigments(peticions);
    	
    	Distribucion d0 = centrosDistribucion.get(0);
    	Distribucion d1 = centrosDistribucion.get(0);
    	
    	Gasolinera g0 = gasolineras.get(0);
        Gasolinera g1 = gasolineras.get(1);
        Gasolinera g2 = gasolineras.get(2);
        
        // Distancia recorrida por el camion 0
        Integer distancia0 =
        		calcularDistancias(d0.getCoordX(), d0.getCoordY(), g0.getCoordX(), g0.getCoordY()) +
        		calcularDistancias(g0.getCoordX(), g0.getCoordY(), g1.getCoordX(), g1.getCoordY()) +
        		calcularDistancias(g1.getCoordX(), g1.getCoordY(), d0.getCoordX(), d0.getCoordY());
        
        // Distancia recorrida por el camion 1
        Integer distancia1 =
        		calcularDistancias(d1.getCoordX(), d1.getCoordY(), g2.getCoordX(), g2.getCoordY()) +
        		calcularDistancias(g2.getCoordX(), g2.getCoordY(), g0.getCoordX(), g0.getCoordY()) +
        		calcularDistancias(g0.getCoordX(), g0.getCoordY(), d1.getCoordX(), d1.getCoordY());
        
    	// Calcular array de distancias (distancias[i] = MAX_DIST - distancia total recorrida por el camion i)
    	ArrayList<Integer> distancias = new ArrayList<Integer>(Arrays.asList(
    		distancia0, distancia1
    	));
    	estado.setDistances(distancias);
    	
    	double kilometros = 0;
    	for(Integer distancia : distancias) {
    		kilometros += AbastecimientoState.maxDist - distancia;
    	}
    	
    	double ingresos = AbastecimientoHeuristicFunction1.VALOR_DEPOSITO * (1.02 + 0.98 + 0.96 + 0.92);
    	double costes = AbastecimientoHeuristicFunction1.COSTE_KILOMETRO * kilometros;
    	
    	double result = ingresos - costes;
    	
    	AbastecimientoHeuristicFunction1 heuristic = new AbastecimientoHeuristicFunction1();
    	
    	assertEquals (result, heuristic.computeProfits(estado), "Heuristic value should be equal");
    }
    
    @Test
    @DisplayName("Test computePenalisations")
    void computePenalisationsTest() {
    	ArrayList<ArrayList<Integer>> gs = new ArrayList<ArrayList<Integer>>(Arrays.asList(
    		new ArrayList<Integer>(Arrays.asList(1, 3, -1, 0)),
    		new ArrayList<Integer>(Arrays.asList(-1, 4, 0, 2))
    	));
    	
    	double result = 300;
    	
    	AbastecimientoHeuristicFunction1 heuristic = new AbastecimientoHeuristicFunction1();
    	heuristic.peticionesDesatendidas = gs;
    	
    	assertEquals (result, heuristic.computePenalisations(2), "Penalisation value should be equal");
    }
    
    int calcularDistancias(int fromX, int fromY, int toX, int toY) {
        return Math.abs (fromX - toX) + Math.abs (fromY - toY);
    }
}