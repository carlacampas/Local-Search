package Abastecimiento;

// IMPORTS.
import java.util.*;

public class AbastecimientoState {
    // Attributes.
    private ArrayList<ArrayList<Integer>> assignments;
    private ArrayList<Integer> distances;
    Gasolineras gasolineras;

    // Constructors.
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
    public void move () {}

    public void delete () {}

    // INITIAL STATE.
    //potseer ni ha més d'una
    public void generateInitialSolution () {}
}