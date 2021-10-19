package Abastecimiento;

// IMPORTS.
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class AbastecimientoSuccessorFunction2 implements SuccessorFunction{
	
    private StringBuffer s;
	private int max;
	    
    public List getSuccessors (Object state) {
    	
    	ArrayList <Successor> saSucesores = new ArrayList<>();
        
    	AbastecimientoState currentState = (AbastecimientoState) state;
    	AbastecimientoState nextState = null;
    	
    	s = new StringBuffer ();
    	this.max = 5;
    	
    	
    	Random rand = new Random();
    	int randomNum = rand.nextInt(max);
        	    	
        nextState = getNextStateNoLoops(currentState, randomNum);
    	
        nextState = (nextState == null ? currentState : nextState);
		
    			
    	saSucesores.add(new Successor(s.toString(), nextState));
    	return saSucesores;
    }
    
    private boolean asigned(AbastecimientoState as, Pair <Integer, Integer > alPn) {
    	for (ArrayList <Peticion> pns : as.getAsignaciones())
    		for (Peticion p : pns){
    			if (p.get().equals(alPn)) return true; 		
    	}
    	return false;
    }
    /*
    private AbastecimientoState getNextStateLoops(AbastecimientoState as, int rnd) {
    	int nCamiones = as.centrosDistribucion.size(), nGasos = as.gasolineras.size();
    	
    	boolean b = false;
		
		int gIt = 0, cIt = 0, pIt = 0;
    	
		switch (rnd){
    		case 1:									           //modificamos el estado mediante la asignaPeticion
    			
    			
    			while (!b && gIt <= nGasos) {
    				Random gn = new Random();
	    			Integer alGas = gn.nextInt(nGasos);			//Escoge las peticiones de una gasolinera aleatoria
	    			
	    			int nPeticiones = as.gasolineras.get(alGas).getPeticiones().size();
	    			if (nPeticiones > 0) {
	    				Random pn = new Random();
	    				Integer alPeticion = pn.nextInt(nPeticiones); 
		    		
	    	    		Random cn = new Random();			    	    			
	    				int alCamion = cn.nextInt(nCamiones);
	    				
		    			Pair<Integer, Integer> x = new Pair <Integer, Integer> (); 
			    	    	
	    	    		while (!b && pIt <= nPeticiones){
		    	    		pn = new Random();
		    	    		alPeticion = pn.nextInt(nPeticiones); 
		    	    	
	    	    			if (!asigned(as, alGas, alPeticion.intValue())) {
	    	    				x = new Pair<Integer, Integer> (alGas, alPeticion);
		    	    			b = as.asignaPeticion(alCamion, x);
	    	    			}
	    	    			pIt++;
	    	    		}
		    			if (b) s.append("asign petition, truck " + alCamion + " petition (" + x.geta() + "," + x.getb() + ")");
	    			}
	    			
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
    */
    private AbastecimientoState getNextStateNoLoops(AbastecimientoState as, int rnd) {
    	
    	int nCamiones = as.centrosDistribucion.size(), nGasos = as.gasolineras.size(), nPeticiones;

    	boolean b = false;
    	
    	Random gn, pn, pnc, pn1, pn2, cn, cn1, cn2;
    	Integer alGas, alCamion, alCamion1, alCamion2, alPeticion, alPn, alPn1, alPn2, alPeticionNoAsig, alPnCamion;
    	int sizeC, sizeC1, sizeC2;
    	
    	Pair<Integer, Integer> x;
    	
    	switch (rnd){
		case 1:									           //modificamos el estado mediante la asignaPeticion
			
			gn = new Random();
			alGas = gn.nextInt(nGasos);			//Escoge las peticiones de una gasolinera aleatoria
			
			nPeticiones = as.gasolineras.get(alGas).getPeticiones().size();
			
			if (nPeticiones > 0) {
				pn = new Random();
				alPeticion = pn.nextInt(nPeticiones); 
    		
	    		cn = new Random();			    	    			
				alCamion = cn.nextInt(nCamiones);
				x = new Pair<Integer, Integer> (alGas, alPeticion);	    			
    	    	
				if (!asigned(as, x)) {
    	    		b = as.asignaPeticion(alCamion, x);
	    			
    	    		if (b) s.append("asign petition, truck " + alCamion + " petition (" + x.geta() + "," + x.getb() + ")");
				}
			}

			break;    			
			
		case 2:														//Modificamos el estado mediante intercambiaPeticiones			 			
			
			cn1 = new Random();
			cn2 = new Random();
			alCamion1 = cn1.nextInt(nCamiones); 
			alCamion2 = cn2.nextInt(nCamiones);			
    		
			if (alCamion1 != alCamion2) {
				sizeC1 = as.getAsignaciones().get(alCamion1).size();
				sizeC2 = as.getAsignaciones().get(alCamion2).size();
				
				pn1 = new Random();
				pn2 = new Random();
				alPn1 =  pn1.nextInt(sizeC1);
				alPn2  = pn2.nextInt(sizeC2);

	    		b = as.intercambiaPeticiones(alPn1, alPn2, alCamion1, alCamion2);			//Aqui probablemente haga falta controlar muchisimas cosas, intercambiaPeticion puede fallar por TODO :)
    	    																					
	    		if (b) s.append("swap petition, truck " + alCamion1 + " petition " + alPn1 + " with petition in truck " + alCamion2 + " petition " + alPn2);	
			}
			break;
			
		case 3:														//Modificamos el estado mediante intercambioOrden		
			cn = new Random();
			alCamion = cn.nextInt(nCamiones);
    			
			sizeC = as.getAsignaciones().get(alCamion).size();
				
			pn1 = new Random();
			pn2 = new Random();
			alPn1 = pn1.nextInt(sizeC);
			alPn2 = pn2.nextInt(sizeC);
	    			
			if (alPn1 != alPn2) {
				b = as.intercambioOrden(alPn1, alPn2, alCamion);
				if (b) s.append("swap petition order, truck " + alCamion + " petition " + alPn1 + " with petition " + alPn2);
			}
					
			break;
			
		case 4:														//Modificamos el estado mediante cambiaPeticion	
			cn1 = new Random();
			cn2 = new Random();
    		alCamion1 = cn1.nextInt(nCamiones);
    		alCamion2 = cn2.nextInt(nCamiones);
				
			if (alCamion1 != alCamion2) {
				sizeC1 = as.getAsignaciones().get(alCamion1).size();

				pn = new Random();
				alPn = pn.nextInt(sizeC1);
				
				b = as.cambiaPeticion(alPn, alCamion1, alCamion2);
			
				if (b) s.append("change petition" + alPn + ", from truck " + alCamion1 + " to truck " + alCamion2);
			}
			break;
		 		
		case 5:														//Modificamos el estado mediante cambioPeticionNoAsig
			
			gn = new Random();
			alGas = gn.nextInt(nGasos);			
    		
			sizeC = as.gasolineras.get(alGas).getPeticiones().size();
			
			pn = new Random();
			alPeticionNoAsig = pn.nextInt(sizeC); 
			
			cn = new Random();
			alCamion = cn.nextInt(nCamiones);
	    			
			pnc = new Random();
			alPnCamion = pnc.nextInt(as.getAsignaciones().get(alCamion).size());
			
			x = new Pair<Integer, Integer> (alGas, alPeticionNoAsig);
			
			if (!asigned(as, x)) {				
				b = as.cambioPeticionNoAsig(alPnCamion, alCamion, x);
				
	    	    if (b) s.append("changed petition " + alPnCamion + " with non assigned petition " + alPeticionNoAsig + "in truck " + alCamion);
			}		    	    		 	    	
    	    break;	
		}
    	
    	return (!b ? null : as);

    }

}