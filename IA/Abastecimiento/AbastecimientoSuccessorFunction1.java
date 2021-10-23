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
    	System.out.println("here");
    	ArrayList <Successor> ret = new ArrayList<>();
    	AbastecimientoState as = new AbastecimientoState ((AbastecimientoState) state);

    	int ncen = as.centrosDistribucion.size();
    	int ngas = as.gasolineras.size();

    	for (int i = 0; i < ncen; i++) {

    		// asigna peticiones no asignadas -- FUNCIONA
    		System.out.println ("SIZE IN METHOD " + as.getPeticionesDesatendidas().size());
    		for (String s : as.getPeticionesDesatendidas()) {
    			System.out.println ("desatendida " + s);
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
    		}

    		/*for (int j = 0; j < ngas; j++) {
    			for (int k = 0; k < as.gasolineras.get(j).getPeticiones().size(); k++) {
    				Pair <Integer, Integer> p = new Pair <Integer, Integer>(j, k);
    				if (assignacionsContains (as, p)) {
    					AbastecimientoState newState = new AbastecimientoState (as);
	    				if (newState.asignaPeticion(i, p, true)) {
	    					StringBuffer s = new StringBuffer ();
		        			s.append("add petition gas station: " + j + " petition " + k + " to truck " + i);
		        			ret.add(new Successor (s.toString(), newState));
	    				}
    				}
    			}
    		}*/

    		// mover paquetes dentro del camion
    		int m = as.getAsignaciones().get(i).size();
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

    		// mover paquetes con los que no estan asignados -- FUNCIONA
    		System.out.println ("SIZE IN METHOD CASSIG " + as.getPeticionesDesatendidas().size());
    		for (String s : as.getPeticionesDesatendidas()) {
    			System.out.println ("desatendida " + s);
    			Pair <Integer, Integer> p = new Pair <Integer, Integer> ();
    			int a = Integer.parseInt(p.fromStringA(s));
    			int b = Integer.parseInt(p.fromStringB(s));

    			p.seta(a);
    			p.setb(b);

    			AbastecimientoState newState = new AbastecimientoState (as);
    			for (int l = 0; l < m; l++) {
					if (newState.cambioPeticionNoAsig (l, i, p)) {
        				StringBuffer sb = new StringBuffer ();
        				sb.append("cambio peticion no assig");
        				ret.add(new Successor (sb.toString(), newState));
					}
				}
    		}

    		/*for (int j = 0; j < ngas; j++) {
    			for (int k = 0; k < as.gasolineras.get(j).getPeticiones().size(); k++) {
    				Pair <Integer, Integer> p = new Pair <Integer, Integer>(j, k);
    				if (!assignacionsContains (as.getAsignaciones(), p)) {
    					for (int l = 0; l < m; l++) {
	    					AbastecimientoState newState = new AbastecimientoState (as);
	    					if (newState.cambioPeticionNoAsig (l, i, p)) {
		        				StringBuffer s = new StringBuffer ();
		        				s.append("swap petition order, truck " + i + " petition (" + j + "," + k + ")" +
		        							" changed with petition pos " + l);
		        				ret.add(new Successor (s.toString(), newState));
	    					}
    					}
    				}
    			}
    		}


    		/* mover paquetes con los que ya estan asignados -- FUNCIONA
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

    		//cambia peticiones
    	/*	for (int j = i + 1; j < ncen; j++) {
    			for (int k = 0; k < as.getAsignaciones().get(j).size(); k++) {
    				AbastecimientoState newState = new AbastecimientoState (as);
    				if (newState.cambiaPeticion(k, j, i)) {
    					StringBuffer s = new StringBuffer ();
        				s.append("swap petition " + k + " from truck " + j + " to truck " + i);
        				Successor suc = new Successor (s.toString(), newState);
        				ret.add(suc);
    				}
    			}
    		}*/
    	}

        return ret;
    }
}
