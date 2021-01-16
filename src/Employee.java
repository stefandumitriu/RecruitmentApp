import java.util.ArrayList;

public class Employee extends Consumer {
    int wage;
    String company;
    Employee() {

    }
    Employee(String name, String surname, String email, String phone, String gender, ArrayList<Education> education, ArrayList<Experience> experience) throws ResumeIncompleteException {
        super(name, surname, email, phone, gender, education, experience);
    }
}
