public class Recruiter extends Employee {
    Recruiter() {}
    Recruiter(String name, String surname, String email, String phone, String gender) {
        super(name, surname, email, phone, gender);
    }
    int rating = 5;
    public int evaluate(Job job, User user) {
        double score = rating * user.getTotalScore();
        Request<Job, Consumer> r = new Request<Job, Consumer>(job, user, this, score);
        /* company.manager.applications.add(Request)*/
        return (int) score;
    }
}
