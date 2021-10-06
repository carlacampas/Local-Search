package Abastecimiento;

// IMPORTS.
import java.util.*;

public class AbastecimientoState {
    // ATTRIBUTES.
    private ArrayList<ArrayList<Pair <int, int>>> assignments;
    private ArrayList<int> distances; // distancia que le queda por recorrer al camión
    Gasolineras gasolineras;

    // CONSTRUCTORS.
    public AbastecimientoState (Gasolineras gasolineras){
        this.assignments = new ArrayList <> ();
        this.distances = new ArrayList <> ();
        this.gasolineras = gasolineras;
    }

    public AbastecimientoState (ArrayList <ArrayList<Integer>> assignments, ArrayList <Integer> distances, Gasolineras gasolineras) {
        this.assignments = assignments;
        this.distances = distances;
        this.gasolineras = gasolineras;
    }
    // SETTERS.
    public ArrayList <ArrayList<Integer>> setAssigments(ArrayList <ArrayList<Integer>> assignments) {
        this.assignments = assignments;
    }

    public ArrayList<Integer> setDistances (ArrayList <Integer> distances) {
        this.distances = distances;
    }

    // GETTERS.
    public ArrayList<ArrayList<Integer>> getAssignments() {
        return assignments;
    }

    public ArrayList<Integer> getDistances () {
        return distances;
    }

    // OPERADORS.
    public void assignaPeticion (/*id camion, peticion*/) {}

    public void intercambiaPeticiones (/*p, p1 peticiones, c, c1 camiones*//) {}

    public void intercambioOrden (/*p, p1 peticiones, c camion*/) {}

    public void cambiaPeticion (/*p peticion, c, c1 camiones*/) {}

    // INITIAL STATE.
    //potseer ni ha més d'una
    public void generateInitialSolution () {}
}