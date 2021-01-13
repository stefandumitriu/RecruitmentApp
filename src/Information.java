import java.util.HashMap;

public class Information{
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String gender;
    private HashMap<String, String> languages;

    public Information(String name, String surname, String email, String phone, String gender) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        languages = new HashMap<>();
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public String getGender() {
        return gender;
    }
    public HashMap<String, String> getLanguages() {
        return languages;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void addLanguage(String language, String level) {
        getLanguages().put(language, level);
    }
}