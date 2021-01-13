public class IT extends Department {
    @Override
    public double getTotalSalaryBudget() {
        double totalBudget = 0;
        for(Employee e : this.employees) {
            totalBudget += e.wage;
        }
        return totalBudget;
    }
}
