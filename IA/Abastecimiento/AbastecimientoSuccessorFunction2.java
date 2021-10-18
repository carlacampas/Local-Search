package Abastecimiento;

// IMPORTS.
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class AbastecimientoSuccessorFunction2 implements SuccessorFunction{
	
   // private AbastecimientoState lastNode;
    //private int steps = 1000;
    //private int k = 5;
    //private int limit = 100;
    //private double lambda = 0.01;
    private StringBuffer s;
	    
	    
    public List getSuccessors (Object state) {
    	
    	ArrayList <Successor> saSucesores = new ArrayList<>();
        
    	AbastecimientoState currentState = (AbastecimientoState) state;
    	AbastecimientoState nextState = new AbastecimientoState ();
    	
    	s = new StringBuffer ();
    	
    	while (nextState == null) {
    		Random rand = new Random();
    		int randomNum = rand.nextInt(5);
        	    	
        	nextState = (AbastecimientoState) getNextState(currentState, randomNum);
    	}
		
    	saSucesores.add(new Successor(s.toString(), nextState));

    	return saSucesores;
    }
    
    private boolean asigned(AbastecimientoState as, int gas, int pet) {
    	int n = as.getAsignaciones().size();
    	
    	for (int i = 0; i < n; i++){
    		int m = as.getAsignaciones().get(i).size();
    		
    		for (int j = 0; j < m; j++) {
    			Pair<Integer, Integer> current = as.getAsignaciones().get(i).get(j).get();
    			
    			if (current.a == gas && current.b == pet) return true;			
    		}
    	}
    	return false;
    }
    
    private AbastecimientoState getNextState(AbastecimientoState as, int rnd) {
    	int nCamiones = as.centrosDistribucion.size(), nGasos = as.gasolineras.size();
    	
    	boolean b = false;
		
		int gIt = 0, cIt = 0, pIt = 0;
    	
		switch (rnd){
    		case 1:									           //modificamos el estado mediante la asignaPeticion
    			
    			
    			while (!b && gIt <= nGasos) {
    				Random gn = new Random();
	    			Integer alGas = gn.nextInt(nGasos);			//Escoge las peticiones de una gasolinera aleatoria
	    			
	    			int nPeticiones = as.gasolineras.get(alGas).getPeticiones().size();
	    			
	    			Random pn = new Random();
    	    		int alPeticion = pn.nextInt(nPeticiones); 
		    		
    	    		Random cn = new Random();			    	    			
    				int alCamion = cn.nextInt(nCamiones);
    				
	    			Pair<Integer, Integer> x = new Pair <Integer, Integer> (); 
		    	    	
    	    		while (!b && pIt <= nPeticiones){
	    	    		pn = new Random();
	    	    		alPeticion = pn.nextInt(nPeticiones); 
	    	    	
    	    			if (!asigned(as, alGas, alPeticion)) {
    	    				x = new Pair<Integer, Integer> (alGas, alPeticion);
	    	    			b = as.asignaPeticion(alCamion, x);
    	    			}
    	    			pIt++;
	    	    	}
	    			if (b) s.append("asign petition, truck " + alCamion + " petition (" + x.geta() + "," + x.getb() + ")");
	    			gIt++;
    			}
    			
    			break;    			
    			
    		case 2:														//Modificamos el estado mediante intercambiaPeticiones			
    			int it2 = 0;
    			
    			while (!b && it2 <= nCamiones) {
	    			
    				Random cn1 = new Random(), cn2 = new Random();
	    			int alCamion1 = cn1.nextInt(nCamiones), alCamion2 = cn2.nextInt(nCamiones);			
		    		
	    			Random pn1 = new Random(), pn2 = new Random();
	    			Integer alPn1 = 0;
	    			Integer alPn2 = 0;
	    		
	    			if (alCamion1 != alCamion2) {
	    				int its = 0;
	    				int sizeC1 = as.getAsignaciones().get(alCamion1).size();
	    				int sizeC2 = as.getAsignaciones().get(alCamion2).size();
	    				
	    				while (!b && its <= sizeC1+sizeC2){
		    				pn1 = new Random();
		    				pn2 = new Random();
		    				alPn1 =  pn1.nextInt(sizeC1);
		    				alPn2  = pn2.nextInt(sizeC2);

		    	    		b = as.intercambiaPeticiones(alPn1, alPn2, alCamion1, alCamion2);			//Aqui probablemente haga falta controlar muchisimas cosas, intercambiaPeticion puede fallar por TODO :)
		    	    		its++;
		    	    	}																				
		    		}
	    			if (b) s.append("swap petition, truck " + alCamion1 + " petition " + alPn1 + " with petition in truck " + alCamion2 + " petition " + alPn2);
	    			it2++;
    			}
    			
    			break;
    			
    		case 3:														//Modificamos el estado mediante intercambioOrden		
    			while (!b && cIt <= nCamiones) {
    				Random cn1 = new Random();
    				Integer alCamion = cn1.nextInt(nCamiones);
    				
    				Random pn1 = new Random(), pn2 = new Random();
    				Integer alPn1 = 0;
	    			Integer alPn2 = 0;
	    			
    				int sizeC1 = as.getAsignaciones().get(alCamion).size();
    				int its = 0;
    				
    				while (!b && its <= sizeC1) {
    					pn1 = new Random();
    					pn2 = new Random();
		    			alPn1 = pn1.nextInt(sizeC1);
		    			alPn2 = pn2.nextInt(sizeC1);
		    			
		    			if (alPn1 != alPn2) {
		    				b = as.intercambioOrden(alPn1, alPn2, alCamion);
		    			}
    					its++;
    				}
    				if (b) s.append("swap petition order, truck " + alCamion + " petition " + alPn1 + " with petition " + alPn2);
    				cIt++;
    			}
    			break;
    			
    		case 4:														//Modificamos el estado mediante cambiaPeticion
    			int it4 = 0;
    			while (!b && it4 <= nCamiones) {
    				
    				Random cn1 = new Random(), cn2 = new Random();
	    			int alCamion1 = cn1.nextInt(nCamiones);
	    			int alCamion2 = cn2.nextInt(nCamiones);
	    			
	    			Random pn = new Random();
    				Integer alPn = 0;
    				
	    			if (alCamion1 != alCamion2) {
	    				int sizeC1 = as.getAsignaciones().get(alCamion1).size();

	    				while(!b && pIt <= sizeC1) {
	    					pn = new Random();
		    				alPn = pn.nextInt(sizeC1);
		    				
		    				b = as.cambiaPeticion(alPn, alCamion1, alCamion2);
		    				pIt++;
	    				}
	    			}
	    			if (b) s.append("change petition" + alPn + ", from truck " + alCamion1 + " to truck " + alCamion2);
    				++it4;
    			}
    			break;
    		 		
    		case 5:														//Modificamos el estado mediante cambioPeticionNoAsig
    			
    			while (!b && gIt <= nGasos) {
    				Random gn = new Random();
	    			Integer alGas = gn.nextInt(nGasos);			//Escoge las peticiones de una gasolinera aleatoria
		    		
	    			Random pn = new Random();
    	    		Integer alPeticionNoAsig = pn.nextInt(as.gasolineras.get(alGas).getPeticiones().size()); 
    	    		
    	    		Random cn = new Random();
    				int alCamion = cn.nextInt(nCamiones);
		    			
    				Random pnc = new Random();
    				int alPnCamion = pnc.nextInt(as.getAsignaciones().get(alCamion).size());
    				
    				int sizeC1 = as.gasolineras.get(alGas).getPeticiones().size();
		    	    
	    	    	while (!b && pIt <= sizeC1){
	    	    		pn = new Random();
	    	    		alPeticionNoAsig = pn.nextInt(sizeC1); 
	    	    		
	    	    			if (!asigned(as, alGas, alPeticionNoAsig)) {
	    	    				cn = new Random();
	    	    				alCamion = cn.nextInt(nCamiones);
	    	    				
	    	    				int sizeC = as.getAsignaciones().get(alCamion).size();
	    	    				int it = 0;
	    	    				
	    	    				while (!b && it < sizeC) {
	    	    					pnc = new Random();
		    	    				alPnCamion = pnc.nextInt(sizeC);
		    	    				
		    	    				Pair<Integer, Integer> x = new Pair<Integer, Integer> (alGas, alPeticionNoAsig);
			    	    		
		    	    				b = as.cambioPeticionNoAsig(alPnCamion, alCamion, x);
		    	    				
		    	    				it++;
	    	    				}		    		
	    	    			}		    	    		
	    	    		pIt++;
	    	    	}
	    	    	if (b) s.append("changed petition " + alPnCamion + " with non assigned petition " + alPeticionNoAsig + "in truck " + alCamion);
	    			gIt++;
		    	}
    				break;	
    		}	
 
    	return (!b ? null : as);
    }
}