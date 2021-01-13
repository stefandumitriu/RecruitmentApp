import java.util.ArrayList;

public abstract class Department {
    private ArrayList<Employee> employees;
    private ArrayList<Job> jobs;

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

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
    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public void add(Job job) {
        jobs.add(job);
    }

    public abstract double getTotalSalaryBudget();

}
