import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class User extends Consumer{
    User() {}
    User(String name, String surname, String email, String phone, String gender) {
        super(name, surname, email, phone, gender);
        companiesInterest = new ArrayList<>();
    }
    ArrayList<String> companiesInterest;
    public Employee convert() {
        Employee e = new Employee(this.resume.userInfo.getName(), this.resume.userInfo.getSurname(), this.resume.userInfo.getEmail(),
                this.resume.userInfo.getPhone(), this.resume.userInfo.getGender());
        return e;
    }
    public Double getTotalScore() {
        double score = 0;
        int months = 0;
        for(Experience e : this.resume.experience) {
            months += e.startYear.until(e.endYear, ChronoUnit.MONTHS);
        }
        score = Math.ceil(months / 12.0) * 1.5 + this.meanGPA();
        return score;
    }
}
