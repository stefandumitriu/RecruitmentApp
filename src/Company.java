import java.util.ArrayList;

public class Company implements Subject {
    String name;
    Manager manager;
    ArrayList<Department> departments;
    ArrayList<Recruiter> recruiters;
    ArrayList<User> applicants;

    Company(String name, Manager manager) {
        this.name = name;
        this.manager = manager;
        departments = new ArrayList<>();
        recruiters = new ArrayList<>();
        applicants = new ArrayList<>();
    }

    public void add(Department department) {
        departments.add(department);
    }

    public void add(Recruiter recruiter) {
        recruiters.add(recruiter);
    }

    public void add(Employee employee, Department department) {
        department.add(employee);
    }


    @Override
    public void addObserver(User user) {
        if(!applicants.contains(user))
            applicants.add(user);
    }

    @Override
    public void removeObserver(User user) {
        applicants.remove(user);
    }

    @Override
    public void notifyAllObservers(Notification notification) {
        for(User u : applicants) {
            u.update(notification);
        }
    }
}
