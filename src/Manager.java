import java.util.ArrayList;
import java.util.Comparator;

public class Manager extends Employee{
    Manager(String name, String surname, String email, String phone, String gender) {
        super(name, surname, email, phone, gender);
    }
    ArrayList<Request> applications;
    ArrayList<Request> validReq;
    public void process(Job job) {
        for(Request r: applications) {
            if(r.getKey() == job) {
                validReq.add(r);
            }
        }
        validReq.sort(new Comparator<Request>() {
            @Override
            public int compare(Request o1, Request o2) {
                if(o1.getScore().equals(o2.getScore()))
                    return 0;
                else if(o1.getScore() > o2.getScore())
                    return -1;
                else return 1;
            }
        });
        // HITE FIRST NO_POSITIONS IN validReq LIST
    }
}
