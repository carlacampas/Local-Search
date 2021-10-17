package Abastecimiento;

// Aquest valora els costos.
public class AbastecimientoHeuristicFunction1 extends AbstractHeuristic {

	@Override
	public double getHeuristicValue (Object state) {
    	AbastecimientoState estado = (AbastecimientoState) state;
    	return computeProfits(estado) - computePenalisations(estado);
	}

}