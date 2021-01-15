import org.json.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

public class Test {
    public static void Parser() throws IOException, InvalidDatesException {
        Path p_input = Paths.get("C:\\Users\\stefan.dumitriu\\Desktop\\tema_poo\\src\\input.json");
        String jsonStringInput = Files.readString(p_input);
        JSONObject o = new JSONObject(jsonStringInput);
        JSONArray companies = o.getJSONArray("companies");
        Application mainApp = new Application();
        for(int i = 0; i < companies.length(); i++) {
            JSONObject company = companies.getJSONObject(i);
            String[] managerName = ((String) company.get("manager")).split("\\s+");
            String companyName = (String) company.get("name");
            mainApp.add(new Company(companyName, new Manager(managerName[0], managerName[1], null, null, null)));
            JSONArray departments = company.getJSONArray("departments");
            for(int j = 0; j < departments.length(); j++) {
                JSONObject department = (JSONObject) departments.get(j);
                JSONArray employees = department.getJSONArray("employees");
                if((department.get("name")).equals("IT")) {
                    mainApp.getCompany(companyName).add(new IT());
                }
                else if((department.get("name")).equals("Management")) {
                    mainApp.getCompany(companyName).add(new Management());
                }
                else if((department.get("name")).equals("Marketing")) {
                    mainApp.getCompany(companyName).add(new Marketing());
                }
                else if((department.get("name")).equals("Finance")) {
                    mainApp.getCompany(companyName).add(new Finance());
                }
                for(int k = 0; k < employees.length(); k++) {
                    String[] name = ((String)((JSONObject) employees.get(k)).get("name")).split(" ");
                    JSONArray friends = ((JSONObject) employees.get(k)).getJSONArray("friends");
                    Employee newEmp = new Employee(name[0], name[1], null, null, null);
                    for(Object f : friends) {
                        String[] friendName = ((String) f).split(" ");
                        newEmp.add(new User(friendName[0], friendName[1], null, null, null));
                    }
                    mainApp.getCompany(companyName).add( newEmp, mainApp.getCompany(companyName).departments.get(j));
                }
                if(department.keySet().contains("jobs")){
                    JSONArray jobs = department.getJSONArray("jobs");
                    for(int k = 0; k < jobs.length(); k++) {
                        JSONObject job = (JSONObject) jobs.get(k);
                        int minGrad = 0, maxGrad = 0, minExp = 0, maxExp = 0;
                        double minGPA = 0, maxGPA = 0;
                        String jobName, jobCompany;
                        jobName = (String) job.get("jobName");
                        jobCompany = companyName;
                        int wage, noPositions;
                        wage = (Integer) job.get("wage");
                        noPositions = (Integer) job.get("noPositions");
                        if(!job.get("minGrad").equals(null)) {
                            minGrad = (int) job.get("minGrad");
                        }
                        else minGrad = Integer.MIN_VALUE;
                        if(!job.get("maxGrad").equals(null)) {
                            maxGrad = (int) job.get("maxGrad");
                        }
                        else maxGrad = Integer.MAX_VALUE;
                        if(!job.get("minExp").equals(null)) {
                            minExp = (int) job.get("minExp");
                        }
                        else minExp = Integer.MIN_VALUE;
                        if(!job.get("maxExp").equals(null)) {
                            maxExp = (int) job.get("maxExp");
                        }
                        else maxExp = Integer.MAX_VALUE;
                        if(!job.get("minGPA").equals(null)) {
                            minGPA =  ((BigDecimal) job.get("minGPA")).doubleValue();
                        }
                        else minGPA = Double.MIN_VALUE;
                        if(!job.get("maxGPA").equals(null)) {
                            maxGPA = (Double) job.get("maxGPA");
                        }
                        else maxGPA = Double.MAX_VALUE;
                        mainApp.getCompany(companyName).departments.get(j).jobs.add(new Job(jobName, jobCompany, new Constraint(minGrad, maxGrad),
                                new Constraint(minExp, maxExp), new Constraint(minGPA, maxGPA), noPositions, wage));
                    }
                }
            }
            JSONArray recruiters = company.getJSONArray("recruiters");
            for(Object rec : recruiters) {
                JSONArray recFriends = ((JSONObject) rec).getJSONArray("friends");
                String[] recName = ((String)((JSONObject) rec).get("name")).split(" ");
                Recruiter newRec = new Recruiter(recName[0], recName[1], null, null, null);
                for(Object f : recFriends) {
                    String[] friendName = ((String) f).split(" ");
                    newRec.add(new User(friendName[0], friendName[1], null, null, null));
                }
                mainApp.getCompany(companyName).recruiters.add(newRec);
            }
        }
        JSONArray users = o.getJSONArray("users");
        for(Object user : users) {
            String[] userName = ((String)((JSONObject) user).get("name")).split(" ");
            User newUser = new User(userName[0], userName[1], null, null, null);
            JSONArray friends = ((JSONObject) user).getJSONArray("friends");
            for(Object f : friends) {
                String[] friendName = ((String) f).split(" ");
                newUser.add(new User(friendName[0], friendName[1], null, null, null));
            }
            mainApp.add(newUser);
        }
        /* FROM HERE I PARSE CONSUMER.JSON FILE
        *
        *
        *
        *
        *
         */
        Path p_consumers = Paths.get("C:\\Users\\stefan.dumitriu\\Desktop\\tema_poo\\src\\consumers.json");
        String jsonStringConsumers = Files.readString(p_consumers);
        JSONObject content = new JSONObject(jsonStringConsumers);
        JSONArray employees = content.getJSONArray("employees");
        JSONArray recruiters = content.getJSONArray("recruiters");
        JSONArray usersObjectArray = content.getJSONArray("users");
        JSONArray managers = content.getJSONArray("managers");

        ArrayList<Employee> employeeArrayList = new ArrayList<>();
        ArrayList<User> userArrayList = new ArrayList<>();
        ArrayList<Recruiter> recruiterArrayList = new ArrayList<>();
        ArrayList<Manager> managerArrayList = new ArrayList<>();

        for(Object employee : employees) {
            JSONObject employeeObject = (JSONObject) employee;
            Employee ref = new Employee();
            Employee newEmp = (Employee) parseInformation(employeeObject, ref);
            newEmp.wage = (Integer) employeeObject.get("salary");
            employeeArrayList.add(newEmp);
        }

        for(Object recruiter : recruiters) {
            JSONObject recruiterObject = (JSONObject) recruiter;
            Recruiter ref = new Recruiter();
            Recruiter newRec = (Recruiter) parseInformation(recruiterObject, ref);
            recruiterArrayList.add(newRec);
        }

        for(Object user : usersObjectArray) {
            JSONObject userObject = (JSONObject) user;
            User ref = new User();
            User newUser = (User) parseInformation(userObject, ref);
            JSONArray prefCompanies = userObject.getJSONArray("interested_companies");
            for(Object comp : prefCompanies) {
                newUser.companiesInterest.add((String) comp);
            }
            userArrayList.add(newUser);
        }
        for(Object manager: managers) {
            JSONObject managerObject = (JSONObject) manager;
            Manager ref = new Manager();
            Manager newMan = (Manager) parseInformation(managerObject, ref);
            managerArrayList.add(newMan);
        }
        Application newApp = mergeInformation(mainApp, employeeArrayList, recruiterArrayList, managerArrayList, userArrayList);
        // Testing data
        for(Company c : newApp.getCompanies()) {
            System.out.println(c.name);
            System.out.println("Manager: ");
            System.out.println(c.manager.resume.userInfo.getName() + " "+ c.manager.resume.userInfo.getSurname());
            for(Education ed : c.manager.resume.education) {
                System.out.println(ed.institution + " -> " + ed.level);
            }
            for(Experience ex : c.manager.resume.experience) {
                System.out.println(ex.company + ": " + ex.startYear.getYear() + " -> " + ex.endYear.getYear());
            }
            for(Consumer f : c.manager.friends) {
                System.out.println(f.resume.userInfo.getName() + " " + f.resume.userInfo.getSurname() + ", ");
            }
            for(Department d : c.departments) {
                System.out.println(d.getClass().getName());
                System.out.println("Employees:");
                for(Employee e : d.employees) {
                    System.out.println(e.resume.userInfo.getName() + " "+ e.resume.userInfo.getSurname());
                    for(Education ed : e.resume.education) {
                        System.out.println(ed.institution + " -> " + ed.level);
                    }
                    for(Experience ex : e.resume.experience) {
                        if(e.resume.experience.indexOf(ex) == 0) {
                            System.out.println(ex.company + ": " + ex.startYear.getYear() + " -> " + ex.endYear.getYear()
                                    + " " + e.wage + "$");
                        }
                        else
                            System.out.println(ex.company + ": " + ex.startYear.getYear() + " -> " + ex.endYear.getYear());
                    }
                    for(Consumer f : e.friends) {
                        System.out.println(f.resume.userInfo.getName() + " " + f.resume.userInfo.getSurname() + ", ");
                    }
                }
                System.out.println("Jobs: ");
                for(Job j : d.jobs) {
                    System.out.println(j.wage + " " + j.jobName);
                }
            }
            System.out.println("Recruiters: ");
            for(Recruiter e : c.recruiters) {
                System.out.println(e.resume.userInfo.getName() + " "+ e.resume.userInfo.getSurname());
                for(Education ed : e.resume.education) {
                    System.out.println(ed.institution + " -> " + ed.level);
                }
                for(Experience ex : e.resume.experience) {
                    System.out.println(ex.company + ": " + ex.startYear.getYear() + " -> " + ex.endYear.getYear());
                }
                for(Consumer f : e.friends) {
                    System.out.println(f.resume.userInfo.getName() + " " + f.resume.userInfo.getSurname() + ", ");
                }
            }
        }
        System.out.println("Users:");
        for(User e : newApp.users) {
            System.out.println(e.resume.userInfo.getName() + " "+ e.resume.userInfo.getSurname());
            System.out.println("Interested in: ");
            for(String c : e.companiesInterest) {
                System.out.print(c + ", ");
            }
            System.out.print("\n");
            int yearsOfExp = 0;
            for(Experience ex : e.resume.experience) {
                System.out.println(ex.company + ": " + ex.startYear.getYear() + " -> " + ex.endYear.getYear());
                yearsOfExp += (int) ex.startYear.until(ex.endYear, ChronoUnit.MONTHS);
            }
            System.out.println((int) Math.ceil(yearsOfExp / 12.0));
            for(Consumer f : e.friends) {
                System.out.println(f.resume.userInfo.getName() + " " + f.resume.userInfo.getSurname() + ", ");
            }
        }
        for(User u : newApp.users) {
            for(String company : u.companiesInterest) {
                for(Job j : Application.getCompany(company).departments.get(0).jobs) {
                    j.apply(u);
                }
            }
        }
        for(Company c : mainApp.getCompanies()) {
            for(Job j : c.departments.get(0).jobs) {
                c.manager.process(j);
            }
        }
    }
    public static Application mergeInformation(Application mainApp, ArrayList<Employee> employees, ArrayList<Recruiter> recruiters,
                                        ArrayList<Manager> managers, ArrayList<User> users) {
        Application app;
        app = mainApp;
        for(Company c : app.getCompanies()) {
            for(Manager m : managers) {
                if(m.resume.userInfo.getName().equals(c.manager.resume.userInfo.getName()) &&
                        m.resume.userInfo.getSurname().equals(c.manager.resume.userInfo.getSurname())) {
                    m.company = c.name;
                    c.manager = m;
                    break;
                }
            }
            for(Department d: c.departments) {
                ArrayList<Employee> modifiedE = new ArrayList<>();
                for(Employee e : d.employees) {
                    for(Employee eConsumers : employees) {
                        if(e.resume.userInfo.getName().equals(eConsumers.resume.userInfo.getName()) &&
                                e.resume.userInfo.getSurname().equals(eConsumers.resume.userInfo.getSurname())) {
                            eConsumers.friends = new ArrayList<>(e.friends);
                            eConsumers.company = c.name;
                            modifiedE.add(eConsumers);
                            break;
                        }
                    }
                }
                d.employees = new ArrayList<>(modifiedE);
            }
            ArrayList<Recruiter> modifiedR = new ArrayList<>();
            for(Recruiter r : c.recruiters) {
                for(Recruiter rConsumers : recruiters) {
                    if(r.resume.userInfo.getName().equals(rConsumers.resume.userInfo.getName()) &&
                            r.resume.userInfo.getSurname().equals(rConsumers.resume.userInfo.getSurname())) {
                        rConsumers.friends = new ArrayList<>(r.friends);
                        rConsumers.company = c.name;
                        modifiedR.add(rConsumers);
                        break;
                    }
                }
                c.recruiters = new ArrayList<>(modifiedR);
            }
        }
        ArrayList<User> modifiedU = new ArrayList<>();
        for(User u : app.users) {
            for(User uConsumers : users) {
                if(u.resume.userInfo.getName().equals(uConsumers.resume.userInfo.getName()) &&
                        u.resume.userInfo.getSurname().equals(uConsumers.resume.userInfo.getSurname())) {
                    uConsumers.friends = new ArrayList<>(u.friends);
                    modifiedU.add(uConsumers);
                    break;
                }
            }
            app.users = new ArrayList<>(modifiedU);
        }
        return app;
    }
    public static Consumer parseInformation(JSONObject consumerObject, Object refObject) throws InvalidDatesException {
        Consumer c;
        String[] name = ((String) consumerObject.get("name")).split(" ");
        String email = (String) consumerObject.get("email");
        String phone = (String) consumerObject.get("phone");
        String gender = (String) consumerObject.get("genre");
        if(refObject instanceof Recruiter) {
            c = new Recruiter(name[0], name[1], email, phone, gender);
        }
        else if(refObject instanceof Manager) {
            c = new Manager(name[0], name[1], email, phone, gender);
        }
        else if(refObject instanceof Employee) {
            c = new Employee(name[0], name[1], email, phone, gender);
        }
        else {
            c = new User(name[0], name[1], email, phone, gender);
        }
        JSONArray languages = consumerObject.getJSONArray("languages");
        JSONArray languages_level = consumerObject.getJSONArray("languages_level");
        for(int i = 0; i < languages.length(); i++) {
            c.resume.userInfo.addLanguage((String) languages.get(i), (String) languages_level.get(i));
        }
        JSONArray educationArray = consumerObject.getJSONArray("education");
        for(Object ed : educationArray) {
            JSONObject edObject = (JSONObject) ed;
            String institutionName = (String) edObject.get("name");
            String level = (String) edObject.get("level");
            double GPA;
            if(edObject.get("grade") instanceof Integer) {
                GPA= ((Integer) edObject.get("grade")).doubleValue();
            }
            else
                GPA = ((BigDecimal) edObject.get("grade")).doubleValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate startYear = LocalDate.parse((String) edObject.get("start_date"), formatter);
            LocalDate endYear;
            if(!edObject.get("end_date").equals(null)) {
                endYear = LocalDate.parse((String) edObject.get("end_date"), formatter);
            }
            else
                endYear = LocalDate.now();
            c.resume.education.add(new Education(startYear, endYear, institutionName, level, GPA));
        }
        JSONArray experienceArray = consumerObject.getJSONArray("experience");
        for(Object ex : experienceArray) {
            JSONObject exObject = (JSONObject) ex;
            String companyName = (String) exObject.get("company");
            String job = (String) exObject.get("position");
            String department = null;
            if(exObject.keySet().contains("departament")) {
                department = (String) exObject.get("departament");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate startYear = LocalDate.parse((String) exObject.get("start_date"), formatter);
            LocalDate endYear;
            if(!exObject.get("end_date").equals(null)) {
                endYear = LocalDate.parse((String) exObject.get("end_date"), formatter);
            }
            else
                endYear = LocalDate.now();
            c.resume.experience.add(new Experience(startYear, endYear, job, companyName, department));
        }
        if(c instanceof Recruiter) {
            for(Experience ex : c.resume.experience) {
                ex.department = "IT";
            }
        }
        Collections.sort(c.resume.education);
        Collections.sort(c.resume.experience);
        return c;
    }
    public static void main(String[] args) throws IOException, InvalidDatesException {
        Test.Parser();
    }
}
