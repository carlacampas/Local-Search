package Abastecimiento;

class Peticion {
	private Pair <Integer, Integer> p;
	
	Peticion () {}
	
	Peticion (Pair <Integer, Integer> a) {
		this.p = a;
	}
	
	Pair <Integer, Integer> get () {
		return this;
	}
	
	void set (Pair <Integer, Integer> a) {
		this.p = a;
	}
}

