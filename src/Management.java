public class Management extends Department{
    @Override
    public double getTotalSalaryBudget() {
        double totalBudget = 0;
        for(Employee e : this.employees) {
            totalBudget += (double) e.wage * 116/100;
        }
        return totalBudget;
    }
}
