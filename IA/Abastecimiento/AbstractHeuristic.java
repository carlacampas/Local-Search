package Abastecimiento;

import java.util.ArrayList;

import IA.Gasolina.Gasolinera;
import aima.search.framework.HeuristicFunction;

public abstract class AbstractHeuristic implements HeuristicFunction {

    public abstract double getHeuristicValue (Object state);
    
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
		return estado.getValorDeposito() * penalisation;
    }
}
