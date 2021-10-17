package Abastecimiento;

import java.util.ArrayList;

import IA.Gasolina.Gasolinera;
import aima.search.framework.HeuristicFunction;

public abstract class AbstractHeuristic implements HeuristicFunction {
    
	final static int COSTE_KILOMETRO = 2;
    final static int VALOR_DEPOSITO = 1000;
    
    public abstract double getHeuristicValue (Object state);
    
    protected double computeProfits(AbastecimientoState estado) {
    	double precioEnDepositos = 0.0;
        ArrayList<Integer> distancias = estado.getDistancias();

        // Recorrido por las asignaciones para calcular la suma del precio
        // de las peticiones atendidas y la distancia total recorrida
        for (ArrayList<Peticion> listaPeticiones : estado.getAsignaciones()) {
            for (int i = 0; i < listaPeticiones.size(); i++) {

                Peticion peticion = listaPeticiones.get(i);
                Gasolinera gasolinera = estado.gasolineras.get(peticion.get().a);

                // Añadir precio de una peticion al precio total:
                int diasPendientes = gasolinera.getPeticiones().get(peticion.get().b);
                if (diasPendientes == 0) precioEnDepositos += 1.02;
                else precioEnDepositos += (100 - Math.pow(2, diasPendientes)) / 100;
            }
        }
        
        double kilometros = 0;
        for (Integer distancia : distancias) {
        	kilometros += AbastecimientoState.maxDist - distancia;
        }
        
        return VALOR_DEPOSITO * precioEnDepositos - COSTE_KILOMETRO * kilometros;
    }
}
