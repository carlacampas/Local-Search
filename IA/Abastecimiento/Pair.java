package Abastecimiento;

class Pair<L, R>
{
    L a;
    R b;

    Pair() {}

    Pair (L a, R b){
        this.a = a;
        this.b = b;
    }

    Pair (Pair <L, R> p){
        this.a = p.a;
        this.b = p.b;
    }

    void seta (L a){
        this.a = a;
    }

    void setb (R b){
        this.b = b;
    }

    void setValues (L a, R b){
        this.a = a;
        this.b = b;
    }
    
    L geta () {
    	return this.a;
    }
    
    R getb () {
    	return this.b;
    }
}