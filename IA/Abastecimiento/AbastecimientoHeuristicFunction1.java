package Abastecimiento;

// Aquest valora els costos.
public class AbastecimientoHeuristicFunction1 extends AbstractHeuristic {

    final static int COSTE_KILOMETRO = 2;
    final static int VALOR_DEPOSITO = 1000;

	public double getHeuristicValue (Object state) {
    	AbastecimientoState estado = (AbastecimientoState) state;
    	return computeProfits(estado);
	}
}