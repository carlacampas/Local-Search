package Abastecimiento;

import java.util.ArrayList;

import IA.Gasolina.Gasolinera;
import aima.search.framework.HeuristicFunction;

public abstract class AbstractHeuristic implements HeuristicFunction {
    
	final static int COSTE_KILOMETRO = 2;
    final static int VALOR_DEPOSITO = 1000;
    
    public ArrayList<ArrayList<Integer>> peticionesDesatendidas = new ArrayList<ArrayList<Integer>>();
    
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
    	double precioEnDepositos = estado.getPrecioEnDepositos();
        // Calculo de la suma de las distancias recorridas por todos los camiones
        double kilometros = estado.getDistTraveled();
    	
        return VALOR_DEPOSITO * precioEnDepositos - COSTE_KILOMETRO * kilometros;
    }
    
    protected double computePenalisations(Integer exp, AbastecimientoState estado) {
    	double penalisation = 0;
    	// Iteración sobre las peticiones desatendidas para calcular la penalización por retraso
		for (String g : estado.getPeticionesDesatendidas()) {
			Pair <Integer, Integer> p = new Pair();
			
			int a = Integer.parseInt(p.fromStringA(g));
			int b = Integer.parseInt(p.fromStringB(g));
			
			int dias = estado.gasolineras.get(a).getPeticiones().get(b);
			penalisation += Math.pow(exp, dias) / 100;
		}
		return VALOR_DEPOSITO * penalisation;
    }
}
