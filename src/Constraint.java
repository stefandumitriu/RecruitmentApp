public class Constraint {
    int inf;
    int sup;
    Constraint(int inf, int sup) {
        this.inf = inf;
        this.sup = sup;
    }
    public boolean verify(double val) {
        return val >= inf && val <= sup;
    }
}
