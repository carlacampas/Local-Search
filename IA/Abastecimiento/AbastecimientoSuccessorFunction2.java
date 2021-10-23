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

    private int k;
    private int limit;
    private double lambda;
    private StringBuffer s;


    public List getSuccessors (Object state) {

    	ArrayList <Successor> saSucesores = new ArrayList<>();

    	AbastecimientoState currentState = (AbastecimientoState) state;
    	AbastecimientoState nextState = null;
    	AbastecimientoState bestState = currentState;

    	for (int step = 0; step < this.steps; step++) {
    		double temperature = computeTemperature(step);
    		if (temperature == 0.0) break;

    		while (nextState == null) {
    			Random rand = new Random();
    	    	int randomNum = rand.nextInt(5) + 1;

    			nextState = (AbastecimientoState) getNextState(currentState, randomNum);
    			if (nextState != null) {

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

    		}


    	}
    	StringBuffer s;
    	//goalState = new Successor(bestState);
    	//saSucesores.add(goalState);
		//ret.add(new Successor (s.toString(), newState));



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

    	switch (randomNum){
		case 0:									           //modificamos el estado mediante la asignaPeticion
			alGas = rand.nextInt(nGasos);			//Escoge las peticiones de una gasolinera aleatoria

			nPeticiones = currentState.gasolineras.get(alGas).getPeticiones().size();

			if (nPeticiones > 0) {
				alPeticion = rand.nextInt(nPeticiones);
				alCamion = rand.nextInt(nCamiones);
				x = new Pair<Integer, Integer> (alGas, alPeticion);

				if (asigned(nextState, x.makeString())) {
    	    		b = nextState.asignaPeticion(alCamion, x, true);

    	    		if (b) s.append("asign petition, truck " + alCamion + " petition (" + x.geta() + "," + x.getb() + ")");
				}
			}

			break;

		case 1:														//Modificamos el estado mediante intercambiaPeticiones
			alCamion1 = rand.nextInt(nCamiones);
			alCamion2 = rand.nextInt(nCamiones);

			if (alCamion1 != alCamion2) {
				sizeC = currentState.getAsignaciones().get(alCamion1).size();
				sizeC1 = currentState.getAsignaciones().get(alCamion2).size();
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

			sizeC = currentState.getAsignaciones().get(alCamion).size();
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
				sizeC1 = currentState.getAsignaciones().get(alCamion1).size();
				if (sizeC1 > 0) {

					alPn = rand.nextInt(sizeC1);

					b = nextState.cambiaPeticion(alPn, alCamion1, alCamion2);

					if (b) s.append("change petition" + alPn + ", from truck " + alCamion1 + " to truck " + alCamion2);
				}

			}
			break;

		case 4:														//Modificamos el estado mediante cambioPeticionNoAsig

			alCamion = rand.nextInt(nCamiones);


			alGas = rand.nextInt(nGasos);

			sizeC = currentState.gasolineras.get(alGas).getPeticiones().size();
			sizeC1 = currentState.getAsignaciones().get(alCamion).size();

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
