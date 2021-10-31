package Abastecimiento;

// IMPORTS.
import java.util.*;

import IA.Gasolina.Gasolineras;
import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Distribucion;
import IA.Gasolina.Gasolinera;

public class AbastecimientoState {
    // ATRIBUTOS.
    static int maxDist;
    static int costeKilometro;
    static int valorDeposito;
    
    Gasolineras gasolineras;
    CentrosDistribucion centrosDistribucion;
    
   private int distTraveled;
   private double precioEnDepositos;
    
    private ArrayList<ArrayList<Peticion>> asignaciones;
    private ArrayList <Integer> distancias;
    private Set <String> peticionesDesatendidas;

    // CONSTRUCTORS.
    public AbastecimientoState () {}
    
    public AbastecimientoState (Gasolineras gasolineras, CentrosDistribucion centrosDistribucion){
        this.gasolineras		 = gasolineras;
        this.centrosDistribucion = centrosDistribucion;
        
        this.distTraveled 		= 0;
        this.precioEnDepositos 	= 0.0;
        
        AbastecimientoState.maxDist 		= 640;
        AbastecimientoState.valorDeposito 	= 1000;
        AbastecimientoState.costeKilometro 	= 2;

        this.peticionesDesatendidas = new HashSet <> ();
        this.asignaciones 			= new ArrayList <> (centrosDistribucion.size());
        this.distancias 			= new ArrayList <> (centrosDistribucion.size());

        for (int i=0; i<centrosDistribucion.size(); i++){
            this.distancias.add(maxDist);
            this.asignaciones.add(new ArrayList <>());
        }
        
        for (int i = 0; i < gasolineras.size(); i++) {
        	for (int j=0; j < gasolineras.get(i).getPeticiones().size(); j++)
        		peticionesDesatendidas.add(new Pair <Integer, Integer> (i, j).makeString());
        }
    }
    
    public AbastecimientoState (AbastecimientoState as) {
    	this(as.gasolineras, as.centrosDistribucion);
    	
    	this.distTraveled = as.distTraveled;
    	this.precioEnDepositos = as.precioEnDepositos;
    
    	for (int i=0; i<as.asignaciones.size(); i++) {
    		int x = as.distancias.get(i);
    		this.distancias.set(i, x);
    		
    		int size = as.asignaciones.get(i).size();
    		ArrayList <Peticion> aux = new ArrayList <> ();
    		for (int j=0; j<size; j++) {
    			Pair <Integer, Integer> p = as.asignaciones.get(i).get(j).get();
    			Pair <Integer, Integer> p1 = new Pair <Integer, Integer> (p.a, p.b);
    			
    			this.peticionesDesatendidas.remove(p1.makeString());
    		
    			aux.add(new Peticion (p1));
    		}
    		this.asignaciones.set(i, aux);
    	}
    }

    // SETTERS.
    public void setAssigments(ArrayList<ArrayList<Peticion>> asignaciones) {
        this.asignaciones = asignaciones;
    }

    public void setDistances (ArrayList <Integer> distancias) {
        this.distancias = distancias;
    }
    
    public void setDistTraveled (int distTraveled) {
    	this.distTraveled = distTraveled;
    }
    
    public void setPrecioEnDepositos (int precioEnDepositos) {
    	this.precioEnDepositos = precioEnDepositos;
    }
    
    public void setMaxDist (int maxDist) {
    	this.maxDist = maxDist;
    }
    
    public void setCosteKilometro (int costeKilometro) {
    	AbastecimientoState.costeKilometro = costeKilometro;
    }
    
    public void setValorDeposito (int valorDeposito) {
    	AbastecimientoState.valorDeposito = valorDeposito;
    }

    // GETTERS.
    public ArrayList<ArrayList<Peticion>> getAsignaciones() {
        return asignaciones;
    }

    public ArrayList<Integer> getDistancias () {
        return distancias;
    }
    
    public int getDistTraveled () {
    	return this.distTraveled;
    }
    
    public double getPrecioEnDepositos () {
    	return this.precioEnDepositos;
    }
    
    public Set <String> getPeticionesDesatendidas () {
    	return this.peticionesDesatendidas;
    }
    
    public double getBenefit (){
    	return (valorDeposito * precioEnDepositos - costeKilometro * distTraveled);
    }
    
    public int getValorDeposito () {
    	return this.valorDeposito;
    }

    // OPERADORS.
    // Las peticiones seran identificadas asi: Pair <Integer, Integer> p = (id peticion, id gasolinera)
    // Los camiones seran identificados con su propio Id Integer
    public Integer calcularDistancias (int c, Pair <Integer, Integer> p) {
    	ArrayList <Peticion> cAssig = asignaciones.get(c);

    	int n = cAssig.size();

    	Distribucion d = centrosDistribucion.get(c);
    	Gasolinera g1 = gasolineras.get(p.a.intValue());

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

    public Integer actualizaDistancia(Integer oldP, Pair <Integer, Integer> newP, int c){
        Peticion oldPn = asignaciones.get(c).get(oldP.intValue());
    	
    	Distribucion d = centrosDistribucion.get(c);
        Gasolinera gOld = gasolineras.get(oldPn.get().a.intValue());
        Gasolinera gNew = gasolineras.get(newP.a.intValue());

        Pair <Integer, Integer> dCoord = new Pair <Integer, Integer> (d.getCoordX(), d.getCoordY());
        Pair <Integer, Integer> oldCoord = new Pair <Integer, Integer> (gOld.getCoordX(), gOld.getCoordY());
        Pair <Integer, Integer> newCoord = new Pair <Integer, Integer> (gNew.getCoordX(), gNew.getCoordY());

        int dcToOldC = calcularDistancia(dCoord, oldCoord);
        int dcToNewC = calcularDistancia(dCoord, newCoord);

        if (oldP%2 == 0) {
            if (asignaciones.get(c).size()-1 == oldP){
                return distancias.get(c) + 2*dcToOldC - 2*dcToNewC;
            }
            Peticion pMid = asignaciones.get(c).get(oldP.intValue()+1);
            Gasolinera gMid = gasolineras.get(pMid.get().a.intValue());
            
            Pair <Integer, Integer> midCoord = new Pair <Integer, Integer> (gMid.getCoordX(), gMid.getCoordY());

            int dcToMidC = calcularDistancia(dCoord, midCoord);

            int dOld = dcToOldC + calcularDistancia(midCoord, oldCoord) + dcToMidC; 
            int dNew = dcToNewC + calcularDistancia(midCoord, newCoord) + dcToMidC;

            return distancias.get(c) + dOld - dNew;
        }
        
        Peticion pMid = asignaciones.get(c).get(oldP.intValue()-1);
        Gasolinera gMid = gasolineras.get(pMid.get().a.intValue());

        Pair <Integer, Integer> midCoord = new Pair <Integer, Integer> (gMid.getCoordX(), gMid.getCoordY());

        int dcToMidC = calcularDistancia(dCoord, midCoord);

        int dOld = dcToMidC + calcularDistancia(midCoord, oldCoord) + dcToOldC;
        int dNew = dcToMidC + calcularDistancia(midCoord, newCoord) + dcToNewC;

        return distancias.get(c) + dOld - dNew;
    }


    public boolean asignaPeticion (int c, Pair <Integer, Integer> p, boolean addDays) {
        ArrayList <Peticion> cAssig = asignaciones.get(c);

        int dist = calcularDistancias(c, p);
        int n = cAssig.size();

        // control de restricciones de distancia y número de peticiones asignadas
    	if (dist > 0 && (n/2) < 5) {
	    	asignaciones.get(c).add(new Peticion (p));

	    	distTraveled = distTraveled - (maxDist-distancias.get(c)) + (maxDist - dist);
	    	distancias.set(c, dist);
	    	
	    	if (addDays) {
		    	int diasPendientes = gasolineras.get(p.a.intValue()).getPeticiones().get(p.b.intValue());
		    	if (diasPendientes == 0) precioEnDepositos += 1.02;
		    	else precioEnDepositos += ((100 - Math.pow(2, diasPendientes)) / 100);
	    	}
	    	peticionesDesatendidas.remove(p.makeString());
	    	return true;
    	}
    	return false;
	}

    /*
    * Pre: la petición p está asignada al camión c y la petición p1 al camion c1
    * Post: La asignación de las peticiones se invierte.
    * */
    public boolean intercambiaPeticiones (Integer p, Integer p1, int c, int c1){
        if (c == c1) return false;

    	Peticion a = asignaciones.get(c).get(p.intValue());
        Peticion b = asignaciones.get(c1).get(p1.intValue());
        
        int distc = distancias.get(c);
        int distc1 = distancias.get(c1);
        
        int checkc = actualizaDistancia(p, b.get(), c);
        int checkc1 = actualizaDistancia(p1, a.get(), c1);
        
        if (checkc < 0 || checkc1 < 0) return false;
        
        asignaciones.get(c).set(p.intValue(), b);
        asignaciones.get(c1).set(p1.intValue(), a);
        
        distTraveled = distTraveled - (maxDist-distc) - (maxDist-distc1) + (maxDist - checkc) + (maxDist - checkc1);
        	
        distancias.set(c, checkc);
        distancias.set(c1, checkc1);
        return true;
    }
    /*
    * Pre: Both p y p1 son peticiones asignadas al camión c
    * Post: El orden en que estaban asignadas p y p1 se invierte
    */
	public boolean intercambioOrden (Integer p, Integer p1, int c) {
		if (p == p1) return false;
		if (p%2 == 0 && p1 == p+1 || p1%2 == 0 && p == p1+1) return false;
		
    	Peticion a = asignaciones.get(c).get(p.intValue());
        Peticion b = asignaciones.get(c).get(p1.intValue());
        
        int dist = distancias.get(c);
        
        int newDist = actualizaDistancia(p, b.get(), c);
        if (newDist < 0) return false;

        distancias.set(c, newDist);
        newDist = actualizaDistancia(p1, a.get(), c);

        if (newDist < 0) {
        	distancias.set(c, dist);
        	return false;
        }
        
        asignaciones.get(c).set(p.intValue(), b);
        asignaciones.get(c).set(p1.intValue(), a);
        
        distTraveled = distTraveled - (maxDist - dist) + (maxDist - newDist);
        
        distancias.set(c, newDist);

        return true;
	}


   //Aux function for cambiaPeticion
    private boolean renewDistances(int peticionesC) {
    	ArrayList<Peticion> auxP = asignaciones.get(peticionesC);

    	int dStore = distancias.get(peticionesC);
    	distTraveled -= (maxDist - dStore);
    	asignaciones.set(peticionesC, new ArrayList<Peticion>());
    	
    	distancias.set(peticionesC, maxDist);

    	for (int i = 0; i < auxP.size(); i++) 
    		if (!asignaPeticion (peticionesC, auxP.get(i).get(), false)) return false;
    	
    	return true;
    }

    /*
     * Pre: La petición p estaba asignada al camion c
     * Post: La petición p deja de estar asignada al camion c y pas+
     * 
     * a a formar parte de las asignaciones de c1
     * */

    public boolean cambiaPeticion (Integer p, int c, int c1) {
    	if (c == c1) return false;		//No cambia nada

    	Peticion a = asignaciones.get(c).get(p.intValue()); 
    	if (asignaPeticion(c1, a.get(), false)) {
    		asignaciones.get(c).remove(p.intValue());
    		
    		renewDistances(c);
    		return true;
        }
        return false;
    }
    
    public boolean cambioPeticionNoAsig(Integer p, int c, Pair <Integer, Integer> newP){  
        int distc = distancias.get(c);
        int checkc = actualizaDistancia(p, newP, c);
        
        if (checkc < 0 ) return false;
        
        asignaciones.get(c).set(p.intValue(), new Peticion (newP));
        
        distTraveled = distTraveled - (maxDist-distc)+ (maxDist - checkc) ;
        	
        distancias.set(c, checkc);
        return true;
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
    			while (!visited[c] && !asignaPeticion(c, pet, true)) {
    				visited[c] = true;
    				c = rand.nextInt(n);
    			}
    		}
    	}
    }

    private int ponderarCoste (int dist, int dias) {
    	return (int) (10*((100 - Math.pow(2, dias))) - 2*dist);
    }

    // cambiar per crear ponderacio basada en costos
    SortedMap <Integer, ArrayList <Pair<Integer, Integer>>> organizarPeticiones (Pair <Integer, Integer> cCoord) {
    	SortedMap <Integer, ArrayList <Pair<Integer, Integer>>> pOrg = new TreeMap <Integer, ArrayList <Pair<Integer, Integer>>> ();

    	int i = 0;
    	for (Gasolinera g : gasolineras) {
    		Pair <Integer, Integer> gCoord = new Pair <Integer, Integer> (g.getCoordX(), g.getCoordY());
    		int d = calcularDistancia (cCoord, gCoord);

    		int n = g.getPeticiones().size();
    		for (int j = 0; j<n; j++) {
    			int c = ponderarCoste (d, g.getPeticiones().get(j));
    			ArrayList <Pair<Integer, Integer>> arr;

    			if (pOrg.containsKey(c)) arr = pOrg.get(c);
    			else arr = new ArrayList <Pair <Integer, Integer>> ();

    			Pair <Integer, Integer> p = new Pair <Integer, Integer> (i, j);
    			arr.add(p);
    			pOrg.put (c, arr);
    		}
    		i++;
    	}
    	return pOrg;
    }
    
    // Genera solució inicial repartint paquets equitativament entre tots els camions amb ponderacions dels costos i
    // beneficis. Posar maxim x paquets en els diferents camions equitativament.
    public void generateInitialSolution2 () {
    	Set <String> coordVisited = new HashSet <String> ();
    	
    	int n = centrosDistribucion.size();
    	for (int i=0; i<n; i++) {
    		Distribucion cd = centrosDistribucion.get(i);
    		Pair <Integer, Integer> coords = new Pair <Integer, Integer> (cd.getCoordX(), cd.getCoordY());

    		if (!coordVisited.isEmpty() && coordVisited.contains(coords.makeString())) continue;
    		coordVisited.add(coords.makeString());

    		SortedMap<Integer, ArrayList<Pair<Integer, Integer>>> pOrg = organizarPeticiones (coords);

    		ArrayList <Integer> pos = new ArrayList <> ();
    		for (int j=i; j<n; j++) {
    			Distribucion cd1 = centrosDistribucion.get(j);
    			if (coords.equals(cd1.getCoordX(), cd1.getCoordY())) pos.add(j);
    		}

    		int j = 0;
    		boolean add = true;
	    	for (ArrayList<Pair<Integer, Integer>> v : pOrg.values()) {
	    		for (Pair <Integer, Integer> p : v) {
	    			if (peticionesDesatendidas.contains(p.makeString())) asignaPeticion(pos.get(j), p, true);

	    			j = add ? j + 1 : j - 1;
	    			
	    			if (add && j == pos.size()) {add = false; j--; }
	    			else if (!add && j == -1) {add = true; j++; }
	    		}
	    	}
    	}
    }
    
    public void print_state () {
    	int total_dist = 0;
    	int petitions = 0;
    	for (int i=0; i<asignaciones.size(); i++) {
        	ArrayList <Peticion> assigs = asignaciones.get(i);
        	System.out.println ("(" + i + ") --> " + distancias.get(i) + ": ");
        	total_dist += (maxDist-distancias.get(i));
	        for (Peticion p : assigs) {
	        	petitions++;
	        	System.out.print ("(" + p.get().a + "," + p.get().b + ")");
	        }
	        System.out.println();
    	}
    	System.out.println ("Total distance " + total_dist);
    }
}
