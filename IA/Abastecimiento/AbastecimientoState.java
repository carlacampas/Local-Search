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

    private ArrayList<ArrayList<Peticion>> asignaciones;
    private ArrayList <Integer> distancias;

    // CONSTRUCTORS.
    public AbastecimientoState (Gasolineras gasolineras, CentrosDistribucion centrosDistribucion){
        this.gasolineras = gasolineras;
        this.centrosDistibucion = centrosDistribucion;

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
        this.centrosDistibucion = centrosDistribucion;
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
    	
    	Distribucion d = centrosDistibucion.get(c);
    	Gasolinera g1 = gasolineras.get(p.a);
		
		Pair <Integer, Integer> coord1 = new Pair <Integer, Integer> (d.getCoordX(), d.getCoordY());
		Pair <Integer, Integer> coord3 = new Pair <Integer, Integer> (g1.getCoordX(), g1.getCoordY());
	
		if (n > 0) {
			Peticion last = cAssig.get(n - 1);
	    	if (n%2 == 1) {
	    		Gasolinera g = gasolineras.get(last.get().a);
	    		Pair <Integer, Integer> coord2 = new Pair <Integer, Integer> (g.getCoordX(), g.getCoordY());
	    		
	    		int prevD = calcularDistancia (coord1, coord2);
	    		int newD = prevD + calcularDistancia (coord2, coord3) + calcularDistancia (coord3, coord1);
	    	
	    		return distancias.get(c) + prevD*2 - newD;
	    	}
		}
    	
    	return distancias.get(c) - calcularDistancia(coord1, coord3)*2;
    }

    public void actualizaDistancia(Pair <Integer, Integer> oldP, Pair <Integer, Integer> newP, int c){
        Distribucion d = centrosDistibucion.get(c);
        Gasolinera gOld = gasolineras.get(oldP.a);
        Gasolinera gNew = gasolineras.get(newP.a);

        //Calcular la distancia de d a oldP
        //Calcular la distancia de d a newP
        Pair <Integer, Integer> dCoord = new Pair <Integer, Integer> (d.getCoordX(), d.getCoordY());
        Pair <Integer, Integer> oldCoord = new Pair <Integer, Integer> (gOld.getCoordX(), gOld.getCoordY());
        Pair <Integer, Integer> newCoord = new Pair <Integer, Integer> (gNew.getCoordX(), gNew.getCoordY());

        int dcToOldC = calcularDistancia(dCoord, oldCoord);
        int dcToNewC = calcularDistancia(dCoord, newCoord);

        int n = asignaciones.get(c).size();
        if (n-1 == oldP.b){

            if (oldP.b%2 == 0) {
                int dAct = distancias.get(c) + dcToOldC - dcToNewC;
                distancias.set(c, dAct);
            }
            else {
                int idOld = asignaciones.get(c).indexOf(oldP);

                Peticion pMid = asignaciones.get(c).get(idOld-1);
                Gasolinera gMid = gasolineras.get(pMid.a);

                Pair <Integer, Integer> midCoord = new Pair <Integer, Integer> (gMid.getCoordX(), gMid.getCoordY());

                int dcToMidC = calcularDistancia(dCoord, midCoord);

                int dOld = dcToMidC + calcularDistancia(midCoord, oldCoord) + dcToOldC;
                int dNew = dcToMidC + calcularDistancia(midCoord, oldCoord) + dcToNewC;

                int dAct = distancias.get(c) + dOld - dNew;
                distancias.set(c, dAct);
            }
        }
        else {
            if (oldP.b%2 == 0){
                int idOld = asignaciones.get(c).indexOf(oldP);

                Peticion pMid = asignaciones.get(c).get(idOld+1);
                Gasolinera gMid = gasolineras.get(pMid.a);

                Pair <Integer, Integer> midCoord = new <Integer, Integer> (gMid.getCoordX(), gMid.getCoordY());

                int dcToMidC = calcularDistancia(dCoord, midCoord);

                int dOld = //dcToMidC + calcularDistancia(midCoord, oldCoord) + dcToOldC;
                int dNew = dcToMidC + calcularDistancia(midCoord, oldCoord) + dcToNewC;

                int dAct = distancias.get(c) + dOld - dNew;
                distancias.set(c, dAct);
                //encontrar la petición siguiente midP
                //calcular las distancias de d a oldP, oldP a midP y midP a d y la sumamos a distancias(c)
                //calcular las distancias de de a newP, newP a midP y midP a d y la restamos a distancias(c)
            }
            else {
                //encontrar la petición anterior midP
                //calcular las distancias de d a midP, de midP a oldP y de oldP a d y las sumamos a distancias(c)
                //calcular las distancias de d a midP, de midP a newP y de newP a d y la restamos a distancias(c)
            }
        }
        if (n-1 != oldP.b && oldP.b%2 == 0) asignaciones.get(c).get(oldP.b)
    }


    public boolean assignaPeticion (int c, Pair <Integer, Integer> p) {
        ArrayList <Peticion> cAssig = asignaciones.get(c);
        int dist = calcularDistancias(c, p);
        int n = cAssig.size();

        // control de restricciones de distancia y número de peticiones asignadas
    	if (dist > 0 && n < 5) {
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

        if (p == 0 && p1 == 0){
            asignaciones.get(c).set(p1, b);
            asignaciones.get(c1).set(p, a);

            int d = distancias.get(c);
            int d1 = distancias.get(c1);

            distancias.set(c, d1);
            distancias.set(c1, d);
        }
        else {
            int distP1 =
        }


        /*
        * Varios casos:
        * ambos camiones c y c1 tienen una sola petición
        *   simplemente swappeamos el valor de distancias(c) por el de distancias(c1)
        *
        * ambos camiones tienen más de una petición
        *   Calcular la distancia del viaje al cual pertenece la peticion p y sumarsela al total de km del camión c
        *   Calcular la distancia del viaje al cual pertenece la peticion p1 y sumarsela al total de km del camión c1
        *
        *   Calcular la distancia que nos quedaria en el viaje de c con p1 y la otra petición y restarsela al total de km de c
        *   Calcular la distancia que nos quedaria en el viaje de c1 con p y la otra petición y restarsela al total de km de c1
        *
        * assignaciones(c).size() == 1 y asignaciones(c1).size() > 1 o viceversa
        *   distancias(c) += 2*d(c,p)
        *   distancias(c) -= 2*d(c,p1)
        *
        *   actualizamos la distancia de c1 con el nuevo viaje
        *
        *   hacemos una método actualizarDistanciaViaje(peticion que hay que eliminar, peticion que hay que añadir y el camión en cuestion)
        * */
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
    }
    /*
    * Pre: La petición p estaba asignada al camion c
    * Post: La petición p deja de estar asignada al camion c y pasa a formar parte de las asignaciones de c1
    * */
    public void cambiaPeticion (Integer p, int c, int c1) {
        int n = asignaciones.get(c1).size();
        Peticion a = asignaciones.get(c).get(p);

        asignaciones.get(c).remove(p);
        asignaciones.get(c1).add(p);
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
    		for (int j = 0; j < peticiones.size(); j++) {
    			Pair <Integer, Integer> pet = new Pair <Integer, Integer> (i, j);
    			
    			boolean[] visited = new boolean[n];
    			int c = rand.nextInt(n);
    			while (!visited[c] && !assignaPeticion(c, pet)) {
    				visited[c] = true;
    				c = rand.nextInt(n);
    			}
    			
    			assignaPeticion (c, new Pair <Integer, Integer> (i, j));
    		}
    	}
    }

    // Genera solució inicial repartint paquets equitativament entre tots els paquets amb ponderacions dels costos i
    // beneficis.
    public void generateInitialSolution2 () {}

    // Genera solució inicial repartint paquets...
    public void generateInitialSolution3 () {}
}