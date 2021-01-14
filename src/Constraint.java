public class Constraint {
    double inf;
    double sup;
    Constraint(double inf, double sup) {
        this.inf = inf;
        this.sup = sup;
    }
    public boolean verify(double val) {
        return val >= inf && val <= sup;
    }
}
