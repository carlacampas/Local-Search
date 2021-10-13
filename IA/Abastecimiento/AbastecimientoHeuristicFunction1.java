package Abastecimiento;

// IMPORTS.
import java.util.*;

import aima.search.framework.HeuristicFunction;
import IA.Gasolina.Gasolinera;

// Aquest valora els costos.
public class AbastecimientoHeuristicFunction1 implements HeuristicFunction {

    final static int COSTE_KILOMETRO = 2;
    final static int VALOR_DEPOSITO = 1000;

	public double getHeuristicValue (Object state) {
    	
    	AbastecimientoState estado = (AbastecimientoState) state;

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

    static int calcularDistancias(int fromX, int fromY, int toX, int toY) {
        return Math.abs (fromX - toX) + Math.abs (fromY - toY);
    }
}