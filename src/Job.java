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
        Company c = Application.getInstance().getCompany(company);
        Application.getInstance().getCompany(this.company).addObserver(user);
        HashMap<Recruiter, Integer> degrees = new HashMap<>();
        assert c != null;
        for(Recruiter r : c.recruiters) {
            degrees.put(r, user.getDegreeInFriendship(r));
        }
        Map.Entry<Recruiter, Integer> prefEntry = null;
        int maxVal = Integer.MIN_VALUE;
        for(Map.Entry<Recruiter, Integer> e : degrees.entrySet()) {
            if(prefEntry == null || e.getValue().compareTo(prefEntry.getValue()) > 0) {
                prefEntry = e;
            }
        }
        Recruiter prefRec = prefEntry.getKey();
        if(!meetsRequirments(user)) {
            user.update(new Notification(Application.getInstance().getCompany(this.company), this, "rejection"));
        }
        else if(isOpen && meetsRequirments(user)) {
            prefRec.evaluate(this, user);
        }
    }

    public boolean meetsRequirments(User user) {
        int yearsOfExp = 0;
        for(Experience e : user.resume.experience) {
            yearsOfExp += e.endYear.getYear() - e.startYear.getYear();
        }
        if(!gradYear.verify(user.getGraduationYear())) {
            System.out.println(user.resume.userInfo.getSurname() + " application was rejected because of " + "gradYear @ " + company + " @ " + jobName);
        }
        else if(!expYears.verify(yearsOfExp)) {
            System.out.println(user.resume.userInfo.getSurname() + " application was rejected because of " + "yearsOfExp @ " + company + " @ " + jobName);
        }
        else if(!GPA.verify(user.meanGPA())) {
            System.out.println(user.resume.userInfo.getSurname() + " application was rejected because of " + "GPA @ " + company + " @ " + jobName);
        }
        return gradYear.verify(user.getGraduationYear()) &&
                expYears.verify(yearsOfExp) &&
                GPA.verify(user.meanGPA());
    }
}
