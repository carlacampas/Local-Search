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
        int nPet = currentState.getPeticionesDesatendidas().size();
    	boolean b = false;
    	
    	Integer alGas, alCamion, alCamion1, alCamion2, alPeticion, alPn, alPn1, alPn2, alPeticionNoAsig, alPnCamion;
    	int sizeC, sizeC1;
    	
    	Pair<Integer, Integer> x;
    	
    	switch (randomNum){
		case 0:
			if (nPet > 0) {
				alPeticion = rand.nextInt(nPet); 
    		
				alCamion = rand.nextInt(nCamiones);
				x = new Pair<Integer, Integer> ();	    
				
				int i = 0;
				String nextPet = "";
				for (String sn : currentState.getPeticionesDesatendidas()) {
					nextPet = sn;
					if (i == alPeticion) break;
					i++;
				}
				
				int petA = Integer.parseInt(x.fromStringA(nextPet));
    			int petB = Integer.parseInt(x.fromStringB(nextPet));

    			x.seta(petA);
    			x.setb(petB);
    	    	
    	    	b = nextState.asignaPeticion(alCamion, x, true);
	    		if (b) s.append("asign petition, truck " + alCamion + " petition (" + x.geta() + "," + x.getb() + ")");
			}

			break;    			
			
		case 1:														//Modificamos el estado mediante intercambiaPeticiones			 			
			alCamion1 = rand.nextInt(nCamiones); 
			alCamion2 = rand.nextInt(nCamiones);			
    		
			if (alCamion1 != alCamion2) {
				sizeC = nextState.getAsignaciones().get(alCamion1).size();
				sizeC1 = nextState.getAsignaciones().get(alCamion2).size();
				if (sizeC > 0 && sizeC1 > 0) {
					alPn1 =  rand.nextInt(sizeC);
					alPn2  = rand.nextInt(sizeC1);

		    		b = nextState.intercambiaPeticiones(alPn1, alPn2, alCamion1, alCamion2);			//Aqui probablemente haga falta controlar muchisimas cosas, intercambiaPeticion puede fallar por TODO :)
	    	    																					
		    		if (b) s.append("swap petition, truck " + alCamion1 + " petition " + alPn1 + " with petition in truck " + alCamion2 + " petition " + alPn2);	
				}
				
			}
			break;
			
		case 2:														//Modificamos el estado mediante intercambioOrden		
			alCamion = rand.nextInt(nCamiones);
    			
			sizeC = nextState.getAsignaciones().get(alCamion).size();
			if (sizeC > 0) {
				alPn1 = rand.nextInt(sizeC);
				alPn2 = rand.nextInt(sizeC);
		    			
				if (alPn1 != alPn2) {
					b = nextState.intercambioOrden(alPn1, alPn2, alCamion);
					if (b) s.append("swap petition order, truck " + alCamion + " petition " + alPn1 + " with petition " + alPn2);
				}
			}
			
					
			break;
			
		case 3:														//Modificamos el estado mediante cambiaPeticion	
    		alCamion1 = rand.nextInt(nCamiones);
    		alCamion2 = rand.nextInt(nCamiones);
				
			if (alCamion1 != alCamion2) {
				sizeC1 = nextState.getAsignaciones().get(alCamion1).size();
				if (sizeC1 > 0) {
					alPn = rand.nextInt(sizeC1);
					
					b = nextState.cambiaPeticion(alPn, alCamion1, alCamion2);
				
					if (b) s.append("change petition" + alPn + ", from truck " + alCamion1 + " to truck " + alCamion2);
				}
				
			}
			break;
		 		
		case 4:														//Modificamos el estado mediante cambioPeticionNoAsig
			alCamion = rand.nextInt(nCamiones);
			
			sizeC = nextState.getAsignaciones().get(alCamion).size();
			if (nPet == 0) break;
			alPeticion = rand.nextInt(nPet); 
			
			if (sizeC == 0) break;
			alPnCamion = rand.nextInt(sizeC);
			
			
			x = new Pair<Integer, Integer> ();	 
			
			int i = 0;
			String nextPet = "";
			for (String sn : currentState.getPeticionesDesatendidas()) {
				nextPet = sn;
				if (i == alPeticion) break;
				i++;
			}
			
			int petA = Integer.parseInt(x.fromStringA(nextPet));
			int petB = Integer.parseInt(x.fromStringB(nextPet));

			x.seta(petA);
			x.setb(petB);
			
			AbastecimientoState next = new AbastecimientoState (nextState);
							 
			b = (next.cambioPeticionNoAsig(alPnCamion, alCamion, x));
			if (b) s.append("cambio peticion no assig"); //s.append("changed petition " + alPnCamion + " with non assigned petition " + alPeticionNoAsig + "in truck " + alCamion); 

    	    break;	
    	    
    	   default:
    		   System.out.println("uooo you shouldn't be here...");
    	   		break;
		}
    			
    	return saSucesores;
    }
}