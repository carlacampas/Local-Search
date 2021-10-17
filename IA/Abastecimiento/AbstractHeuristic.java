package Abastecimiento;

import java.util.ArrayList;

import IA.Gasolina.Gasolinera;
import aima.search.framework.HeuristicFunction;

public abstract class AbstractHeuristic implements HeuristicFunction {
    
	final static int COSTE_KILOMETRO = 2;
    final static int VALOR_DEPOSITO = 1000;
    
    private ArrayList<ArrayList<Integer>> peticionesDesatendidas = new ArrayList<ArrayList<Integer>>();
    
    public abstract double getHeuristicValue (Object state);
    
    protected double computeProfits(AbastecimientoState estado) {
        // Hard copy del array de las gasolineras con todas sus peticiones para monitorizar
    	// las peticiones desatendidas
    	for (Gasolinera g : estado.gasolineras) {
    		ArrayList<Integer> gasolinera = new ArrayList<Integer>();
    		for (Integer peticion : g.getPeticiones())
    			gasolinera.add(Integer.valueOf(peticion));
    		peticionesDesatendidas.add(gasolinera);
    	}

        // Recorrido por las asignaciones para calcular la suma del precio
        // de las peticiones atendidas, y para borrar las peticiones atendidas
    	// de la lista peticionesDesatendidas.
    	double precioEnDepositos = 0.0;
        for (ArrayList<Peticion> listaPeticiones : estado.getAsignaciones()) {
            for (int i = 0; i < listaPeticiones.size(); i++) {

                Peticion peticion = listaPeticiones.get(i);
                Integer gasolineraId = peticion.get().a;
                Integer peticionId = peticion.get().b;
                
                Gasolinera gasolinera = estado.gasolineras.get(gasolineraId);
                
                // Marcar peticion peticionId de la gasolinera gasolineraId como atendida
                borrarPeticionDeGasolinera(gasolineraId, peticionId);

                // Añadir precio de una peticion al precio total:
                int diasPendientes = gasolinera.getPeticiones().get(peticionId);
                if (diasPendientes == 0) precioEnDepositos += 1.02;
                else precioEnDepositos += (100 - Math.pow(2, diasPendientes)) / 100;
            }
        }
        // Calculo de la suma de las distancias recorridas por todos los camiones
        double kilometros = 0;
        ArrayList<Integer> distancias = estado.getDistancias();
        for (Integer distancia : distancias) {
        	kilometros += AbastecimientoState.maxDist - distancia;
        }
        
        return VALOR_DEPOSITO * precioEnDepositos - COSTE_KILOMETRO * kilometros;
    }
    
    protected double computePenalisations(AbastecimientoState estado) {
		return 0;
    }
    
    // Función que dada unas Gasolineras, marca la petición con índice peticionId de la gasolinera con índice gasolineraId con un -1
    private void borrarPeticionDeGasolinera(int gasolineraId, int peticionId) {
		ArrayList<Integer> peticiones = peticionesDesatendidas.get(gasolineraId);
		peticiones.set(peticionId, -1);
		peticionesDesatendidas.set(gasolineraId, peticiones);
    }
}
