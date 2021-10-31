package Abastecimiento;

import java.util.*;

import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Gasolinera;
import IA.Gasolina.Gasolineras;
import IA.Gasolina.Distribucion;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import aima.search.framework.SearchAgent;

public class Main {
	private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }
	
	private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }
	
	private static void printFinalState (AbastecimientoState newState, long time, boolean hc, SearchAgent agent) {
		System.out.println();
		System.out.println("FINAL STATE: ");
		newState.print_state();
		line();

		System.out.println ("SOLUTION STATS: ");
		
		if (hc) {
			printActions (agent.getActions());
			printInstrumentation(agent.getInstrumentation());
			line();
		}
		
		System.out.println ("time to generate solution " + time + " ms");
		System.out.println ("solution benefit:         " + newState.getBenefit());
		System.out.println ("km:                       " + newState.getDistTraveled());
		System.out.println ("precio:                   " + newState.getPrecioEnDepositos());
		line();
		
		System.out.println();
	}
	
	private static void printInitialState (AbastecimientoState state) {
		System.out.println("INITIAL STATE: ");
		state.print_state();
		line();
	}
	
	public static void AbastecimientoHillClimbingHeuristic1 (AbastecimientoState state) {
		System.out.println ("Solution using Hill Climbing + Heuristic1: ");
		try {
			printInitialState(state);
			long time = System.currentTimeMillis();
			
			Problem problem = new Problem (state, new AbastecimientoSuccessorFunction1(), new AbastecimientoGoalTest(), new AbastecimientoHeuristicFunction1());
			Search search = new HillClimbingSearch();
			SearchAgent agent = new SearchAgent (problem, search);
			
			AbastecimientoState newState = (AbastecimientoState) search.getGoalState();
			time = System.currentTimeMillis() - time;
			
			printFinalState (newState, time, true, agent);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static void AbastecimientoHillClimbingHeuristic2 (AbastecimientoState state) {
		System.out.println ("Solution using Hill Climbing + Heuristic 2");
		try {
			printInitialState(state);
			
			long time = System.currentTimeMillis();
			Problem problem = new Problem (state, new AbastecimientoSuccessorFunction1(), new AbastecimientoGoalTest(), new AbastecimientoHeuristicFunction2());
			Search search = new HillClimbingSearch();
			SearchAgent agent = new SearchAgent (problem, search);
			
			AbastecimientoState newState = (AbastecimientoState) search.getGoalState();
			time = System.currentTimeMillis() - time;
			
			printFinalState (newState, time, true, agent);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static void AbastecimientoSimulatedAnnealingHeuristic1 (AbastecimientoState state) {
		System.out.println ("Solution using Simulated Annealing + Heuristic 1");
		try {
			printInitialState(state);
			
			long time = System.currentTimeMillis();
			Problem problem = new Problem (state, new AbastecimientoSuccessorFunction2(), new AbastecimientoGoalTest(), new AbastecimientoHeuristicFunction1());
			Search search = new SimulatedAnnealingSearch(150000, 10, 100, 0.00001);
			SearchAgent agent = new SearchAgent (problem, search);
			
			AbastecimientoState newState = (AbastecimientoState) search.getGoalState();
			time = System.currentTimeMillis() - time;
			
			printFinalState (newState, time, false, agent);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static void AbastecimientoSimulatedAnnealingHeuristic2 (AbastecimientoState state) {
		System.out.println ("Abastecimiento Simulated Annealing Heuristic 2");
		try {
			printInitialState(state);
			
			long time = System.currentTimeMillis();
			Problem problem = new Problem (state, new AbastecimientoSuccessorFunction2(), new AbastecimientoGoalTest(), new AbastecimientoHeuristicFunction2());
			Search search = new SimulatedAnnealingSearch(300000, 10, 5, 0.01);
			SearchAgent agent = new SearchAgent (problem, search);
			
			AbastecimientoState newState = (AbastecimientoState) search.getGoalState();
			time = System.currentTimeMillis() - time;
			
			printFinalState (newState, time, false, agent);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static void line () {
		System.out.println("----------------------------------------------");
	}
	
	public static void opts () {
		System.out.println ("OPCIONES: ");
		System.out.println ();
		System.out.println ("run        --  ejecutar busqueda");
		System.out.println ("ngas       --  cambiar numero gasolineras");
		System.out.println ("ncen       --  cambiar numero de centros de distribucion");
		System.out.println ("mult       --  cambiar multiplicidad de centros de distribucion");
		System.out.println ("costekm    --  cambiar coste de recorrer un kilometro");
		System.out.println ("horas      --  cambiar horas que puede trabajar un camion (0 <= h <= 24");
		System.out.println ("algo       --  cambiar algoritmo de busqueda (hc == hill climbing, sa == simulated annealing)");
		System.out.println ("inicial    --  cambiar solucion inicial (0 - randomizada, 1 - ponderada");
		System.out.println ("heuristica --	cambiar heuristica (0 - penalización 2^x, 1 - penalización 4^x)");
		System.out.println ("seed       --  cambiar la semilla aleatoria");
		System.out.println ("print      --  ver opciones escogidas");
		System.out.println ("opts       --  ver opciones");
		line();
		System.out.println();
	}
	
	public static void init (int ngas, int ncen, int mult, boolean ini, boolean heur, int estIni, int seed,
			int horas, int costeKm) {
		line();
		System.out.println("VALORES POR DEFECTO: ");
		System.out.println();
		
		System.out.println("número gasolineras: " + ngas);
		System.out.println("número centros de distribucion: " + ncen);
		System.out.println("multiplicidad (camiones por centro distribucion): " + mult);
		
		if (ini) System.out.println("algoritmo de busqueda: hill climbing");
		else System.out.println("algoritmo de busqueda: simulated annealing");
		
		if(heur) System.out.println("heuristica: 1");
		else System.out.println("heuristica: 2");
		
		if (estIni == 0) System.out.println("estado inicial: randomizado");
		else System.out.println("estado inicial: ponderado");
		
		System.out.println("seed " + seed);
		
		System.out.println ("horas de trabajo: " + horas + " (camiones siempre van a 80km/h");
		System.out.println ("coste de viajar un kilómetro: " + costeKm);
		
		line();
	}
	
    public static void main(String[] args) {
    	// VALORES POR DEFECTO (USADOS EN LOS TESTS
    	int ngas 				= 100;
    	int ncen 				= 10;
    	int mult 				= 1;
    	int seed 				= 1234;
    	int costeKm 			= 2;
    	int horasTrabajo 		= 8;
    	int valorDeposito 		= 1000;
    	int initialSolution 	= 0;
    	boolean firstHeuristic 	= true;
    	boolean hillClibming 	= true;

    	init(ngas, ncen, mult, hillClibming, firstHeuristic, initialSolution, seed, horasTrabajo, costeKm);
    	opts();
    	
    	Scanner sc = new Scanner(System.in);
    	
    	boolean executed = false;
    	
    	while (!executed) {
    		while (!sc.hasNext());
    		String cmd = sc.next(); 
    		cmd.trim(); cmd.toLowerCase();
    		
    		switch (cmd) {
    			case "run":
    				Gasolineras gasolineras = new Gasolineras (ngas, seed);
    		    	CentrosDistribucion centrosDistrbucion = new CentrosDistribucion (ncen, mult, seed);
    		    	
    		    	AbastecimientoState as = new AbastecimientoState (gasolineras, centrosDistrbucion);
    		    	as.setMaxDist(horasTrabajo*80);
    		    	as.setCosteKilometro(costeKm);
    		    	as.setValorDeposito(valorDeposito);
    		    	
    		    	switch (initialSolution) {
    		    		case 0:
    		    			as.generateInitialSolution1();
    		    			break;
    		    		case 1:
    		    			as.generateInitialSolution2();
    		    			break;
    		    	}
    		    	
    		    	if 		( hillClibming &&  firstHeuristic)	AbastecimientoHillClimbingHeuristic1(as);
    		    	else if ( hillClibming && !firstHeuristic)	AbastecimientoHillClimbingHeuristic2(as);
    		    	else if (!hillClibming &&  firstHeuristic)	AbastecimientoSimulatedAnnealingHeuristic1(as);
    		    	else if (!hillClibming && !firstHeuristic)	AbastecimientoSimulatedAnnealingHeuristic2(as);
    		    	
    		    	executed = true;
    				break;
    				
    			case "ngas":
    				ngas = sc.nextInt();
    				break;
    				
    			case "ncen":
    				ncen = sc.nextInt();
    				break;
    				
    			case "mult":
    				mult = sc.nextInt();
    				break;
    				
    			case "costekm":
    				costeKm = sc.nextInt();
    				break;
    				
    			case "horas":
    				horasTrabajo = sc.nextInt();
    				
    				if (horasTrabajo < 0 || horasTrabajo > 24) {
    					horasTrabajo = 8;
    					System.out.println ("please enter valid option");
        				opts();
    				}
    				
    				break;
    				
    			case "algo":
    				String alg = sc.next(); 
    				alg.trim(); alg.toLowerCase();
    				
    				if (alg.equals("hc")) hillClibming = true;
    				else if (alg.equals("sa")) hillClibming = false;
    				else {
    					hillClibming = true;
    					System.out.println ("please enter valid option");
        				opts();
    				}
   
    				break;
    				
    			case "inicial":
    				initialSolution = sc.nextInt();
    				
    				if (initialSolution < 0 || initialSolution > 2) {
    					initialSolution = 0;
    					System.out.println ("please enter valid option");
        				opts();
    				}
    				
    				break;
    				
    			case "heuristica":
    				int heuristics = sc.nextInt();

    				if (heuristics == 0) firstHeuristic = true;
    				if (heuristics == 1) firstHeuristic = false;
    				else {
    					firstHeuristic = true;
    					System.out.println ("please enter valid option");
        				opts();
    				}
    				
    				break;
    				
    			case "seed":
    				seed = sc.nextInt();
    				break;
    				
    			case "print":
    				init(ngas, ncen, mult, hillClibming, firstHeuristic, initialSolution, seed, horasTrabajo, costeKm);
    				break;
    				
    			case "opts":
    				opts();
    				break;
    				
    			default:
    				System.out.println ("please enter valid option");
    				opts();
    				break;
    		}
    	}

    	sc.close();
    }
}