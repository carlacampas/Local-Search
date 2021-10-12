package Abastecimiento;

// IMPORTS.
import java.util.*;

import aima.search.framework.HeuristicFunction;

import IA.Gasolina.Gasolineras;
import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Distribucion;
import IA.Gasolina.Gasolinera;

// Aquest valora els costos.
public class AbastecimientoHeuristicFunction1 implements HeuristicFunction {

    final static int COSTE_KILOMETRO = 2;
    final static int VALOR_DEPOSITO = 1000;

    public double getHeuristicValue (AbastecimientoState state) {

        double precioTotal = 0.0;
        double distanciaTotal = 0.0;

        // Recorrido por las asignaciones para calcular la suma del precio
        // de las peticiones atendidas y la distancia total recorrida
        for (ArrayList<Peticion> listaPeticiones : state.getAsignaciones()) {
            double distanciaCamion = 0.0;
            for (int i = 0; i < listaPeticiones.size(); i++) {

                Peticion peticion = listaPeticiones.get(i);
                Gasolinera gasolinera = state.gasolineras.get(peticion.a);
                Distribucion distribucion = state.centrosDistribucion.get(i);

                // Añadir distancia recorrida por una petición a la distancia recorrida por UN camión:
                Gasolinera gasolineraAnterior;
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
                int diasPendientes = gasolinera.getPeticiones(peticion.b);
                precioTotal += VALOR_DEPOSITO * ((100 - 2 ^ diasPendientes) / 100);
            }
            distanciaTotal += distanciaCamion;
        }
        return precioTotal - COSTE_KILOMETRO * distanciaTotal;
    }

    private int calcularDistancias(int fromX, int fromY, int toX, int toY) {
        return Math.abs (fromX - toX) + Math.abs (fromY - toY);
    }
}