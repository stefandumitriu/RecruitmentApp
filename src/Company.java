import java.util.ArrayList;

public class Company {
    String name;
    Manager manager;
    ArrayList<Department> departments;
    ArrayList<Recruiter> recruiters;

    Company(String name, Manager manager) {
        this.name = name;
        this.manager = manager;
        departments = new ArrayList<>();
        recruiters = new ArrayList<>();
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
    

}
