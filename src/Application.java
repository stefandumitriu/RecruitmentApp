import java.util.ArrayList;

public class Application{
    private ArrayList<Company> companies;
    ArrayList<User> users;
    private ArrayList<String> company_names;

    Application() {
        companies = new ArrayList<>();
        users = new ArrayList<>();
        company_names = new ArrayList<>();
    }
    public ArrayList<Company> getCompanies() {
        return companies;
    };
    public Company getCompany(String name) {
        for(Company c : getCompanies()) {
            if(c.name.equals(name))
                return c;
        }
        return null;
    };

    public void add(Company company) {
        companies.add(company);
    }

    public void add(User user) {
        users.add(user);
    }

    public boolean remove(Company company) {
        if(companies.contains(company)) {
            companies.remove(company);
            return true;
        }
        return false;
    }

    public boolean remove(User user) {
        if(users.contains(user)) {
            users.remove(user);
            return true;
        }
        return false;
    }

    public ArrayList<Job> getJobs(ArrayList<String> companies) {
        return null;
    }
}
