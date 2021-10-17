package Abastecimiento;

import java.util.*;

import aima.search.framework.GoalTest;

public class AbastecimientoGoalTest implements GoalTest {
    public boolean isGoalState(Object aState){
    	AbastecimientoState estado = (AbastecimientoState) aState;
    	if (!checkMaximumDistance(estado)) return false;
    	if (!checkMaximumTrips(estado)) return false;
        return true;
    }
    
    private boolean checkMaximumDistance(AbastecimientoState estado) {
    	for (Integer d : estado.getDistancias()) {
    		if (d < 0) return false;
    	}
    	return true;
    }
    
    private boolean checkMaximumTrips(AbastecimientoState estado) {
    	for (ArrayList<Peticion> asignacionesCisterna : estado.getAsignaciones()) {
    		if (asignacionesCisterna.size() > 5) return false;
    	}
    	return true;
    }
}