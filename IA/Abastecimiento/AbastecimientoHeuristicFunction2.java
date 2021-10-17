package Abastecimiento;

// Aquest valora els costos.
public class AbastecimientoHeuristicFunction2 extends AbstractHeuristic {

	public double getHeuristicValue (Object state) {
    	AbastecimientoState estado = (AbastecimientoState) state;
    	return computeProfits(estado);
	}
}