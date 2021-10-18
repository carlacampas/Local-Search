package Abastecimiento;

import java.util.Scanner;

import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Gasolineras;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import aima.search.framework.SearchAgent;

public class Main {
	public static void AbastecimientoHillClimbingHeuristic1 (AbastecimientoState state) {
		System.out.println ("Abastecimiento Hill Climbing Heuristic 1");
		try {
			long time = System.currentTimeMillis();
			Problem problem = new Problem (state, new AbastecimientoSuccessorFunction1(), new AbastecimientoGoalTest(), new AbastecimientoHeuristicFunction1());
			Search search = new HillClimbingSearch();
			SearchAgent agent = new SearchAgent (problem, search);
			
			AbastecimientoState newState = (AbastecimientoState) search.getGoalState();
			time = System.currentTimeMillis() - time;
			
			//AbastecimientoGoalTest test = new AbastecimientoGoalTest();
			
			System.out.println ("Solution using Hill Climbing + Heuristic1: ");
			System.out.println (agent.getActions());
			System.out.println(agent.getInstrumentation());
			
			System.out.println ("time to generate solution " + time + " ms");
			
			AbstractHeuristic ah = new AbstractHeuristic ();
			System.out.println ("solution benefit " + ah.computeProfits(newState));
			
			System.out.println();
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static void AbastecimientoHillClimbingHeuristic2 (AbastecimientoState state) {
		System.out.println ("Abastecimiento Hill Climbing Heuristic 2");
		try {
			long time = System.currentTimeMillis();
			Problem problem = new Problem (state, new AbastecimientoSuccessorFunction1(), new AbastecimientoGoalTest(), new AbastecimientoHeuristicFunction2());
			Search search = new HillClimbingSearch();
			SearchAgent agent = new SearchAgent (problem, search);
			
			AbastecimientoState newState = (AbastecimientoState) search.getGoalState();
			time = System.currentTimeMillis() - time;
			
			//AbastecimientoGoalTest test = new AbastecimientoGoalTest();
			
			System.out.println ("Solution using Hill Climbing + Heuristic1: ");
			System.out.println (agent.getActions());
			System.out.println(agent.getInstrumentation());
			
			System.out.println ("time to generate solution " + time + " ms");
			
			AbstractHeuristic ah = new AbstractHeuristic();
			System.out.println ("solution benefit " + ah.computeProfits(newState));
			
			System.out.println();
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static void AbastecimientoSimulatedAnnealingHeuristic1 (AbastecimientoState state) {
		System.out.println ("Abastecimiento Simulated Annealing Heuristic 1");
		try {
			long time = System.currentTimeMillis();
			Problem problem = new Problem (state, new AbastecimientoSuccessorFunction2(), new AbastecimientoGoalTest(), new AbastecimientoHeuristicFunction1());
			Search search = new SimulatedAnnealingSearch();
			SearchAgent agent = new SearchAgent (problem, search);
			
			AbastecimientoState newState = (AbastecimientoState) search.getGoalState();
			time = System.currentTimeMillis() - time;
			
			//AbastecimientoGoalTest test = new AbastecimientoGoalTest();
			
			System.out.println ("Solution using Hill Climbing + Heuristic1: ");
			System.out.println (agent.getActions());
			System.out.println(agent.getInstrumentation());
			
			System.out.println ("time to generate solution " + time + " ms");
			//System.out.println ("solution benefit " + newState.getBenefit());
			
			System.out.println();
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static void AbastecimientoSimulatedAnnealingHeuristic2 (AbastecimientoState state) {
		System.out.println ("Abastecimiento Simulated Annealing Heuristic 2");
		try {
			long time = System.currentTimeMillis();
			Problem problem = new Problem (state, new AbastecimientoSuccessorFunction2(), new AbastecimientoGoalTest(), new AbastecimientoHeuristicFunction2());
			Search search = new SimulatedAnnealingSearch();
			SearchAgent agent = new SearchAgent (problem, search);
			
			AbastecimientoState newState = (AbastecimientoState) search.getGoalState();
			time = System.currentTimeMillis() - time;
			
			//AbastecimientoGoalTest test = new AbastecimientoGoalTest();
			
			System.out.println ("Solution using Hill Climbing + Heuristic1: ");
			System.out.println (agent.getActions());
			System.out.println(agent.getInstrumentation());
			
			System.out.println ("time to generate solution " + time + " ms");
			//System.out.println ("solution benefit " + newState.getBenefit());
			
			System.out.println();
			
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
		System.out.println("OPCIONES: ");
		System.out.println("run -- ejecutar busqueda");
		System.out.println("ngas -- cambiar numero gasolineras");
		System.out.println("ncen -- cambiar numero de centros de distribucion");
		System.out.println("mult -- cambiar multiplicidad de centros de distribucion");
		System.out.println("costekm -- cambiar coste de recorrer un kilometro");
		System.out.println("horas -- cambiar horas que puede trabajar un camion (0 <= h <= 24");
		System.out.println("algo -- cambiar algoritmo de busqueda (hc == hill climbing, sa == simulated annealing)");
		System.out.println("inicial -- cambiar solucion inicial (0 - randomizada, 1 - ponderada, 2 - ponderada igual por camiones)");
		System.out.println("heuristica -- cambiar heuristica (0 - , 1 -)");
		System.out.println("print -- ver opciones escogidas");
		line();
		System.out.println();
	}
	
	public static void init () {
		line();
		System.out.println("VALORES POR DEFECTO: ");
		System.out.println("num. gasolineras: 100");
		System.out.println("num. centros distribucion: 10");
		System.out.println("multiplicidad (camiones por centro distribucion): 1");
		System.out.println("algoritmo de busqueda: Hill Climbing");
		System.out.println("heuristica: 0");
		System.out.println("estado inicial: 0 (randomizado)");
		line();
		opts();
	}
	
    public static void main(String[] args) {
    	//inicializacion gasolinera
    	int ngas = 100, seedGas = 1234;
    	
    	//inicializacion centros distribucion
    	int ncen = 10, mult = 1, seedCen = 1234;
    	
    	// valores para experimentar
    	int costeKm = 2;
    	int horasTrabajo = 8;
    	
    	boolean hillClibming = true;
    	int initialSolution = 0;
    	boolean firstHeuristic = true;

    	init();
    	
    	Scanner sc = new Scanner(System.in);
    	
    	boolean executed = false;
    	
    	while (!executed) {
    		while (!sc.hasNext());
    		String cmd = sc.next(); 
    		cmd.trim(); cmd.toLowerCase();
    		
    		switch (cmd) {
    			case "run":
    				Gasolineras gasolineras = new Gasolineras (ngas, seedGas);
    		    	CentrosDistribucion centrosDistrbucion = new CentrosDistribucion (ncen, mult, seedCen);
    		  
    		    	AbastecimientoState as = new AbastecimientoState (gasolineras, centrosDistrbucion);
    		    	
    		    	switch (initialSolution) {
    		    		case 0:
    		    			as.generateInitialSolution1();
    		    			break;
    		    		case 1:
    		    			as.generateInitialSolution2();
    		    			break;
    		    		case 2:
    		    			as.generateInitialSolution3();
    		    			break;
    		    	}
    		    	
    		    	if (hillClibming && firstHeuristic) AbastecimientoHillClimbingHeuristic1(as);
    		    	else if (hillClibming && !firstHeuristic) AbastecimientoHillClimbingHeuristic2(as);
    		    	else if (!hillClibming && firstHeuristic) AbastecimientoSimulatedAnnealingHeuristic1(as);
    		    	else if (!hillClibming && !firstHeuristic) AbastecimientoSimulatedAnnealingHeuristic2(as);
    		    	
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
    				
    				if (alg == "hc") hillClibming = true;
    				else if (alg == "sa") hillClibming = false;
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
    			case "heurisitca":
    				int heuristics = sc.nextInt();

    				if (heuristics == 0) firstHeuristic = true;
    				if (heuristics == 1) firstHeuristic = false;
    				else {
    					firstHeuristic = true;
    					System.out.println ("please enter valid option");
        				opts();
    				}
    				
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