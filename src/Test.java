import org.json.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
        // Testing input file parsing
//        for(Company company : mainApp.getCompanies()) {
//            System.out.println(company.name);
//            System.out.println("Recruiters:");
//            for(Recruiter r : company.recruiters) {
//                System.out.println(r.resume.userInfo.getName() + " " + r.resume.userInfo.getSurname() + " has friends:");
//                for(Consumer c : r.friends) {
//                    System.out.print(c.resume.userInfo.getName() + " " + c.resume.userInfo.getSurname() +", ");
//                }
//                System.out.println("");
//            }
//            for(Department d : company.departments) {
//                System.out.println(d.getClass().getName());
//                for(Job test : d.jobs) {
//                    System.out.println(test.wage + " " + test.jobName);
//                }
//                for(Employee e : d.employees) {
//                    System.out.println(e.resume.userInfo.getName() + " " + e.resume.userInfo.getSurname() + " has friends:");
//                    for(Consumer c : e.friends) {
//                        System.out.print(c.resume.userInfo.getName() + " " + c.resume.userInfo.getSurname() +", ");
//                    }
//                    System.out.println("");
//                }
//            }
//        }
//        System.out.println("Users:");
//        for(User u : mainApp.users) {
//            System.out.println(u.resume.userInfo.getName() + " " + u.resume.userInfo.getSurname() + " has friends:");
//            for(Consumer c : u.friends) {
//                System.out.print(c.resume.userInfo.getName() + " " + c.resume.userInfo.getSurname() +", ");
//            }
//            System.out.println("");
//        }
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

        ArrayList<Employee> employeeArrayList = new ArrayList<>();
        ArrayList<User> userArrayList = new ArrayList<>();
        ArrayList<Recruiter> recruiterArrayList = new ArrayList<>();
        ArrayList<Manager> managerArrayList = new ArrayList<>();

        for(Object employee : employees) {
            JSONObject employeeObject = (JSONObject) employee;
            String[] name = ((String) employeeObject.get("name")).split(" ");
            String email = (String) employeeObject.get("email");
            String phone = (String) employeeObject.get("phone");
            String gender = (String) employeeObject.get("genre");
            int wage = (Integer) employeeObject.get("salary");
            Employee e = new Employee(name[0], name[1], email, phone, gender);
            e.wage = wage;
            JSONArray languages = employeeObject.getJSONArray("languages");
            JSONArray languages_level = employeeObject.getJSONArray("languages_level");
            for(int i = 0; i < languages.length(); i++) {
                e.resume.userInfo.addLanguage((String) languages.get(i), (String) languages_level.get(i));
            }
            employeeArrayList.add(e);
            JSONArray educationArray = employeeObject.getJSONArray("education");
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
                e.resume.education.add(new Education(startYear, endYear, institutionName, level, GPA));
            }
            JSONArray experienceArray = employeeObject.getJSONArray("experience");
            for(Object ex : experienceArray) {
                JSONObject exObject = (JSONObject) ex;
                String companyName = (String) exObject.get("company");
                String job = (String) exObject.get("position");
                String department = (String) exObject.get("departament");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate startYear = LocalDate.parse((String) exObject.get("start_date"), formatter);
                LocalDate endYear;
                if(!exObject.get("end_date").equals(null)) {
                    endYear = LocalDate.parse((String) exObject.get("end_date"), formatter);
                }
                else
                    endYear = LocalDate.now();
                e.resume.experience.add(new Experience(startYear, endYear, job, companyName, department));
            }
            Collections.sort(e.resume.education);
            Collections.sort(e.resume.experience);
        }
        // Testing employee input from consumers.json
//        for(Employee e : employeeArrayList) {
//            System.out.println(e.resume.userInfo.getName() + " "+ e.resume.userInfo.getSurname());
//            for(Education ed : e.resume.education) {
//                System.out.println(ed.institution + " -> " + ed.level);
//            }
//            for(Experience ex : e.resume.experience) {
//                System.out.println(ex.company + ": " + ex.startYear.getYear() + " -> " + ex.endYear.getYear());
//            }
//        }
    }
    public static void main(String[] args) throws IOException, InvalidDatesException {
        Test.Parser();
    }
}
