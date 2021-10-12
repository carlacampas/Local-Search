package Abastecimiento;

import java.util.Scanner;

import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Gasolineras;

public class Main {
    public static void main(String[] args) {
    	Scanner sc = new Scanner(System.in);
    	int ngas, seedGas;
    	
    	System.out.println ("Introduce el numero de gasolineras: ");
    	ngas = sc.nextInt();
    	System.out.println ("Introduce la semilla para el generador de numeros aleatorios de gasolineras: ");
    	seedGas = sc.nextInt();
    	Gasolineras gasolineras = new Gasolineras (ngas, seedGas);
    	
    	int ncen, mult, seedCen;
    	System.out.println ("Introduce el numero de centros de distribucion: ");
    	ncen = sc.nextInt();
    	System.out.println ("Introduce la multiplicidad en la misma posicion: ");
    	mult = sc.nextInt();
    	System.out.println ("Introduce la semilla para el generador de numeros aleatorios de centros de distribucion: ");
    	seedCen = sc.nextInt();
    	CentrosDistribucion centrosDistrbucion = new CentrosDistribucion (ncen, mult, seedCen);
  
    	AbastecimientoState as = new AbastecimientoState (gasolineras, centrosDistrbucion);
        // genera solucio
        // imprimir estat
    	sc.close();
    }
}