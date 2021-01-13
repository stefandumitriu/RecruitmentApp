public class Marketing extends Department{

    @Override
    public double getTotalSalaryBudget() {
        double totalBudget = 0;
        for(Employee e : this.employees) {
            if(e.wage > 5000)
                totalBudget += (double) e.wage * 110/100;
            else if (e.wage < 3000)
                totalBudget += e.wage;
            else totalBudget += (double) e.wage * 116/100;
        }
        return totalBudget;
    }
}
