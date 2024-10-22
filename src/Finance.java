import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Finance extends Department{

    @Override
    public double getTotalSalaryBudget() {
        double totalBudget = 0;
        for(Employee e : this.employees) {
            if(ChronoUnit.YEARS.between(LocalDate.now(), e.resume.experience.get(e.resume.experience.size() - 1).startYear) > 1) {
                totalBudget += (double) e.wage * 110/100;
            }
            else
                totalBudget += (double) e.wage * 116/100;
        }
        return totalBudget;
    }
}
