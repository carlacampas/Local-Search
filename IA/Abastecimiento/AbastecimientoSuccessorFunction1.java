package Abastecimiento;

// IMPORTS.
import java.util.*;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class AbastecimientoSuccessorFunction1 implements SuccessorFunction{
	public boolean assignacionsContains (@SuppressWarnings("exports") ArrayList<ArrayList<Peticion>> assig, Pair <Integer, Integer> p) {
		for (ArrayList <Peticion> a : assig) {
			for (Peticion pet : a) {
				if (p.equals(pet.get())) return true;
			}
		}
		return false;
	}

    public List getSuccessors (Object state) {
    	System.out.print ("-");
    	ArrayList <Successor> ret = new ArrayList<>();
    	AbastecimientoState as = new AbastecimientoState ((AbastecimientoState) state);

    	int ncen = as.centrosDistribucion.size();
    	int ngas = as.gasolineras.size();

    	for (int i = 0; i < ncen; i++) {
    		int m = as.getAsignaciones().get(i).size();
    		
    		// asigna peticiones no asignadas -- FUNCIONA
    		for (String s : as.getPeticionesDesatendidas()) {
    			Pair <Integer, Integer> p = new Pair <Integer, Integer> ();
    			int a = Integer.parseInt(p.fromStringA(s));
    			int b = Integer.parseInt(p.fromStringB(s));

    			p.seta(a);
    			p.setb(b);

    			AbastecimientoState newState = new AbastecimientoState (as);
				if (newState.asignaPeticion(i, p, true)) {
					StringBuffer sb = new StringBuffer ();
        			sb.append("add petition");
        			ret.add(new Successor (sb.toString(), newState));
				}
				
    			for (int l = 0; l < m; l++) {
    				newState = new AbastecimientoState (as);
					if (newState.cambioPeticionNoAsig (l, i, p)) {
        				StringBuffer sb = new StringBuffer ();
        				sb.append("cambio peticion no assig");
        				ret.add(new Successor (sb.toString(), newState));
					}
				}
    		}

    		// mover paquetes dentro del camion
    		for (int j = 0; j < m; j++) {
    			for (int k = j+1; k < m; k++) {
    				AbastecimientoState newState = new AbastecimientoState (as);
    				if (newState.intercambioOrden (j, k, i)) {
	    				StringBuffer s = new StringBuffer ();
	    				s.append("swap petition order, truck " + i + " petition " + j + " changed with petition " + k);
	    				ret.add(new Successor (s.toString(), newState));
    				}
    			}
    		}

    		// mover paquetes con los que ya estan asignados -- FUNCIONA
    		for (int j = i + 1; j < ncen; j++) {
    			for (int k = 0; k < as.getAsignaciones().get(i).size(); k++) {
    				for (int l = 0; l < as.getAsignaciones().get(j).size(); l++) {
    					AbastecimientoState newState = new AbastecimientoState (as);
    					if (newState.intercambiaPeticiones (k, l, i, j)) {
	        				StringBuffer s = new StringBuffer ();
	        				s.append("swap petition, truck " + i + " petition " + k + " with petition in truck " + j + " petition " + l);
	        				ret.add(new Successor (s.toString(), newState));
    					}
	    			}
    			}
    		}

    		// cambia peticiones
    		for (int j = i + 1; j < ncen; j++) {
    			for (int k = 0; k < as.getAsignaciones().get(j).size(); k++) {
    				AbastecimientoState newState = new AbastecimientoState (as);
    				if (newState.cambiaPeticion(k, j, i)) {
    					StringBuffer s = new StringBuffer ();
        				s.append("swap petition " + k + " from truck " + j + " to truck " + i);
        				Successor suc = new Successor (s.toString(), newState);
        				ret.add(suc);
    				}
    			}
    		}
    	}

        return ret;
    }
}
