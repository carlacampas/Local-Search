package Abastecimiento;

/* Heurística que aplica bastante penalización a las peticiones de las gasolineras sin atender. 
 * La estrategia resultante es establecer una mayor prioridad a las peticiones pendientes
 * y evitar que se acumulen.
 * */
public class AbastecimientoHeuristicFunction2 extends AbstractHeuristic {

	@Override
	public double getHeuristicValue (Object state) {
    	AbastecimientoState estado = (AbastecimientoState) state;
    	return computeProfits(estado) - computePenalisations(4);
	}
}