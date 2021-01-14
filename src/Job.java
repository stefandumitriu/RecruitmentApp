import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Job {
    String jobName;
    String company;
    boolean isOpen;
    Constraint gradYear, expYears, GPA;
    ArrayList<User> applicants;
    int noPositions;
    int wage;
    Job(String jobName, String company, Constraint gradYear, Constraint expYears, Constraint GPA, int noPositions, int wage) {
        this.jobName = jobName;
        this.company = company;
        this.gradYear = gradYear;
        this.expYears = expYears;
        this.GPA = GPA;
        this.noPositions = noPositions;
        this.wage = wage;
        isOpen = true;
        applicants = new ArrayList<>();
    }
//    public void apply(User user) {
//        Company c = Application.getCompany(company);
//        HashMap<Recruiter, Integer> degrees = new HashMap<>();
//        assert c != null;
//        for(Recruiter r : c.recruiters) {
//            degrees.put(r, user.getDegreeInFriendship(r));
//        }
//        ArrayList<Integer> recruiterScore = (ArrayList<Integer>) degrees.values();
//        recruiterScore.sort(null);
//        Integer getRecruiterValue = recruiterScore.get(recruiterScore.size() - 1);
//        Recruiter correctRecruiter = (Recruiter) degrees.entrySet().stream().filter(entry -> getRecruiterValue.equals(entry.getValue())).map(Map.Entry::getKey);
//        int score = correctRecruiter.evaluate(this, user);
//
//    }

    public boolean meetsRequirments(User user) {
        int yearsOfExp = 0;
        for(Experience e : user.resume.experience) {
            yearsOfExp += e.endYear.getYear() - e.startYear.getYear();
        }
        return gradYear.verify(user.getGraduationYear()) &&
                expYears.verify(yearsOfExp) &&
                GPA.verify(user.meanGPA());
    }
}
