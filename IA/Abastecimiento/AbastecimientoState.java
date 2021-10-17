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
    
    public AbastecimientoState (AbastecimientoState as) {
    	this.gasolineras = as.gasolineras;
    	this.centrosDistribucion = as.centrosDistribucion;
    	this.asignaciones = as.asignaciones;
    	this.distancias = as.distancias;
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

        Peticion oldPet = new Peticion (oldP);

        Pair <Integer, Integer> dCoord = new Pair <Integer, Integer> (d.getCoordX(), d.getCoordY());
        Pair <Integer, Integer> oldCoord = new Pair <Integer, Integer> (gOld.getCoordX(), gOld.getCoordY());
        Pair <Integer, Integer> newCoord = new Pair <Integer, Integer> (gNew.getCoordX(), gNew.getCoordY());

        int dcToOldC = calcularDistancia(dCoord, oldCoord);
        int dcToNewC = calcularDistancia(dCoord, newCoord);

        int n = asignaciones.get(c).size();
        int dAct=0;
        int pos = 0;

        while (asignaciones.get(c).get(pos).get() != oldP) ++pos;

        if (pos%2 == 0) {

            if (n-1 == pos){
                dAct = distancias.get(c) + 2*dcToOldC - 2*dcToNewC;
                //distancias.set(c, dAct);
            }
            else {

                Peticion pMid = asignaciones.get(c).get(pos+1);
                Gasolinera gMid = gasolineras.get(pMid.get().a);

                Pair <Integer, Integer> midCoord = new Pair <Integer, Integer> (gMid.getCoordX(), gMid.getCoordY());

                int dcToMidC = calcularDistancia(dCoord, midCoord);

                int dOld = dcToOldC + calcularDistancia(midCoord, oldCoord) + dcToMidC; //dcToMidC + calcularDistancia(midCoord, oldCoord) + dcToOldC;
                int dNew = dcToNewC + calcularDistancia(midCoord, newCoord) + dcToMidC;

                dAct = distancias.get(c) + dOld - dNew;
                //distancias.set(c, dAct);
            }
        }
        else {
            Peticion pMid = asignaciones.get(c).get(pos-1);
            Gasolinera gMid = gasolineras.get(pMid.get().a);

            Pair <Integer, Integer> midCoord = new Pair <Integer, Integer> (gMid.getCoordX(), gMid.getCoordY());

            int dcToMidC = calcularDistancia(dCoord, midCoord);

            int dOld = dcToMidC + calcularDistancia(midCoord, oldCoord) + dcToOldC;
            int dNew = dcToMidC + calcularDistancia(midCoord, newCoord) + dcToNewC;

            dAct = distancias.get(c) + dOld - dNew;
            //distancias.set(c, dAct);
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
    public boolean intercambiaPeticiones (Integer p, Integer p1, int c, int c1){
        if (c == c1) return false;

    	Peticion a = asignaciones.get(c).get(p);
        Peticion b = asignaciones.get(c1).get(p1);

        int x = actualizaDistancia(a.get(), b.get(), c);
        int y = actualizaDistancia(b.get(), a.get(), c1);

        if (x > 0 && y > 0){
            asignaciones.get(c).set(p, b);
            asignaciones.get(c1).set(p1, a);

            distancias.set(c, x);
            distancias.set(c1, y);

            return true;
        }
        return false;
    }
    /*
    * Pre: Both p y p1 son peticiones asignadas al camión c
    * Post: El orden en que estaban asignadas p y p1 se invierte
    */
    public boolean intercambioOrden (Integer p, Integer p1, int c) {
    	if (p == p1) return true; 								//same petition, hence already swapped since nothing changes.

    	Peticion a = asignaciones.get(c).get(p);
        Peticion b = asignaciones.get(c).get(p1);

        int x = actualizaDistancia(a.get(), b.get(), c);
        int ogDistance = distancias.get(c);

        if (x > 0){
            distancias.set(c, x);                               //para poder calcular la distancia correcta en el segundo cambio (int y) me veo obligada a actualizar
            int y = actualizaDistancia(b.get(), a.get(), c);    //el arraylist de distancias aunque pueda ser incorrecto (si y < 0 y por lo tando no se de el intercambio)

            if (y > 0){
                asignaciones.get(c).set(p1, a);
                asignaciones.get(c).set(p, b);

                distancias.set(c, y);

                return true;
            }
            else distancias.set(c, ogDistance);                 //si pasa cambiamos al valor original y aquí no ha pasado nada :)
            return false;
        }
        return false;
    }


   //Aux function for cambiaPeticion
    private void renewDistances(int peticionesC) {
    	ArrayList<Peticion> auxP = asignaciones.get(peticionesC);

    	asignaciones.set(peticionesC, new ArrayList<Peticion>());

    	for (int i = 0; i < auxP.size(); i++) {
    		Peticion x = auxP.get(i);
    		int dist = calcularDistancias(peticionesC, x.get());

    		distancias.set(peticionesC, dist);
    		asignaciones.get(peticionesC).add(i, x);
    	}
    }

    /*
     * Pre: La petición p estaba asignada al camion c
     * Post: La petición p deja de estar asignada al camion c y pasa a formar parte de las asignaciones de c1
     * */

    public boolean cambiaPeticion (Integer p, int c, int c1) {
    	if (c == c1) return true;		//No cambia nada

        int sizeC1 = asignaciones.get(c1).size();
    	int newDist;

    	boolean bounded = false;

    	Peticion a = asignaciones.get(c).get(p);
    	Peticion b;

        if (sizeC1 > p) {
        	b = asignaciones.get(c1).get(p);
        	newDist = actualizaDistancia(b.get(), a.get(), c1);
        	bounded = true;
        }
        else newDist = calcularDistancias(c1, a.get());


        if (newDist > 0){
            asignaciones.get(c).remove(p.intValue());

            if (bounded) asignaciones.get(c1).add(p, a);
            else asignaciones.get(c1).add(a);

            distancias.set(c, maxDist);
            distancias.set(c1, maxDist);

            renewDistances(c);
            renewDistances(c1);

            return true;
        }
        return false;
    }

    public boolean cambioPeticionNoAsig(Integer p, int c, Pair <Integer, Integer> newP){
    	Peticion a = asignaciones.get(c).get(p);
    	Peticion b = new Peticion(newP);

        int newDist = actualizaDistancia(a.get(), b.get(), c);

        if (newDist > 0){
            asignaciones.get(c).remove(p.intValue());
            asignaciones.get(c).add(p, b);

            distancias.set(c, maxDist);
            renewDistances(c);
          
            return true;
        }
        return false;
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
    // beneficis. Maximizar distancies en tots els camions.
    public void generateInitialSolution2 () {
    	Set <String> used = new HashSet <String> ();
    	Set <String> coordVisited = new HashSet <String> ();

    	int n = centrosDistribucion.size();
    	for (int i=0; i<n; i++) {
    		Distribucion cd = centrosDistribucion.get(i);
    		Pair <Integer, Integer> coords = new Pair <Integer, Integer> (cd.getCoordX(), cd.getCoordY());

    		if (!coordVisited.isEmpty() && coordVisited.contains(coords.makeString())) continue;
    		coordVisited.add(coords.makeString());

    		SortedMap<Integer, ArrayList<Pair<Integer, Integer>>> pOrg = organizarPeticiones (coords);

    		for (int j=i; j<n; j++) {
    			Distribucion cd1 = centrosDistribucion.get(j);
    			if (!coords.equals(cd1.getCoordX(), cd1.getCoordY())) continue;

	    		for (ArrayList<Pair<Integer, Integer>> v : pOrg.values()) {
	    			for (Pair <Integer, Integer> p : v)
	    				if (!used.contains(p.makeString()) && asignaPeticion(j, p)) used.add(p.makeString());
	    		}
    		}
    	}
    }

    // Genera solució inicial repartint paquets equitativament entre tots els camions amb ponderacions dels costos i
    // beneficis. Posar maxim x paquets en els diferents camions equitativament.
    public void generateInitialSolution3 () {
    	Set <String> used = new HashSet <String> ();
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
	    			if (!used.contains(p.makeString()) && asignaPeticion(pos.get(j), p)) used.add(p.makeString());

	    			if (add) j++;
	    			else j--;

	    			if (add && j == pos.size()) {add = false; j--; }
	    			else if (!add && j == -1) {add = true; j++; }
	    		}
	    	}
    	}
    }

    public int getBenefit () {
    	return 0;
    }
}
