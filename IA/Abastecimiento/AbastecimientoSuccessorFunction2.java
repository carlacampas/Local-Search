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
    	AbastecimientoState nextState = new AbastecimientoState (currentState);
    	
    	s = new StringBuffer ();
    	this.max = 5;
    	
    	
    	Random rand = new Random();
    	int randomNum = rand.nextInt(max);
		
        int nCamiones = nextState.centrosDistribucion.size(), nGasos = nextState.gasolineras.size(), nPeticiones;

    	boolean b = false;
    	
    	Integer alGas, alCamion, alCamion1, alPn, alPn1, alPeticionNoAsig, alPnCamion;
    	int sizeC, sizeC1;
    	
    	Pair<Integer, Integer> x;
    	
    	switch (randomNum){
		case 0:									           //modificamos el estado mediante la asignaPeticion
			
			alCamion = rand.nextInt(nCamiones);
			alGas = rand.nextInt(nGasos);
			nPeticiones = currentState.gasolineras.get(alGas).getPeticiones().size();
			if (nPeticiones > 0) {
				alPn = rand.nextInt(nPeticiones);
				x = new Pair<Integer, Integer> (alGas, alPn);
				
				if (asigned(currentState, x.makeString())) {
						b = nextState.asignaPeticion(alCamion, x, true);
						if (b) s.append("asign petition, truck " + alCamion + " petition (" + x.geta() + "," + x.getb() + ")");
					
				}
			}
			
			break;    	
			
			case 1:														//Modificamos el estado mediante intercambiaPeticiones			 			
			alCamion = rand.nextInt(nCamiones);
			alCamion1 = rand.nextInt(nCamiones);
			
			if (alCamion != alCamion1) {
				sizeC = currentState.getAsignaciones().get(alCamion).size();
				sizeC1 = currentState.getAsignaciones().get(alCamion1).size();
				
				if (sizeC > 0 && sizeC1 > 0) {
					alPn =  rand.nextInt(sizeC);
					alPn1  = rand.nextInt(sizeC1);
					
					b = nextState.intercambiaPeticiones(alPn, alPn1, alCamion, alCamion1);			
					
		    		if (b) s.append("swap petition, truck " + alCamion + " petition " + alPn + " with petition in truck " + alCamion1 + " petition " + alPn1);	
				}
			}
		
			break; 
			
		case 2:														//Modificamos el estado mediante intercambioOrden		
			alCamion = rand.nextInt(nCamiones);
			sizeC = currentState.getAsignaciones().get(alCamion).size();
			
			if (sizeC > 1) {
				alPn = rand.nextInt(sizeC);
				alPn1 = rand.nextInt(sizeC);
				
				if ( alPn != alPn1) {
					b = nextState.intercambioOrden(alPn, alPn1, alCamion);
					if (b) s.append("swap petition order, truck " + alCamion + " petition " + alPn + " with petition " + alPn1);
				}
			}
					
			break;
			
		case 3:														//Modificamos el estado mediante cambiaPeticion	
    		alCamion = rand.nextInt(nCamiones);
    		alCamion1 = rand.nextInt(nCamiones);
				
			if (alCamion != alCamion1) {
				sizeC = nextState.getAsignaciones().get(alCamion1).size();
				if (sizeC > 0) {
					alPn = rand.nextInt(sizeC);
					
					b = nextState.cambiaPeticion(alPn, alCamion1, alCamion);
				
					if (b) s.append("change petition" + alPn + ", from truck " + alCamion1 + " to truck " + alCamion);
				}
				
			}
			break;
		 		
		case 4:														//Modificamos el estado mediante cambioPeticionNoAsig
			alCamion = rand.nextInt(nCamiones);
			
			alGas = rand.nextInt(nGasos);
			
			sizeC = nextState.gasolineras.get(alGas).getPeticiones().size();
			sizeC1 = nextState.getAsignaciones().get(alCamion).size();
			
			 if (sizeC > 0) {
				 alPeticionNoAsig = rand.nextInt(sizeC);
				 
				 if (sizeC1 > 0) {
				 	alPnCamion = rand.nextInt(sizeC1);
				 
					 x = new Pair <Integer, Integer> (alGas, alPeticionNoAsig);
					 
					 if (!asigned(nextState, x.makeString())) {
						 AbastecimientoState next = new AbastecimientoState (nextState);
						 
						 
						 b = (next.cambioPeticionNoAsig(alPnCamion, alCamion, x));
						 
						 if (b) s.append("changed petition " + alPnCamion + " with non assigned petition " + alPeticionNoAsig + "in truck " + alCamion); 
					 }
				 
				 }
					 
			 }
			 
    	    break;
    	    
    	   default:
    		   System.out.println("uooo you shouldn't be here...");
    	   		break;
		}
    			
    	saSucesores.add(new Successor(s.toString(), nextState));
    	return saSucesores;
    }
    
    private boolean asigned(AbastecimientoState as, String alPn) {
    	return as.getPeticionesDesatendidas().contains(alPn);
    }
}