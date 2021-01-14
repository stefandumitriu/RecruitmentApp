import java.util.ArrayList;

public class Employee extends Consumer {
    int wage;
    String company;
    Employee() {

    }
    Employee(String name, String surname, String email, String phone, String gender) {
        super(name, surname, email, phone, gender);
    }
    Employee(String name, String surname, String email, String phone, String gender, int wage, String company) {
        this(name, surname, email, phone, gender);
        this.wage = wage;
        this.company = company;
    }
}
