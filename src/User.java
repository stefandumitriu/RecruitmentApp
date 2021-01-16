import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class User extends Consumer implements Observer{
    User() {}
    User(String name, String surname, String email, String phone, String gender, ArrayList<Education> education, ArrayList<Experience> experience) throws ResumeIncompleteException {
        super(name, surname, email, phone, gender, education, experience);
        companiesInterest = new ArrayList<>();
    }
    ArrayList<String> companiesInterest;
    public Employee convert() throws ResumeIncompleteException {
        Employee e = new Employee(this.resume.userInfo.getName(), this.resume.userInfo.getSurname(), this.resume.userInfo.getEmail(),
                this.resume.userInfo.getPhone(), this.resume.userInfo.getGender(), this.resume.education, this.resume.experience);
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

    @Override
    public void update(Notification notification) {
//        switch (notification.notificationType) {
//            case "rejection" -> System.out.println("You have been rejected " + " @ " + notification.company.name + " @ " + notification.job.jobName);
//            case "hired" -> System.out.println("You have been hired " + " @ " + notification.company.name + " @ " + notification.job.jobName);
//            case "closed" -> System.out.println(notification.job.jobName + " @ " + notification.company.name + " has been closed");
//        }
    }
}
