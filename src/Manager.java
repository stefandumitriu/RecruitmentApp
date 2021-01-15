import java.util.ArrayList;
import java.util.Comparator;

public class Manager extends Employee{
    Manager(){}
    Manager(String name, String surname, String email, String phone, String gender) {
        super(name, surname, email, phone, gender);
    }
    ArrayList<Request<Job, Consumer>> applications;
    ArrayList<Request<Job, Consumer>> validReq;
    public void process(Job job) {
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
        for(int i = 0; i < job.noPositions; i++) {
            User newEmp = (User) validReq.get(i).getValue1();
            int unemployed = 0;
            for(User u : Application.users) {
                if(newEmp.resume.userInfo.getName().equals(u.resume.userInfo.getName()) &&
                        newEmp.resume.userInfo.getSurname().equals(u.resume.userInfo.getSurname())) {
                    unemployed = 1;
                    Application.users.remove(u);
                    break;
                }
            }
            if(unemployed == 1) {
                Application.getCompany(this.company).departments.get(0).employees.add(newEmp.convert());
            }
        }
        job.noPositions = 0;
        job.isOpen = false;
        applications.clear();;
    }
}
