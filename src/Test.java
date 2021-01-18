import org.json.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class Test {
    public static void Parser() throws IOException, InvalidDatesException, ResumeIncompleteException {
        Application mainApp = Application.getInstance();
        DepartmentFactory departmentFactory = new DepartmentFactory();
        /* FROM HERE I PARSE CONSUMER.JSON FILE
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
        JSONArray users = content.getJSONArray("users");
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
            Application.getInstance().consumers.add(newEmp);
        }

        for(Object recruiter : recruiters) {
            JSONObject recruiterObject = (JSONObject) recruiter;
            Recruiter ref = new Recruiter();
            Recruiter newRec = (Recruiter) parseInformation(recruiterObject, ref);
            recruiterArrayList.add(newRec);
            Application.getInstance().consumers.add(newRec);
        }

        for(Object user : users) {
            JSONObject userObject = (JSONObject) user;
            User ref = new User();
            User newUser = (User) parseInformation(userObject, ref);
            JSONArray prefCompanies = userObject.getJSONArray("interested_companies");
            for(Object comp : prefCompanies) {
                newUser.companiesInterest.add((String) comp);
            }
            userArrayList.add(newUser);
            Application.getInstance().consumers.add(newUser);
        }
        for(Object manager: managers) {
            JSONObject managerObject = (JSONObject) manager;
            Manager ref = new Manager();
            Manager newMan = (Manager) parseInformation(managerObject, ref);
            managerArrayList.add(newMan);
            Application.getInstance().consumers.add(newMan);
        }
        /* FROM HERE I MERGE WITH DATA FROM INPUT
        *
        *
        *
        *
         */
        Path p_input = Paths.get("C:\\Users\\stefan.dumitriu\\Desktop\\tema_poo\\src\\input.json");
        String jsonStringInput = Files.readString(p_input);
        JSONObject o = new JSONObject(jsonStringInput);
        JSONArray companies = o.getJSONArray("companies");
        for(int i = 0; i < companies.length(); i++) {
            JSONObject company = companies.getJSONObject(i);
            String[] managerName = ((String) company.get("manager")).split("\\s+");
            String companyName = (String) company.get("name");
            Manager newMan = (Manager) Consumer.searchByName(managerName[1]);
            newMan.company = companyName;
            mainApp.add(new Company(companyName, newMan));
            JSONArray departments = company.getJSONArray("departments");
            for(int j = 0; j < departments.length(); j++) {
                JSONObject department = (JSONObject) departments.get(j);
                JSONArray employeesInput = department.getJSONArray("employees");
                mainApp.getCompany(companyName).add(departmentFactory.getDepartment((String) department.get("name")));
                for(int k = 0; k < employeesInput.length(); k++) {
                    String[] name = ((String)((JSONObject) employeesInput.get(k)).get("name")).split(" ");
                    JSONArray friends = ((JSONObject) employeesInput.get(k)).getJSONArray("friends");
                    Employee newEmp = (Employee) Consumer.searchByName(name[1]);
                    newEmp.company = companyName;
                    for(Object f : friends) {
                        String[] friendName = ((String) f).split(" ");
                        newEmp.add(Consumer.searchByName(friendName[1]));
                    }
                    mainApp.getCompany(companyName).add(newEmp, mainApp.getCompany(companyName).departments.get(j));
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
            JSONArray recruitersInput = company.getJSONArray("recruiters");
            for(Object rec : recruitersInput) {
                JSONArray recFriends = ((JSONObject) rec).getJSONArray("friends");
                String[] recName = ((String)((JSONObject) rec).get("name")).split(" ");
                Recruiter newRec = (Recruiter) Consumer.searchByName(recName[1]);
                newRec.company = companyName;
                for(Object f : recFriends) {
                    String[] friendName = ((String) f).split(" ");
                    newRec.add(Consumer.searchByName(friendName[1]));
                }
                mainApp.getCompany(companyName).recruiters.add(newRec);
            }
        }
        JSONArray usersInput = o.getJSONArray("users");
        for(Object user : usersInput) {
            String[] userName = ((String)((JSONObject) user).get("name")).split(" ");
            User newUser = (User) Consumer.searchByName(userName[1]);
            JSONArray friends = ((JSONObject) user).getJSONArray("friends");
            for(Object f : friends) {
                String[] friendName = ((String) f).split(" ");
                newUser.add(Consumer.searchByName(friendName[1]));
            }
            mainApp.add(newUser);
        }
        // Testing data
        printDataInput(mainApp);
        // Applying process
//        for(User u : mainApp.users) {
//            for(String company : u.companiesInterest) {
//                for(Job j : Application.getInstance().getCompany(company).departments.get(0).jobs) {
//                    j.apply(u);
//                }
//            }
//        }
//        for(Company c : mainApp.getCompanies()) {
//            for(Job j : c.departments.get(0).jobs) {
//                c.manager.process(j);
//            }
//        }
    }
    public static void printDataInput(Application newApp) {
        for(Company c : newApp.getCompanies()) {
            System.out.println("Company: " + c.name);
            System.out.print("Manager: ");
            System.out.println(c.manager.resume.userInfo.getName() + " "+ c.manager.resume.userInfo.getSurname());
            for(Education ed : c.manager.resume.education) {
                System.out.println(ed.institution + " -> " + ed.level);
            }
            for(Experience ex : c.manager.resume.experience) {
                System.out.println(ex.company + ": " + ex.startYear.getYear() + " -> " + ex.endYear.getYear());
            }
            System.out.print("Friends: ");
            for(Consumer f : c.manager.friends) {
                if(c.manager.friends.indexOf(f) == c.manager.friends.size() - 1) {
                    System.out.print(f.resume.userInfo.getName() + " " + f.resume.userInfo.getSurname() + "\n");
                }
                else
                    System.out.print(f.resume.userInfo.getName() + " " + f.resume.userInfo.getSurname() + "; ");
            }
            System.out.println("");
            for(Department d : c.departments) {
                System.out.println("Dept.: " + d.getClass().getName());
                System.out.println("Employees:\n");
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
                    System.out.print("Friends: ");
                    for(Consumer f : e.friends) {
                        if(e.friends.indexOf(f) == e.friends.size() - 1) {
                            System.out.print(f.resume.userInfo.getName() + " " + f.resume.userInfo.getSurname() + "\n\n");
                        }
                        else
                            System.out.print(f.resume.userInfo.getName() + " " + f.resume.userInfo.getSurname() + "; ");
                    }
                    System.out.println();
                }
                System.out.println("Jobs: ");
                for(Job j : d.jobs) {
                    System.out.println(j.wage + " " + j.jobName);
                }
                System.out.println("");
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
                System.out.print("Friends: ");
                for(Consumer f : e.friends) {
                    if(e.friends.indexOf(f) == e.friends.size() - 1) {
                        System.out.print(f.resume.userInfo.getName() + " " + f.resume.userInfo.getSurname() + "\n\n");
                    }
                    else
                        System.out.print(f.resume.userInfo.getName() + " " + f.resume.userInfo.getSurname() + "; ");
                }
            }
        }
        System.out.println("Users:");
        for(User e : newApp.users) {
            System.out.println(e.resume.userInfo.getName() + " "+ e.resume.userInfo.getSurname());
            System.out.print("Interested in: ");
            for(String c : e.companiesInterest) {
                System.out.print(c + "; ");
            }
            System.out.print("\n");
            for(Education ed : e.resume.education) {
                System.out.println(ed.institution + " -> " + ed.level);
            }
            for(Experience ex : e.resume.experience) {
                System.out.println(ex.company + ": " + ex.startYear.getYear() + " -> " + ex.endYear.getYear());
            }
            System.out.print("Friends: ");
            for(Consumer f : e.friends) {
                if(e.friends.indexOf(f) == e.friends.size() - 1) {
                    System.out.print(f.resume.userInfo.getName() + " " + f.resume.userInfo.getSurname() + "\n\n");
                }
                else
                    System.out.print(f.resume.userInfo.getName() + " " + f.resume.userInfo.getSurname() + "; ");
            }
        }
    }
    public static Consumer parseInformation(JSONObject consumerObject, Object refObject) throws InvalidDatesException, ResumeIncompleteException {
        Consumer c;
        ArrayList<Education> tempEducation = new ArrayList<>();
        ArrayList<Experience> tempExperience = new ArrayList<>();
        String[] name = ((String) consumerObject.get("name")).split(" ");
        String email = (String) consumerObject.get("email");
        String phone = (String) consumerObject.get("phone");
        String gender = (String) consumerObject.get("genre");
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
            tempEducation.add(new Education(startYear, endYear, institutionName, level, GPA));
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
            tempExperience.add(new Experience(startYear, endYear, job, companyName, department));
        }
        if(refObject instanceof Recruiter) {
            c = new Recruiter(name[0], name[1], email, phone, gender, tempEducation, tempExperience);
        }
        else if(refObject instanceof Manager) {
            c = new Manager(name[0], name[1], email, phone, gender, tempEducation, tempExperience);
        }
        else if(refObject instanceof Employee) {
            c = new Employee(name[0], name[1], email, phone, gender, tempEducation, tempExperience);
        }
        else {
            c = new User(name[0], name[1], email, phone, gender, tempEducation, tempExperience);
        }
        if(c instanceof Recruiter) {
            for(Experience ex : c.resume.experience) {
                ex.department = "IT";
            }
        }
        JSONArray languages = consumerObject.getJSONArray("languages");
        JSONArray languages_level = consumerObject.getJSONArray("languages_level");
        for(int i = 0; i < languages.length(); i++) {
            c.resume.userInfo.addLanguage((String) languages.get(i), (String) languages_level.get(i));
        }
        Collections.sort(c.resume.education);
        Collections.sort(c.resume.experience);
        return c;
    }
    public static void main(String[] args) throws IOException, InvalidDatesException, ResumeIncompleteException {
        Test.Parser();
    }
}