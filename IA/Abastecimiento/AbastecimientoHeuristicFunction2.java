package Abastecimiento;

/* Heurística que aplica bastante penalización a las peticiones de las gasolineras sin atender. 
 * La estrategia resultante es establecer una mayor prioridad a las peticiones pendientes
 * y evitar que se acumulen.
 * */
public class AbastecimientoHeuristicFunction2 extends AbstractHeuristic {

	@Override
	public double getHeuristicValue (Object s) {
    	AbastecimientoState estado = (AbastecimientoState) s;
    	return -1*(computeProfits(estado) - computePenalisations(4, estado));
	}
}