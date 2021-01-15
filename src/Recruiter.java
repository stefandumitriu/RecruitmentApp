import java.util.ArrayList;

public class Recruiter extends Employee {
    int rating;
    Recruiter() {}
    Recruiter(String name, String surname, String email, String phone, String gender) {
        super(name, surname, email, phone, gender);
        rating = 5;
    }
    public int evaluate(Job job, User user) {
        double score = rating * user.getTotalScore();
        rating += 0.1;
        Request<Job, Consumer> r = new Request<Job, Consumer>(job, user, this, score);
        if(Application.getCompany(this.company) != null) {
            Application.getCompany(this.company).manager.applications.add(r);
        }
        return (int) score;
    }
}
