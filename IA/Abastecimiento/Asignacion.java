package Abastecimiento;

class Asignacion {
	private Pair <Integer, Integer> p;
	private Pair <Integer, Integer> p1;
	
	Asignacion () {}
	
	Asignacion (Pair <Integer, Integer> a, Pair <Integer, Integer> b) {
		this.p = a;
		this.p1 = b;
	}
	
	Asignacion (Pair <Integer, Integer> a) {
		this.p = a;
		this.p1 = new Pair <Integer, Integer> (-1, -1);
	}
	
	Pair <Integer, Integer> first () {
		return this.p;
	}
	
	Pair <Integer, Integer> second () {
		return this.p1;
	}
	
	void setFirst (Pair <Integer, Integer> a) {
		this.p = a;
	}
	
	void setSecond (Pair <Integer, Integer> b) {
		this.p1 = b;
	}
	
	boolean secondIsEmpty () {
		return p1.a == -1;
	}
}

