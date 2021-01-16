public class DepartmentFactory {
    public Department getDepartment(String department) {
        if(department == null)
            return null;
        if(department.equals("IT"))
            return new IT();
        else if(department.equals("Management"))
            return new Management();
        else if(department.equals("Marketing"))
            return new Marketing();
        else if(department.equals("Finance"))
            return new Finance();
        return null;
    }
}
