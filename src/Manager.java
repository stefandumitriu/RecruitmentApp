import java.util.ArrayList;
import java.util.Comparator;

public class Manager extends Employee{
    Manager(){}
    Manager(String name, String surname, String email, String phone, String gender, ArrayList<Education> education, ArrayList<Experience> experience) throws ResumeIncompleteException {
        super(name, surname, email, phone, gender, education, experience);
        applications = new ArrayList<>();
        validReq = new ArrayList<>();
    }
    ArrayList<Request<Job, Consumer>> applications;
    ArrayList<Request<Job, Consumer>> validReq;
    public void process(Job job) throws ResumeIncompleteException {
        for(Request<Job, Consumer> r: applications) {
            if(r.getKey().jobName.equals(job.jobName)) {
                validReq.add(r);
            }
        }
        validReq.sort((o1, o2) -> {
            if (o1.getScore().equals(o2.getScore()))
                return 0;
            else if (o1.getScore() > o2.getScore())
                return -1;
            else return 1;
        });
        int i = 0;
        while(job.noPositions != 0) {
            if(i == validReq.size()) {
                break;
            }
            User newEmp = (User) validReq.get(i).getValue1();
            i++;
            int unemployed = 0;
            for(User u : Application.getInstance().users) {
                if(newEmp.resume.userInfo.getName().equals(u.resume.userInfo.getName()) &&
                        newEmp.resume.userInfo.getSurname().equals(u.resume.userInfo.getSurname())) {
                    unemployed = 1;
                    u.update(new Notification(Application.getInstance().getCompany(this.company), job, "hired"));
                    for(Company c : Application.getInstance().companies) {
                        c.removeObserver(u);
                    }
                    Application.getInstance().users.remove(u);
                    for(Request r : applications) {
                        if(((User) r.getValue1()).resume.userInfo.getSurname().equals(newEmp.resume.userInfo.getSurname())) {
                            applications.remove(r);
                            break;
                        }
                    }
                    break;
                }
            }
            if(unemployed == 1) {
                job.noPositions--;
                Employee e = newEmp.convert();
                e.company = Application.getInstance().getCompany(this.company).name;
                Application.getInstance().getCompany(this.company).departments.get(0).employees.add(e);
                System.out.println(e.resume.userInfo.getName() + " " + e.resume.userInfo.getSurname() + " was hired @" + e.company +
                        " as a " + job.jobName);

            }
        }
        Company c = Application.getInstance().getCompany(this.company);
        if(job.noPositions == 0)
            c.notifyAllObservers(new Notification(c, job, "closed"));
        job.isOpen = false;
        validReq.clear();
    }
}
