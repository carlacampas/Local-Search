package Abastecimiento;

// IMPORTS.
import java.util.*;

import IA.Gasolina.Gasolineras;
import IA.Gasolina.CentrosDistribucion;
//import aima.search.informed.HillClimbingSearch;
//import aima.search.informed.SimulatedAnnealingSearch;
//import aima.search.framework.Problem;
//import aima.search.framework.Search;
//import aima.search.framework.SearchAgent;

public class AbastecimientoState {
    // ATRIBUTOS.
    final static int maxDist = 640;

    Gasolineras gasolineras;
    CentrosDistribucion centrosDistibucion;

    private ArrayList<ArrayList<Integer>> asignaciones;
    private ArrayList <Integer> distancias;

    // CONSTRUCTORS.
    public AbastecimientoState (Gasolineras gasolineras, CentrosDistribucion centrosDistribucion){
        this.gasolineras = gasolineras;
        this.centrosDistibucion = centrosDistribucion;

        this.asignaciones = new ArrayList <> (centrosDistribucion.size());
        this.distancias = new ArrayList <> (centrosDistribucion.size());

        for (int i=0; i<centrosDistribucion.size(); i++){
            this.distancias.set(i, maxDist);
        }
    }

    public AbastecimientoState (ArrayList <ArrayList<Integer>> asignaciones, ArrayList <Integer> distancias,
                                Gasolineras gasolineras, CentrosDistribucion centrosDistribucion) {
        this.asignaciones = asignaciones;
        this.distancias = distancias;
        this.gasolineras = gasolineras;
        this.centrosDistibucion = centrosDistribucion;
    }

    // SETTERS.
    public void setAssigments(ArrayList <ArrayList<Integer>> asignaciones) {
        this.asignaciones = asignaciones;
    }

    public void setDistances (ArrayList <Integer> distancias) {
        this.distancias = distancias;
    }

    // GETTERS.
    public ArrayList<ArrayList<Integer>> getAsignaciones() {
        return asignaciones;
    }

    public ArrayList<Integer> getDistancias () {
        return distancias;
    }

    // OPERADORS.
    // Las peticiones seran identificadas asi: Pair <Integer, Integer> p = (id peticion, id gasolinera)
    // Los camiones seran identificados con su propio Id Integer
    public void assignaPeticion (Integer camion, Pair <Integer, Integer> peticion) {

	} 

    public void intercambiaPeticiones (Integer p, Integer p1, int c, int c1){}

    public void intercambioOrden (/*p, p1 peticiones, c camion*/) {}

    public void cambiaPeticion (/*p peticion, c, c1 camiones*/) {}

    // INITIAL SOLUTION.
    // Genera solució inicial repartint paquets equitativament entre tots els paquets de forma aleatoria.
    public void generateInitialSolution1 () {}

    // Genera solució inicial repartint paquets equitativament entre tots els paquets amb ponderacions dels costos i
    // beneficis.
    public void generateInitialSolution2 () {}

    // Genera solució inicial repartint paquets...
    public void generateInitialSolution3 () {}
}