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

    int distTraveled;
    double precioEnDepositos;

    private ArrayList<ArrayList<Peticion>> asignaciones;
    private ArrayList <Integer> distancias;
    private Set <String> peticionesDesatendidas;

    // CONSTRUCTORS.
    public AbastecimientoState (Gasolineras gasolineras, CentrosDistribucion centrosDistribucion){
        this.gasolineras = gasolineras;
        this.centrosDistribucion = centrosDistribucion;
        this.distTraveled = 0;
        this.precioEnDepositos = 0.0;

        this.peticionesDesatendidas = new HashSet <> ();
        this.asignaciones = new ArrayList <> (centrosDistribucion.size());
        this.distancias = new ArrayList <> (centrosDistribucion.size());

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

    	for (int i=0; i<as.asignaciones.size(); i++) {
    		int x = as.distancias.get(i);
    		this.distancias.set(i, x);

    		int size = as.asignaciones.get(i).size();
    		ArrayList <Peticion> aux = new ArrayList <> ();
    		for (int j=0; j<size; j++) {
    			Pair <Integer, Integer> p = as.asignaciones.get(i).get(j).get();

    			Pair <Integer, Integer> p1 = new Pair <Integer, Integer> (p.a, p.b);
    			if (this.peticionesDesatendidas.contains(p1.makeString())) {
    				this.peticionesDesatendidas.remove(p1.makeString());
    			} else {
    				System.out.println ("SOMETHINGS WRONG");
    			}

    			aux.add(new Peticion (p1));
    		}
    		this.asignaciones.set(i, aux);
    	}

    	System.out.println ("SIZE " + this.peticionesDesatendidas.size());
    	for (String s : this.peticionesDesatendidas) {
    		System.out.print (s + " : ");
    	}
    	System.out.println ();
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

	    		int dCheck = distancias.get(c) + prevD*2 - newD;
	    		if (dCheck > maxDist) System.out.println ("WROOONG " + dCheck + " new " + newD + " prevD " + prevD);
	    		return distancias.get(c) + prevD*2 - newD;
	    	}
		}

		distTraveled = distTraveled + calcularDistancia(coord1, coord3)*2;
    	return distancias.get(c) - calcularDistancia(coord1, coord3)*2;
    }

    public Integer actualizaDistancia(Integer oldP, Pair <Integer, Integer> newP, int c){
        Peticion oldPn = asignaciones.get(c).get(oldP.intValue());

    	Distribucion d = centrosDistribucion.get(c);
        Gasolinera gOld = gasolineras.get(oldPn.get().a.intValue());
        Gasolinera gNew = gasolineras.get(newP.a.intValue());

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
            	distTraveled = distTraveled - 2*dcToOldC + 2*dcToNewC;
                return distancias.get(c) + 2*dcToOldC - 2*dcToNewC;
            }
            Peticion pMid = asignaciones.get(c).get(oldP.intValue()+1);
            Gasolinera gMid = gasolineras.get(pMid.get().a.intValue());

            Peticion pMid = asignaciones.get(c).get(pos+1);
            Gasolinera gMid = gasolineras.get(pMid.get().a);

            Pair <Integer, Integer> midCoord = new Pair <Integer, Integer> (gMid.getCoordX(), gMid.getCoordY());

            int dcToMidC = calcularDistancia(dCoord, midCoord);

            int dOld = dcToOldC + calcularDistancia(midCoord, oldCoord) + dcToMidC; //dcToMidC + calcularDistancia(midCoord, oldCoord) + dcToOldC;
            int dNew = dcToNewC + calcularDistancia(midCoord, newCoord) + dcToMidC;

            distTraveled = distTraveled - dOld + dNew;
            return distancias.get(c) + dOld - dNew;
        }

        Peticion pMid = asignaciones.get(c).get(oldP.intValue()-1);
        Gasolinera gMid = gasolineras.get(pMid.get().a.intValue());

        Pair <Integer, Integer> midCoord = new Pair <Integer, Integer> (gMid.getCoordX(), gMid.getCoordY());

        int dcToMidC = calcularDistancia(dCoord, midCoord);

        int dOld = dcToMidC + calcularDistancia(midCoord, oldCoord) + dcToOldC;
        int dNew = dcToMidC + calcularDistancia(midCoord, newCoord) + dcToNewC;

        distTraveled = distTraveled - dOld + dNew;
        return distancias.get(c) + dOld - dNew;
    }


    public boolean asignaPeticion (int c, Pair <Integer, Integer> p, boolean addDays) {
    	System.out.println ("ASIGNA PETICION PRE" + distancias.get(c));
		for (Peticion pet : asignaciones.get(c)) {
			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
		}
		System.out.println();

        ArrayList <Peticion> cAssig = asignaciones.get(c);

        int dist = calcularDistancias(c, p);
        int n = cAssig.size();

        // control de restricciones de distancia y número de peticiones asignadas
    	if (dist > 0 && (n/2) < 5) {
	    	cAssig.add(new Peticion (p));
	    	asignaciones.set(c, cAssig);

	    	distancias.set(c, dist);

	    	if (addDays) {
		    	int diasPendientes = gasolineras.get(p.a.intValue()).getPeticiones().get(p.b.intValue());
		    	if (diasPendientes == 0) precioEnDepositos += 1.02;
		    	else precioEnDepositos += ((100 - Math.pow(2, diasPendientes)) / 100);
	    	}
	    	else peticionesDesatendidas.remove(p.makeString());
	    	//System.out.println (distTraveled);
	    	System.out.println ("ASIGNA PETICION POST GOOD" + distancias.get(c));
			for (Peticion pet : asignaciones.get(c)) {
				System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
			}
			System.out.println();
	    	return true;
    	}
    	//System.out.println (distTraveled);
    	System.out.println ("ASIGNA PETICION POST BAD" + distancias.get(c));
		for (Peticion pet : asignaciones.get(c)) {
			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
		}
		System.out.println();
    	return false;
	}

    /*
    * Pre: la petición p está asignada al camión c y la petición p1 al camion c1
    * Post: La asignación de las peticiones se invierte.
    * */
    public boolean intercambiaPeticiones (Integer p, Integer p1, int c, int c1){
    	System.out.println ("INTERCAMBIA PETICION PRE C" + distancias.get(c));
		for (Peticion pet : asignaciones.get(c)) {
			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
		}
		System.out.println();

		System.out.println ("INTERCAMBIA PETICION PRE C1" + distancias.get(c1));
		for (Peticion pet : asignaciones.get(c1)) {
			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
		}
		System.out.println();

        if (c == c1) return false;

    	Peticion a = asignaciones.get(c).get(p.intValue());
        Peticion b = asignaciones.get(c1).get(p1.intValue());

        int x = actualizaDistancia(p.intValue(), b.get(), c);
        int y = actualizaDistancia(p1.intValue(), a.get(), c1);

        if (x > 0 && y > 0){
            asignaciones.get(c).set(p.intValue(), b);
            asignaciones.get(c1).set(p1.intValue(), a);

            distancias.set(c, x);
            distancias.set(c1, y);

            //System.out.println (distTraveled);

            System.out.println ("INTERCAMBIA PETICION POST GOOD C" + distancias.get(c));
    		for (Peticion pet : asignaciones.get(c)) {
    			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
    		}
    		System.out.println();

    		System.out.println ("INTERCAMBIA PETICION POST GOOD C1" + distancias.get(c1));
    		for (Peticion pet : asignaciones.get(c1)) {
    			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
    		}
    		System.out.println();

            return true;
        }
       // System.out.println (distTraveled);
        System.out.println ("INTERCAMBIA PETICION POST BAD C" + distancias.get(c));
		for (Peticion pet : asignaciones.get(c)) {
			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
		}
		System.out.println();

		System.out.println ("INTERCAMBIA PETICION POST BAD C1" + distancias.get(c1));
		for (Peticion pet : asignaciones.get(c1)) {
			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
		}
		System.out.println();
        return false;
    }
    /*
    * Pre: Both p y p1 son peticiones asignadas al camión c
    * Post: El orden en que estaban asignadas p y p1 se invierte
    */
    public boolean intercambioOrden (Integer p, Integer p1, int c) {
    	System.out.println ("INTERCAMBIO ORDEN PRE" + distancias.get(c));
		for (Peticion pet : asignaciones.get(c)) {
			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
		}
		System.out.println();

    	int dist = distancias.get(c), distStore = distTraveled;
    	Peticion a = asignaciones.get(c).get(p.intValue());
        Peticion b = asignaciones.get(c).get(p1.intValue());

        asignaciones.get(c).set(p1.intValue(), a);
        asignaciones.get(c).set(p.intValue(), b);

        /*System.out.println ("AFTER REMOVING");
		for (Peticion pet : asignaciones.get(c)) {
			System.out.println ("(" + pet.get().a + ", " + pet.get().b + ")");
		}*/

        boolean check = renewDistances(c);

        if (check) {
        	System.out.println ("here");

        	distancias.set(c, dist);
        	distTraveled = distStore;

        	asignaciones.get(c).set(p.intValue(), a);
            asignaciones.get(c).set(p1.intValue(), b);

           // System.out.println (distTraveled);
            System.out.println ("INTERCAMBIO ORDEN POST bad" + distancias.get(c));
    		for (Peticion pet : asignaciones.get(c)) {
    			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
    		}
    		System.out.println();

            return false;
        } else System.out.println ("here2");
        //System.out.println (distTraveled);

        System.out.println ("INTERCAMBIO ORDEN POST GOOD" + distancias.get(c));
		for (Peticion pet : asignaciones.get(c)) {
			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
		}
		System.out.println();
        return true;
    }


   //Aux function for cambiaPeticion
    private boolean renewDistances(int peticionesC) {
    	ArrayList<Peticion> auxP = asignaciones.get(peticionesC);

    	asignaciones.set(peticionesC, new ArrayList<Peticion>());

    	distancias.set(peticionesC, maxDist);

    	for (int i = 0; i < auxP.size(); i++) {
    		if (!asignaPeticion (peticionesC, auxP.get(i).get(), false)) return false;
    	}
    	return true;
    }

    /*
     * Pre: La petición p estaba asignada al camion c
     * Post: La petición p deja de estar asignada al camion c y pasa a formar parte de las asignaciones de c1
     * */

    public boolean cambiaPeticion (Integer p, int c, int c1) {
    	if (c == c1) return false;		//No cambia nada

    	System.out.println ("CAMBIA PETICION PRE C" + distancias.get(c));
		for (Peticion pet : asignaciones.get(c)) {
			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
		}
		System.out.println();

		System.out.println ("CAMBIA PETICION PRE C1" + distancias.get(c1));
		for (Peticion pet : asignaciones.get(c1)) {
			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
		}
		System.out.println();

    	Peticion a = asignaciones.get(c).get(p.intValue());
    	if (asignaPeticion(c1, a.get(), false)) {
    		//System.out.println (distancias.get(c1));

    		asignaciones.get(c).remove(p.intValue());

    		renewDistances(c);
    		//System.out.println (distTraveled);
    		System.out.println ("CAMBIA PETICION POST TRUE C" + distancias.get(c));
    		for (Peticion pet : asignaciones.get(c)) {
    			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
    		}
    		System.out.println();

    		System.out.println ("CAMBIA PETICION POST TRUE C1" + distancias.get(c1));
    		for (Peticion pet : asignaciones.get(c1)) {
    			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
    		}
    		System.out.println();
    		return true;
        }
    	//System.out.println (distTraveled);
    	System.out.println ("CAMBIA PETICION POST FALSE C" + distancias.get(c));
		for (Peticion pet : asignaciones.get(c)) {
			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
		}
		System.out.println();

		System.out.println ("CAMBIA PETICION POST FALSE C1" + distancias.get(c1));
		for (Peticion pet : asignaciones.get(c1)) {
			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
		}
		System.out.println();
        return false;
    }

    public Pair <Integer, Integer> getCoordGas (Pair <Integer, Integer> p){
    	return new Pair <Integer, Integer> (gasolineras.get(p.a.intValue()).getCoordX(), gasolineras.get(p.a.intValue()).getCoordY());
    }

    public int recalcDist (Pair <Integer, Integer> f, Pair <Integer, Integer> s,
    			Pair <Integer, Integer> n, int c, boolean changeFirst) {

    	Distribucion d = centrosDistribucion.get(c);
    	Pair <Integer, Integer> cd = new Pair <Integer, Integer> (d.getCoordX(), d.getCoordY());

    	f = getCoordGas(f);
    	s = getCoordGas(s);
    	n = getCoordGas(n);

    	int oldD = calcularDistancia (cd, f) + calcularDistancia (f, s) + calcularDistancia (s, cd);

    	int newD = 0;

    	if (changeFirst) newD = calcularDistancia (cd, n) + calcularDistancia (n, s) + calcularDistancia (s, cd);
    	else newD = calcularDistancia (cd, f) + calcularDistancia (f, n) + calcularDistancia (n, cd);

    	distTraveled = distTraveled - oldD + newD;
    	return distancias.get(c) + oldD - newD;
    }

    public boolean cambioPeticionNoAsig(Integer p, int c, Pair <Integer, Integer> newP){
    	System.out.println ("CAMBIO PET NO ASSIG PRE" + distancias.get(c));
		for (Peticion pet : asignaciones.get(c)) {
			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
		}
		System.out.println();

    	Pair <Integer, Integer> f, s;

    	f = asignaciones.get(c).get(p.intValue()).get();

    	double addPrecioEnDepositos;
    	int diasPendientes = gasolineras.get(newP.a.intValue()).getPeticiones().get(newP.b.intValue());
    	if (diasPendientes == 0) addPrecioEnDepositos = 1.02;
    	else addPrecioEnDepositos = (100 - Math.pow(2, diasPendientes)) / 100;

    	double removePrecioEnDespositos;
    	diasPendientes = gasolineras.get(f.a.intValue()).getPeticiones().get(f.b.intValue());
    	if (diasPendientes == 0) removePrecioEnDespositos = 1.02;
    	else removePrecioEnDespositos = (100 - Math.pow(2, diasPendientes)) / 100;

    	int dist = 0;
    	boolean firstPos = true;

    	if (p%2 == 0) {
    		if (p == asignaciones.get(c).size()-1) {
    			Distribucion d = centrosDistribucion.get(c);
    	    	Pair <Integer, Integer> cd = new Pair <Integer, Integer> (d.getCoordX(), d.getCoordY());

    			int oldDist = calcularDistancia (fc, cd)*2;
    			int newDist = calcularDistancia (newPCoord, cd)*2;

    			dist = distancias.get(c) + oldDist - newDist;
    			if (dist > 0) {
    				distancias.set(c, dist);
    	    		asignaciones.get(c).set(p.intValue(), new Peticion (newP));

    	    		precioEnDepositos = precioEnDepositos - removePrecioEnDespositos + addPrecioEnDepositos;

    	    		peticionesDesatendidas.add(f.makeString());
    	    		peticionesDesatendidas.remove(newP.makeString());

    	    		distTraveled = distTraveled - oldDist + newDist;
    	    		//System.out.println (distTraveled);
    	    		System.out.println ("CAMBIO PET NO ASSIG POST GOOD"  + distancias.get(c));
    	    		for (Peticion pet : asignaciones.get(c)) {
    	    			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
    	    		}
    	    		System.out.println();
    	    		return true;
    			}
    			//System.out.println (distTraveled);
    			System.out.println ("CAMBIO PET NO ASSIG POST BAD"  + distancias.get(c));
	    		for (Peticion pet : asignaciones.get(c)) {
	    			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
	    		}
	    		System.out.println();
    			return false;
    		}

    		s = asignaciones.get(c).get(p.intValue()+1).get();
    	} else {
    		f = asignaciones.get(c).get(p.intValue()-1).get();
    		s = asignaciones.get(c).get(p.intValue()).get();

    		fc = getCoordGas(f);
    		sc = getCoordGas(s);

    		firstPos = false;
    	}

    	dist = recalcDist (fc, sc, newPCoord, c, firstPos);

    	if (d > 0) {
    		//System.out.println (d);
    		distancias.set(c, d);
    		asignaciones.get(c).set(p.intValue(), new Peticion (newP));
    		precioEnDepositos = precioEnDepositos - removePrecioEnDespositos + addPrecioEnDepositos;
    		distTraveled = distTraveled + (maxDist - distancias.get(c)) - dist;

    		if (firstPos) peticionesDesatendidas.add(f.makeString());
    		else peticionesDesatendidas.add(s.makeString());

    		peticionesDesatendidas.remove(newP.makeString());

    		System.out.println ("CAMBIO PET NO ASSIG POST GOOD"  + distancias.get(c));
    		for (Peticion pet : asignaciones.get(c)) {
    			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
    		}
    		System.out.println();
    		//System.out.println (distTraveled);
    		return true;
    	}

    	System.out.println ("CAMBIO PET NO ASSIG POST BAD"  + distancias.get(c));
		for (Peticion pet : asignaciones.get(c)) {
			System.out.print ("(" + pet.get().a + ", " + pet.get().b + "), ");
		}
		System.out.println();
    	//System.out.println (distTraveled);
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

	    			j = add ? j + 1 : j - 1;

	    			if (add && j == pos.size()) {add = false; j--; }
	    			else if (!add && j == -1) {add = true; j++; }
	    		}
	    	}
    	}
    }

    public double getBenefit (){
    	AbastecimientoHeuristicFunction1 ah = new AbastecimientoHeuristicFunction1 ();
    	return ah.computeProfits(this);
    }

    public void print_state () {
    	int total_dist = 0;
    	int petitions = 0;
    	for (int i=0; i<asignaciones.size(); i++) {
        	ArrayList <Peticion> assigs = asignaciones.get(i);
        	System.out.println ("(" + i + ") --> " + asignaciones.get(i) + ": ");
	        for (Peticion p : assigs) {
	        	petitions++;
	        	System.out.print ("(" + p.get().a + "," + p.get().b + ")");
	        }
	        System.out.println();
    	}
    	System.out.println ("Total distance " + total_dist);

    	for (String s : peticionesDesatendidas) {
    		System.out.print(s + ", ");
    	}
    	System.out.println();
    	System.out.println ("num petitions " + petitions);
    }
}
