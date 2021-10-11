package Abastecimiento;

// IMPORTS.
import java.util.*;

import IA.Gasolina.Gasolineras;
import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Distribucion;
import IA.Gasolina.Gasolinera;

public class AbastecimientoState {
    // ATRIBUTOS.
    final static int maxDist = 640;

    Gasolineras gasolineras;
    CentrosDistribucion centrosDistibucion;

    private ArrayList<ArrayList<Asignacion>> asignaciones;
    private ArrayList <Integer> distancias;

    // CONSTRUCTORS.
    public AbastecimientoState (Gasolineras gasolineras, CentrosDistribucion centrosDistribucion){
        this.gasolineras = gasolineras;
        this.centrosDistibucion = centrosDistribucion;

        this.asignaciones = new ArrayList <> (centrosDistribucion.size());
        this.distancias = new ArrayList <> (centrosDistribucion.size());

        for (int i=0; i<centrosDistribucion.size(); i++){
            this.distancias.set(i, maxDist);
            this.asignaciones.set (i, new ArrayList <>());
        }
    }

    public AbastecimientoState (ArrayList <ArrayList<Asignacion>> asignaciones, ArrayList <Integer> distancias,
                                Gasolineras gasolineras, CentrosDistribucion centrosDistribucion) {
        this.asignaciones = asignaciones;
        this.distancias = distancias;
        this.gasolineras = gasolineras;
        this.centrosDistibucion = centrosDistribucion;
    }

    // SETTERS.
    public void setAssigments(ArrayList <ArrayList<Asignacion>> asignaciones) {
        this.asignaciones = asignaciones;
    }

    public void setDistances (ArrayList <Integer> distancias) {
        this.distancias = distancias;
    }

    // GETTERS.
    public ArrayList<ArrayList<Asignacion>> getAsignaciones() {
        return asignaciones;
    }

    public ArrayList<Integer> getDistancias () {
        return distancias;
    }

    // OPERADORS.
    // Las peticiones seran identificadas asi: Pair <Integer, Integer> p = (id peticion, id gasolinera)
    // Los camiones seran identificados con su propio Id Integer
    public Integer calcularDistancias (int c, Pair <Integer, Integer> p) {
    	ArrayList <Asignacion> cAssig = asignaciones.get(c);
    	
    	int n = cAssig.size();
    	
    	Distribucion d = centrosDistibucion.get(c);
    	Gasolinera g1 = gasolineras.get(p.a);
		
		Pair <Integer, Integer> coord1 = new Pair <Integer, Integer> (d.getCoordX(), d.getCoordY());
		Pair <Integer, Integer> coord3 = new Pair <Integer, Integer> (g1.getCoordX(), g1.getCoordY());
	
		if (n > 0) {
			Asignacion last = cAssig.get(n - 1);
	    	if (last.secondIsEmpty()) {
	    		Gasolinera g = gasolineras.get(last.first().a);
	    		Pair <Integer, Integer> coord2 = new Pair <Integer, Integer> (g.getCoordX(), g.getCoordY());
	    		
	    		int prevD = calcularDistancia (coord1, coord2);
	    		int newD = prevD + calcularDistancia (coord2, coord3) + calcularDistancia (coord3, coord1);
	    	
	    		return distancias.get(c) + prevD*2 - newD;
	    	}
		}
    	
    	return distancias.get(c) - calcularDistancia(coord1, coord3)*2;
    }
    
    public boolean assignaPeticion (int c, Pair <Integer, Integer> p) {
    	int dist = calcularDistancias(c, p);
    	if (dist > 0) {
	    	ArrayList <Asignacion> cAssig = asignaciones.get(c);
	    	
	    	int n = cAssig.size();
	    	if (n == 0) cAssig.add(new Asignacion (p));
	    	else {
	    		Asignacion last = cAssig.get(n - 1);
	    		if (last.secondIsEmpty()) {
	    			last.setSecond(p);
	    			cAssig.set(n-1, last);
	    		}
	        	else {
	        		last = new Asignacion (p);
	        		cAssig.add(last);
	        	}
	    	}
	    	
	    	asignaciones.set(c, cAssig);
	    	return true;
    	}
    	return false;
	} 
    /*
    * Pre: la petición p está asignada al camión c y la petición p1 al camion c1
    * Post: La asignación de las peticiones se invierte.
    * */
    public void intercambiaPeticiones (Integer p, Integer p1, int c, int c1){
        Asignacion a = asignaciones.get(c).get(p);
        Asignacion b = asignaciones.get(c1).get(p1);

        asignaciones.get(c).set(p1, b);
        asignaciones.get(c1).set(p, a);
    }
    /*
    * Pre: Both p y p1 son peticiones asignadas al camión c
    * Post: El orden en que estaban asignadas p y p1 se invierte
    * */
    public void intercambioOrden (Integer p, Integer p1, int c) {
        Asignacion a = asignaciones.get(c).get(p);
        Asignacion b = asignaciones.get(c).get(p1);

        asignaciones.get(c).set(p1, a);
        asignaciones.get(c).set(p, b);
    }
    /*
    * Pre: La petición p estaba asignada al camion c
    * Post: La petición p deja de estar asignada al camion c y pasa a formar parte de las asignaciones de c1
    * */
    public void cambiaPeticion (Integer p, int c, int c1) {
        int n = asignaciones.get(c1).size();
        Asignacion a = asignaciones.get(c).get(p);

        asignaciones.get(c).remove(p);
        asignaciones.get(c1).set(n-1, a);
    }

    // INITIAL SOLUTION.
    public int calcularDistancia (Pair <Integer, Integer> coord1, Pair <Integer, Integer> coord2) {
    	return Math.abs (coord1.a - coord2.a) + Math.abs (coord1.b - coord2.b);
    }
    
    // Genera solució inicial repartint paquets equitativament entre tots els paquets de forma aleatoria.
    public void generateInitialSolution1 () {
    	Random rand = new Random();
    	int n = centrosDistibucion.size();
    	
    	for (int i=0; i<gasolineras.size(); i++) {
    		ArrayList <Integer> peticiones = gasolineras.get(i).getPeticiones();
    		for (Integer p : peticiones) {
    			Pair <Integer, Integer> pet = new Pair <Integer, Integer> (i, p);
    			
    			boolean[] visited = new boolean[n];
    			int c = rand.nextInt(n);
    			while (!visited[c] && !assignaPeticion(c, pet)) {
    				visited[c] = true;
    				c = rand.nextInt(n);
    			}
    			
    			assignaPeticion (c, new Pair <Integer, Integer> (i, p));
    		}
    	}
    }

    // Genera solució inicial repartint paquets equitativament entre tots els paquets amb ponderacions dels costos i
    // beneficis.
    public void generateInitialSolution2 () {}

    // Genera solució inicial repartint paquets...
    public void generateInitialSolution3 () {}
}