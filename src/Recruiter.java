public class Recruiter extends Employee {
    Recruiter(String name, String surname, String email, String phone, String gender) {
        super(name, surname, email, phone, gender);
    }
    int rating = 5;
    public int evaluate(Job job, User user) {
        double score = rating * user.getTotalScore();
        Request r = new Request(job, user, this, score);
        /* company.manager.applications.add(Request)*/
        return (int) score;
    }
}
