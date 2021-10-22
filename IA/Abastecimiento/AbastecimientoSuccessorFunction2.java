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
        
    	AbastecimientoState nextState = (AbastecimientoState) state;;
    	
    	s = new StringBuffer ();
    	this.max = 5;
    	
    	
    	Random rand = new Random();
    	int randomNum = rand.nextInt(max);
		
        int nCamiones = nextState.centrosDistribucion.size(), nGasos = nextState.gasolineras.size(), nPeticiones;

    	boolean b = false;
    	
    	Random gn, pn, pnc, pn1, pn2, cn, cn1, cn2;
    	Integer alGas, alCamion, alCamion1, alCamion2, alPeticion, alPn, alPn1, alPn2, alPeticionNoAsig, alPnCamion;
    	int sizeC, sizeC1, sizeC2;
    	
    	Pair<Integer, Integer> x;
    	
    	switch (randomNum){
		case 0:									           //modificamos el estado mediante la asignaPeticion
			
			gn = new Random();
			alGas = gn.nextInt(nGasos);			//Escoge las peticiones de una gasolinera aleatoria
			
			nPeticiones = nextState.gasolineras.get(alGas).getPeticiones().size();
			
			if (nPeticiones > 0) {
				pn = new Random();
				alPeticion = pn.nextInt(nPeticiones); 
    		
	    		cn = new Random();			    	    			
				alCamion = cn.nextInt(nCamiones);
				x = new Pair<Integer, Integer> (alGas, alPeticion);	    			
    	    	
				if (asigned(nextState, x.makeString())) {
    	    		b = nextState.asignaPeticion(alCamion, x, true);
	    			
    	    		if (b) s.append("asign petition, truck " + alCamion + " petition (" + x.geta() + "," + x.getb() + ")");
				}
			}

			break;    			
			
		case 1:														//Modificamos el estado mediante intercambiaPeticiones			 			
			
			cn1 = new Random();
			cn2 = new Random();
			alCamion1 = cn1.nextInt(nCamiones); 
			alCamion2 = cn2.nextInt(nCamiones);			
    		
			if (alCamion1 != alCamion2) {
				sizeC1 = nextState.getAsignaciones().get(alCamion1).size();
				sizeC2 = nextState.getAsignaciones().get(alCamion2).size();
				if (sizeC1 > 0 && sizeC2 > 0) {
					pn1 = new Random();
					pn2 = new Random();
					alPn1 =  pn1.nextInt(sizeC1);
					alPn2  = pn2.nextInt(sizeC2);

		    		b = nextState.intercambiaPeticiones(alPn1, alPn2, alCamion1, alCamion2);			//Aqui probablemente haga falta controlar muchisimas cosas, intercambiaPeticion puede fallar por TODO :)
	    	    																					
		    		if (b) s.append("swap petition, truck " + alCamion1 + " petition " + alPn1 + " with petition in truck " + alCamion2 + " petition " + alPn2);	
				}
				
			}
			break;
			
		case 2:														//Modificamos el estado mediante intercambioOrden		
			cn = new Random();
			alCamion = cn.nextInt(nCamiones);
    			
			sizeC = nextState.getAsignaciones().get(alCamion).size();
			if (sizeC > 0) {
				pn1 = new Random();
				pn2 = new Random();
				alPn1 = pn1.nextInt(sizeC);
				alPn2 = pn2.nextInt(sizeC);
		    			
				if (alPn1 != alPn2) {
					b = nextState.intercambioOrden(alPn1, alPn2, alCamion);
					if (b) s.append("swap petition order, truck " + alCamion + " petition " + alPn1 + " with petition " + alPn2);
				}
			}
			
					
			break;
			
		case 3:														//Modificamos el estado mediante cambiaPeticion	
			cn1 = new Random();
			cn2 = new Random();
    		alCamion1 = cn1.nextInt(nCamiones);
    		alCamion2 = cn2.nextInt(nCamiones);
				
			if (alCamion1 != alCamion2) {
				sizeC1 = nextState.getAsignaciones().get(alCamion1).size();
				if (sizeC1 > 0) {
					pn = new Random();
					alPn = pn.nextInt(sizeC1);
					
					b = nextState.cambiaPeticion(alPn, alCamion1, alCamion2);
				
					if (b) s.append("change petition" + alPn + ", from truck " + alCamion1 + " to truck " + alCamion2);
				}
				
			}
			break;
		 		
		case 4:														//Modificamos el estado mediante cambioPeticionNoAsig
			
			cn = new Random();
			alCamion = cn.nextInt(nCamiones);
			
			gn = new Random();
			alGas = gn.nextInt(nGasos);
			
			sizeC = nextState.gasolineras.get(alGas).getPeticiones().size();
			sizeC1 = nextState.getAsignaciones().get(alCamion).size();
			
			 if (sizeC > 0) {
				 pn = new Random();
				 alPeticionNoAsig = pn.nextInt(sizeC);
				 
				 if (sizeC1 > 0) {
				 	cn = new Random();
				 	alPnCamion = cn.nextInt(sizeC1);
				 
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