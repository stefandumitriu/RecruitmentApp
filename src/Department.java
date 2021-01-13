import java.util.ArrayList;

public abstract class Department {
    ArrayList<Employee> employees;
    ArrayList<Job> jobs;

    Department(){
        employees = new ArrayList<>();
        jobs = new ArrayList<>();
    }

    public void add(Employee employee) {
        employees.add(employee);
    }

    public void remove(Employee employee) {
        employees.remove(employee);
    }

    public void add(Job job) {
        jobs.add(job);
    }

    public abstract double getTotalSalaryBudget();

}
