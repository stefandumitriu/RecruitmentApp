import java.util.*;
import java.util.stream.Collectors;

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
    public void apply(User user) {
        Company c = Application.getCompany(company);
        HashMap<Recruiter, Integer> degrees = new HashMap<>();
        assert c != null;
        for(Recruiter r : c.recruiters) {
            degrees.put(r, user.getDegreeInFriendship(r));
        }
        System.out.println(user.resume.userInfo.getSurname());
        for(Map.Entry e : degrees.entrySet()) {
            System.out.println(((Recruiter) e.getKey()).resume.userInfo.getSurname() + " " + e.getValue());
        }
        HashMap<Recruiter, Integer> sortedMap = degrees.entrySet().stream().sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, HashMap::new));
//        if(isOpen && meetsRequirments(user)) {
//            correctRecruiter.evaluate(this, user);
//        }
    }

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
