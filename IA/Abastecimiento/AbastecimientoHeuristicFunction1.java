package Abastecimiento;

/* Heurística que aplica poca penalización a las peticiones de las gasolineras sin atender. 
 * La estrategia resultante es atender primero a las nuevas, aunque también tiene en cuenta las
 * peticiones pendientes.
 * */
public class AbastecimientoHeuristicFunction1 extends AbstractHeuristic {

	@Override
	public double getHeuristicValue (Object s) {
    	AbastecimientoState estado = (AbastecimientoState) s;
    	//estado.print_state();
    	//System.out.println ("-------------------------------------");
    	//System.out.println (computeProfits(estado));
    	//System.out.println (computePenalisations(2, estado));
    	return -1*(computeProfits(estado) - computePenalisations(2, estado));
	}
}