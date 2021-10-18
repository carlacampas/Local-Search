package Abastecimiento;

// IMPORTS.
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class AbastecimientoSuccessorFunction2 implements SuccessorFunction{
	
	private Object goalState;
   // private AbastecimientoState lastNode;
    private int steps;
    private boolean trace = false;
    //private Scheduler scheduler;
    
    private int k;
    private int limit;
    private double lambda;
	    
	    
    public List getSuccessors (Object state) {
    	
    	List saSucesores = new ArrayList();
        
    	AbastecimientoState currentState = (AbastecimientoState) state;
    	AbastecimientoState nextState = null;
    	AbastecimientoState bestState = currentState;
    	
    	Random rand = new Random();
    	int randomNum = rand.nextInt(5) + 1;
    	
    	for (int step = 0; step < this.steps; step++) {
    		double temperature = computeTemperature(step);
    		if (temperature == 0.0) break;
    		
    		nextState = (AbastecimientoState) nextState(currentState, randomNum);
    		double valNxt = nextState.getBenefit(), valCurr = currentState.getBenefit();
    		double dE = valNxt - valCurr;
    		
    		Random v = new Random();
    		double al = v.nextDouble();
    		double prob = 1.0 / (1.0 + Math.exp(dE / temperature));
    		
    		/*if (trace && (dE < 0.0) && (al > prob)) {
    			//??
    		}*/
    		
    		if ((dE > 0.0) || (al > prob)) {
    			if (valCurr > valNxt) bestState = nextState;
    			currentState = nextState;			
    		}
    		
    		
    	}
    	goalState = bestState;
    	saSucesores.add(goalState);
    
    	
    	return saSucesores;
    }
    
    private double computeTemperature(int atStep) {
    	double tempD = this.limit*(atStep/this.limit);;
    	int temp = (int) tempD;
    	
    	return this.k * Math.exp((-1)*this.lambda*temp);
    	
    }
    
    private boolean asigned(AbastecimientoState as, int gas, int pet) {
    	int n = as.getAsignaciones().size();
    	Pair<Integer, Integer> peticion = new Pair <Integer, Integer>(gas, pet);
    	
    	for (int i = 0; i < n; i++){
    		int m = as.getAsignaciones().get(i).size();
    		
    		for (int j = 0; j < m; j++) {
    			Pair<Integer, Integer> current = as.getAsignaciones().get(i).get(j).get();
    			
    			if (current.a == peticion.a && current.b == peticion.b) return true;			
    		}
    	}
    	return false;
    }
    
    private AbastecimientoState nextState(AbastecimientoState as, int rnd) {
    	int nCamiones = as.centrosDistribucion.size(), nGasos = as.gasolineras.size();
    	boolean b = false;
		ArrayList<Integer> visitedGas = new ArrayList<Integer>();
		ArrayList<Integer> visitedCam = new ArrayList<Integer>();

    	switch (rnd){
    		case 1:		//modificamos el estado mediante la asignaPeticion
    			while (!b && visitedGas.size() <= nGasos) {
    				Random gn = new Random();
	    			Integer alGas = gn.nextInt(nGasos-1) + 1;			//Escoge las peticiones de una gasolinera aleatoria
		    		
	    			if (!visitedGas.contains(alGas)) {
		    			ArrayList<Integer> visitedPet = new ArrayList<Integer>();
		    	    
		    	    	while (!b && visitedPet.size() <= as.gasolineras.get(alGas).getPeticiones().size()){
		    	    		Random pn = new Random();
		    	    		Integer alPeticion = pn.nextInt(as.gasolineras.size()-1)+1; 
		    	    		
		    	    		if (!visitedPet.contains(alPeticion)) {
		    	    			
		    	    			if (!asigned(as, alGas, alPeticion)) {
		    	    				Random cn = new Random();
			    	    			
		    	    				int alCamion = cn.nextInt(nCamiones-1)+1;
			    	    			
			    	    			Pair<Integer, Integer> x = new Pair<Integer, Integer> (alGas, alPeticion);
			    	    			
			    	    			b = as.asignaPeticion(alCamion, x);
		    	    			}
		    	    			else visitedPet.add(alPeticion);
		    	    		}
		    	    	}
		    	    	if (!b) visitedGas.add(alGas);
		    		}
    			}
    			break;    			
    			
    		case 2:														//Modificamos el estado mediante intercambiaPeticiones			
    			int it2 = 0;
    			while (!b && it2 <= nCamiones*nCamiones) {
	    			
    				Random cn1 = new Random(), cn2 = new Random();
	    			int alCamion1 = cn1.nextInt(nGasos-1) + 1, alCamion2 = cn2.nextInt(nGasos-1)+1;			
		    		
	    			if (alCamion1 != alCamion2) {
	    				int its = 0;
	    				int sizeC1 = as.getAsignaciones().get(alCamion1).size();
	    				int sizeC2 = as.getAsignaciones().get(alCamion2).size();
	    				
	    				while (!b && its <= sizeC1*sizeC2){
		    				Random pn1 = new Random(), pn2 = new Random();
			    			Integer alPn1 = pn1.nextInt(sizeC1-1) + 1;
			    			Integer alPn2 = pn2.nextInt(sizeC2-1) + 1;

		    	    		b = as.intercambiaPeticiones(alPn1, alPn2, alCamion1, alCamion2);			//Aqui probablemente haga falta controlar muchisimas cosas, intercambiaPeticion puede fallar por TODO :)
		    	    		its++;
		    	    	}																				
		    		}
	    			it2++;
    			}
    			break;
    			
    		case 3:														//Modificamos el estado mediante intercambioOrden		
    			while (!b && visitedCam.size() <= nCamiones) {
    				Random cn1 = new Random();
    				Integer alCamion = cn1.nextInt(nGasos-1) + 1;
    				
    				int sizeC1 = as.getAsignaciones().get(alCamion).size();
    				int its = 0;
    				while (!b && its <= sizeC1*sizeC1) {
    					Random pn1 = new Random(), pn2 = new Random();
		    			Integer alPn1 = pn1.nextInt(sizeC1-1) + 1;
		    			Integer alPn2 = pn2.nextInt(sizeC1-1) + 1;
		    			
		    			if (alPn1 != alPn2) {
		    				b = as.intercambioOrden(alPn1, alPn2, alCamion);
		    			}
    					its++;
    				}
    				if (!b) visitedCam.add(alCamion);
    			}
    			break;
    			
    		case 4:														//Modificamos el estado mediante cambiaPeticion
    			int it4 = 0;
    			while (!b && it4 <= nCamiones*nCamiones) {
    				
    				Random cn1 = new Random(), cn2 = new Random();
	    			int alCamion1 = cn1.nextInt(nGasos-1) + 1;
	    			int alCamion2 = cn2.nextInt(nGasos-1) + 1;
	    			
	    			if (alCamion1 != alCamion2) {
	    				int sizeC1 = as.getAsignaciones().get(alCamion1).size();
	    				
	    				ArrayList<Integer> visitedPet = new ArrayList<Integer>();
	    				
	    				while(!b && visitedPet.size() <= sizeC1) {
	    					Random pn = new Random();
		    				Integer alPn = pn.nextInt(sizeC1-1) + 1;
		    				
		    				if (!visitedPet.contains(alPn)) {
		    					b = as.cambiaPeticion(alPn, alCamion1, alCamion2);
			    				
			    				if (!b) visitedPet.add(alPn);
		    				}
	    				}
	    			}
    				++it4;
    			}
    			break;
    		 		
    		case 5:														//Modificamos el estado mediante cambioPeticionNoAsig
    			
    			while (!b && visitedGas.size() <= nGasos) {
    				Random gn = new Random();
	    			Integer alGas = gn.nextInt(nGasos-1) + 1;			//Escoge las peticiones de una gasolinera aleatoria
		    		
	    			if (!visitedGas.contains(alGas)) {
		    			ArrayList<Integer> visitedPnNoAsig = new ArrayList<Integer>();
		    	    
		    	    	while (!b && visitedPnNoAsig.size() <= as.gasolineras.get(alGas).getPeticiones().size()){
		    	    		Random pn = new Random();
		    	    		Integer alPeticionNoAsig = pn.nextInt(as.gasolineras.get(alGas).getPeticiones().size()-1)+1; 
		    	    		
		    	    		
		    	    		if (!visitedPnNoAsig.contains(alPeticionNoAsig)) {
		    	    			
		    	    			if (!asigned(as, alGas, alPeticionNoAsig)) {
		    	    				Random cn = new Random();
		    	    				int alCamion = cn.nextInt(nCamiones-1)+1;
		    	    				int sizeC = as.getAsignaciones().get(alCamion).size();
		    	    				int it = 0;
		    	    				
		    	    				while (!b && it < sizeC*sizeC) {
		    	    					Random pnc = new Random();
			    	    				int alPnCamion = pnc.nextInt(as.getAsignaciones().get(alCamion).size()-1)+1;
			    	    				
			    	    				Pair<Integer, Integer> x = new Pair<Integer, Integer> (alGas, alPeticionNoAsig);
				    	    		
			    	    				b = as.cambioPeticionNoAsig(alPnCamion, alCamion, x);
			    	    				
			    	    				it++;
		    	    				}		    		
		    	    			}
		    	    			else visitedPnNoAsig.add(alPeticionNoAsig);
		    	    		}
		    	    	}
		    	    	if (!b) visitedGas.add(alGas);
		    		}
    			}
    		break;	
    	}	
 	
    	return (!b ? null : as);
    }
}