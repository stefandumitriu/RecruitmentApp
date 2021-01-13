public class IT extends Department {
    @Override
    public double getTotalSalaryBudget() {
        double totalBudget = 0;
        for(Employee e : this.getEmployees()) {
            totalBudget += e.wage;
        }
        return totalBudget;
    }
}
