package Abastecimiento;

// IMPORTS.
import java.util.*;

import aima.search.framework.HeuristicFunction;

import IA.Gasolina.Distribucion;
import IA.Gasolina.Gasolinera;

// Aquest valora els costos.
public class AbastecimientoHeuristicFunction1 implements HeuristicFunction {

    final static int COSTE_KILOMETRO = 2;
    final static int VALOR_DEPOSITO = 1000;

	public double getHeuristicValue (Object state) {
    	
    	AbastecimientoState estado = (AbastecimientoState) state;

        double precioTotal = 0.0;
        double distanciaTotal = 0.0;

        // Recorrido por las asignaciones para calcular la suma del precio
        // de las peticiones atendidas y la distancia total recorrida
        for (ArrayList<Peticion> listaPeticiones : estado.getAsignaciones()) {
            double distanciaCamion = 0.0;
            Gasolinera gasolineraAnterior = null;
            for (int i = 0; i < listaPeticiones.size(); i++) {

                Peticion peticion = listaPeticiones.get(i);
                Gasolinera gasolinera = estado.gasolineras.get(peticion.get().a);
                Distribucion distribucion = estado.centrosDistribucion.get(i);

                // Añadir distancia recorrida por una petición a la distancia recorrida por UN camión:
                if (i == 0) { // Primer pedido: Distancia entre el centro de distribución y la gasolinera que ha hecho el pedido
                    distanciaCamion += calcularDistancias(distribucion.getCoordX(), distribucion.getCoordY(), gasolinera.getCoordX(), gasolinera.getCoordY());
                }
                else { // Distancia entre la gasolinera que ha hecho el pedido anterior y la gasolinera del pedido actual
                    distanciaCamion += calcularDistancias(gasolineraAnterior.getCoordX(), gasolineraAnterior.getCoordY(), gasolinera.getCoordX(), gasolinera.getCoordY());
                    if (i == listaPeticiones.size()) { // Último pedido: Además también se sumala distancia entre la gasolinera y el centro de distribucion
                        distanciaCamion += calcularDistancias(gasolinera.getCoordX(), gasolinera.getCoordY(), distribucion.getCoordX(), distribucion.getCoordY());
                    }
                }
                gasolineraAnterior = gasolinera;

                // Añadir precio de una peticion al precio total:
                int diasPendientes = gasolinera.getPeticiones().get(peticion.get().b);
                if (diasPendientes == 0) precioTotal += VALOR_DEPOSITO * 1.02;
                else precioTotal += VALOR_DEPOSITO * ((100 - 2 ^ diasPendientes) / 100);
            }
            distanciaTotal += distanciaCamion;
        }
        
        // TODO: Erase prints
        System.out.println("Real");
        System.out.println(precioTotal);
        System.out.println(distanciaTotal);
        
        return precioTotal - COSTE_KILOMETRO * distanciaTotal;
    }

    static int calcularDistancias(int fromX, int fromY, int toX, int toY) {
        return Math.abs (fromX - toX) + Math.abs (fromY - toY);
    }
}