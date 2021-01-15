import java.util.ArrayList;

public class Application{
    private static Application applicationInstance = null;
    ArrayList<Company> companies;
    ArrayList<User> users;
    ArrayList<Consumer> consumers;

    private Application() {
        companies = new ArrayList<>();
        users = new ArrayList<>();
        consumers = new ArrayList<>();
    }
    public static Application getInstance() {
        if(applicationInstance == null) {
            applicationInstance = new Application();
        }
        return applicationInstance;
    }
    public ArrayList<Company> getCompanies() {
        return companies;
    };
    public Company getCompany(String name) {
        for(Company c : Application.getInstance().companies) {
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
}
