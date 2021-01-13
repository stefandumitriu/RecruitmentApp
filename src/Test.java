import org.json.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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
                    System.out.println(Arrays.toString(name));
                    mainApp.getCompany(companyName).add(new Employee(name[0], name[1], null, null, null),
                            mainApp.getCompany(companyName).departments.get(j));
                }
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Test.Parser();
    }
}
