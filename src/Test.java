import org.json.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.Arrays;

public class Test {
    public static void Parser() throws IOException {
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
        // Testing input file parsing
        for(Company company : mainApp.getCompanies()) {
            System.out.println(company.name);
            System.out.println("Recruiters:");
            for(Recruiter r : company.recruiters) {
                System.out.println(r.resume.userInfo.getName() + " " + r.resume.userInfo.getSurname() + " has friends:");
                for(Consumer c : r.friends) {
                    System.out.print(c.resume.userInfo.getName() + " " + c.resume.userInfo.getSurname() +", ");
                }
                System.out.println("");
            }
            for(Department d : company.departments) {
                System.out.println(d.getClass().getName());
                for(Job test : d.jobs) {
                    System.out.println(test.wage + " " + test.jobName);
                }
                for(Employee e : d.employees) {
                    System.out.println(e.resume.userInfo.getName() + " " + e.resume.userInfo.getSurname() + " has friends:");
                    for(Consumer c : e.friends) {
                        System.out.print(c.resume.userInfo.getName() + " " + c.resume.userInfo.getSurname() +", ");
                    }
                    System.out.println("");
                }
            }
        }
        System.out.println("Users:");
        for(User u : mainApp.users) {
            System.out.println(u.resume.userInfo.getName() + " " + u.resume.userInfo.getSurname() + " has friends:");
            for(Consumer c : u.friends) {
                System.out.print(c.resume.userInfo.getName() + " " + c.resume.userInfo.getSurname() +", ");
            }
            System.out.println("");
        }
    }
    public static void main(String[] args) throws IOException {
        Test.Parser();
    }
}
