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
    CentrosDistribucion centrosDistribucion;

    private ArrayList<ArrayList<Peticion>> asignaciones;
    private ArrayList <Integer> distancias;

    // CONSTRUCTORS.
    public AbastecimientoState (Gasolineras gasolineras, CentrosDistribucion centrosDistribucion){
        this.gasolineras = gasolineras;
        this.centrosDistribucion = centrosDistribucion;

        this.asignaciones = new ArrayList <> (centrosDistribucion.size());
        this.distancias = new ArrayList <> (centrosDistribucion.size());

        for (int i=0; i<centrosDistribucion.size(); i++){
            this.distancias.add(maxDist);
            this.asignaciones.add(new ArrayList <>());
        }
    }

    public AbastecimientoState (ArrayList<ArrayList<Peticion>> asignaciones, ArrayList <Integer> distancias,
                                Gasolineras gasolineras, CentrosDistribucion centrosDistribucion) {
        this.asignaciones = asignaciones;
        this.distancias = distancias;
        this.gasolineras = gasolineras;
        this.centrosDistribucion = centrosDistribucion;
    }

    // SETTERS.
    public void setAssigments(ArrayList<ArrayList<Peticion>> asignaciones) {
        this.asignaciones = asignaciones;
    }

    public void setDistances (ArrayList <Integer> distancias) {
        this.distancias = distancias;
    }

    // GETTERS.
    public ArrayList<ArrayList<Peticion>> getAsignaciones() {
        return asignaciones;
    }

    public ArrayList<Integer> getDistancias () {
        return distancias;
    }

    // OPERADORS.
    // Las peticiones seran identificadas asi: Pair <Integer, Integer> p = (id peticion, id gasolinera)
    // Los camiones seran identificados con su propio Id Integer
    public Integer calcularDistancias (int c, Pair <Integer, Integer> p) {
    	ArrayList <Peticion> cAssig = asignaciones.get(c);
    	
    	int n = cAssig.size();
    	
    	Distribucion d = centrosDistribucion.get(c);
    	Gasolinera g1 = gasolineras.get(p.a);
		
		Pair <Integer, Integer> coord1 = new Pair <Integer, Integer> (d.getCoordX(), d.getCoordY());
		Pair <Integer, Integer> coord3 = new Pair <Integer, Integer> (g1.getCoordX(), g1.getCoordY()); 
	
		if (n > 0) {
	    	if (n%2 == 1) {
	    		Peticion last = cAssig.get(n - 1);
	    		
	    		Gasolinera g = gasolineras.get(last.get().a);
	    		Pair <Integer, Integer> coord2 = new Pair <Integer, Integer> (g.getCoordX(), g.getCoordY());

	    		int prevD = calcularDistancia (coord1, coord2);
	    		int newD = prevD + calcularDistancia (coord2, coord3) + calcularDistancia (coord3, coord1);
	    	
	    		return distancias.get(c) + prevD*2 - newD;
	    	}
		}
    	
    	return distancias.get(c) - calcularDistancia(coord1, coord3)*2;
    }

    public Integer actualizaDistancia(Pair <Integer, Integer> oldP, Pair <Integer, Integer> newP, int c){
        Distribucion d = centrosDistribucion.get(c);
        Gasolinera gOld = gasolineras.get(oldP.a);
        Gasolinera gNew = gasolineras.get(newP.a);

        Pair <Integer, Integer> dCoord = new Pair <Integer, Integer> (d.getCoordX(), d.getCoordY());
        Pair <Integer, Integer> oldCoord = new Pair <Integer, Integer> (gOld.getCoordX(), gOld.getCoordY());
        Pair <Integer, Integer> newCoord = new Pair <Integer, Integer> (gNew.getCoordX(), gNew.getCoordY());

        int dcToOldC = calcularDistancia(dCoord, oldCoord);
        int dcToNewC = calcularDistancia(dCoord, newCoord);

        int n = asignaciones.get(c).size();
        int dAct;
        if (oldP.b%2 == 0) {
            if (n-1 == oldP.b){
                dAct = distancias.get(c) + 2*dcToOldC - 2*dcToNewC;
                distancias.set(c, dAct);
            }
            else {
                int idOld = asignaciones.get(c).indexOf(oldP);

                Peticion pMid = asignaciones.get(c).get(idOld+1);
                Gasolinera gMid = gasolineras.get(pMid.get().a);

                Pair <Integer, Integer> midCoord = new Pair <Integer, Integer> (gMid.getCoordX(), gMid.getCoordY());

                int dcToMidC = calcularDistancia(dCoord, midCoord);

                int dOld = dcToOldC + calcularDistancia(midCoord, oldCoord) + dcToMidC; //dcToMidC + calcularDistancia(midCoord, oldCoord) + dcToOldC;
                int dNew = dcToNewC + calcularDistancia(midCoord, newCoord) + dcToMidC;

                dAct = distancias.get(c) + dOld - dNew;
                distancias.set(c, dAct);
            }
        }
        else {
            int idOld = asignaciones.get(c).indexOf(oldP);

            Peticion pMid = asignaciones.get(c).get(idOld-1);
            Gasolinera gMid = gasolineras.get(pMid.get().a);

            Pair <Integer, Integer> midCoord = new Pair <Integer, Integer> (gMid.getCoordX(), gMid.getCoordY());

            int dcToMidC = calcularDistancia(dCoord, midCoord);

            int dOld = dcToMidC + calcularDistancia(midCoord, oldCoord) + dcToOldC;
            int dNew = dcToMidC + calcularDistancia(midCoord, newCoord) + dcToNewC;

            dAct = distancias.get(c) + dOld - dNew;
            distancias.set(c, dAct);
        }
        return dAct;
    }


    public boolean asignaPeticion (int c, Pair <Integer, Integer> p) {
        ArrayList <Peticion> cAssig = asignaciones.get(c);
        
        int dist = calcularDistancias(c, p);
        int n = cAssig.size();

        // control de restricciones de distancia y número de peticiones asignadas
    	if (dist > 0 && (n/2) < 5) {
	    	cAssig.add(new Peticion (p));
	    	asignaciones.set(c, cAssig);
	    	
	    	distancias.set(c, dist);
	    	return true;
    	}
    	return false;
	} 
    
    /*
    * Pre: la petición p está asignada al camión c y la petición p1 al camion c1
    * Post: La asignación de las peticiones se invierte.
    * */
    public void intercambiaPeticiones (Integer p, Integer p1, int c, int c1){
        Peticion a = asignaciones.get(c).get(p);
        Peticion b = asignaciones.get(c1).get(p1);

        asignaciones.get(c).set(p1, b);
        asignaciones.get(c1).set(p, a);

        actualizaDistancia(a.get(), b.get(), c);
        actualizaDistancia(b.get(), a.get(), c1);
    }
    
    /*
    * Pre: Both p y p1 son peticiones asignadas al camión c
    * Post: El orden en que estaban asignadas p y p1 se invierte
    * */
    public void intercambioOrden (Integer p, Integer p1, int c) {
        Peticion a = asignaciones.get(c).get(p);
        Peticion b = asignaciones.get(c).get(p1);

        asignaciones.get(c).set(p1, a);
        asignaciones.get(c).set(p, b);

        actualizaDistancia(a.get(), b.get(), c);
        actualizaDistancia(b.get(), a.get(), c);
    }
    /*
    * Pre: La petición p estaba asignada al camion c
    * Post: La petición p deja de estar asignada al camion c y pasa a formar parte de las asignaciones de c1
    * */
    public void cambiaPeticion (Integer p, int c, int c1) {
        Peticion a = asignaciones.get(c).get(p);

        asignaciones.get(c).remove(p);
        asignaciones.get(c1).add(a);

        int n = asignaciones.get(c).size();
        distancias.set(c, maxDist);
        if (n != 0) {
            for (int i = 0; i < n-1; i++){
                Peticion x = asignaciones.get(c).get(i);
                calcularDistancias(c, x.get());
            }
        }
    }

    public void cambioPeticionNoAsig (Integer p, int c, Pair <Integer, Integer> newP){

    }

    // INITIAL SOLUTION.
    public int calcularDistancia (Pair <Integer, Integer> coord1, Pair <Integer, Integer> coord2) {
    	return Math.abs (coord1.a - coord2.a) + Math.abs (coord1.b - coord2.b);
    }
    
    // Genera solució inicial repartint paquets equitativament entre tots els paquets de forma aleatoria.
    public void generateInitialSolution1 () {
    	Random rand = new Random();
    	int n = centrosDistribucion.size();
    	
    	for (int i=0; i<gasolineras.size(); i++) {
    		ArrayList <Integer> peticiones = gasolineras.get(i).getPeticiones();
    		for (int j = 0; j < peticiones.size(); j++) {
    			Pair <Integer, Integer> pet = new Pair <Integer, Integer> (i, j);
    			
    			boolean[] visited = new boolean[n];
    			int c = rand.nextInt(n);
    			while (!visited[c] && !asignaPeticion(c, pet)) {
    				visited[c] = true;
    				c = rand.nextInt(n);
    			}
    			
    			asignaPeticion (c, new Pair <Integer, Integer> (i, j));
    		}
    	}
    }
    
    // cambiar per crear ponderacio basada en costos
    SortedMap <Integer, ArrayList <Pair<Integer, Integer>>> organizarPeticiones (Pair <Integer, Integer> cCoord) {
    	SortedMap <Integer, ArrayList <Pair<Integer, Integer>>> pOrg = new TreeMap <Integer, ArrayList <Pair<Integer, Integer>>> ();
    	int i = 0;
    	for (Gasolinera g : gasolineras) {
    		Pair <Integer, Integer> gCoord = new Pair <Integer, Integer> (g.getCoordX(), g.getCoordY());
    		int d = calcularDistancia (cCoord, gCoord);
    		
    		ArrayList <Pair<Integer, Integer>> arr = pOrg.get(d);
    		int n = g.getPeticiones().size();
    		for (int j = 0; j<n; j++) {
    			Pair <Integer, Integer> p = new Pair <Integer, Integer> (i, j);
    			arr.add(p);
    		}
    		pOrg.put(d, arr);
    		i++;
    	}
    	return pOrg;
    }
    
    // Genera solució inicial repartint paquets equitativament entre tots els camions amb ponderacions dels costos i
    // beneficis. Maximizar distancies en tots els camions.
    public void generateInitialSolution2 () {
    	Map <Integer, Integer> used = new HashMap <Integer, Integer> ();
    	Set <Pair <Integer, Integer>> coordVisited = new HashSet <Pair <Integer, Integer>> ();
    	
    	int n = centrosDistribucion.size();
    	for (int i=0; i<n; i++) {
    		Distribucion cd = centrosDistribucion.get(i);
    		Pair <Integer, Integer> coords = new Pair <Integer, Integer> (cd.getCoordX(), cd.getCoordY());
    		if (coordVisited.contains(coords)) continue;
    		coordVisited.add(coords);
    		
    		SortedMap<Integer, ArrayList<Pair<Integer, Integer>>> pOrg = organizarPeticiones (coords);
    		boolean done = false;
    		
    		
    		for (int j=i; j<n; j++) {
    			Distribucion cd1 = centrosDistribucion.get(i);
    			if (new Pair <Integer, Integer> (cd1.getCoordX(), cd1.getCoordY()) != coords) continue;
    			
	    		for (ArrayList<Pair<Integer, Integer>> v : pOrg.values()) {
	    			for (Pair <Integer, Integer> p : v) {
	    				int x = used.get(p.a);
	    				if (x > p.b) continue;
	    				
	    				if (!asignaPeticion(j, p)) { done = true; break; }
	    				else used.put(p.a, x+1);
	    			}
	    			if (done) break;
	    		}
    		}
    	}
    }

    // Genera solució inicial repartint paquets equitativament entre tots els camions amb ponderacions dels costos i
    // beneficis. Posar maxim x paquets en els diferents camions equitativament.
    public void generateInitialSolution3 () {}
}