import java.time.LocalDate;

public class Experience implements Comparable<Experience> {
    LocalDate startYear;
    LocalDate endYear;
    String position;
    String company;
    String department;

    Experience(LocalDate startYear, LocalDate endYear, String position, String company, String department) throws InvalidDatesException {
        if(endYear != null && startYear.isAfter(endYear)) {
            throw new InvalidDatesException();
        }
        else {
            this.startYear = startYear;
            this.endYear = endYear;
            this.position = position;
            this.company = company;
            this.department = department;
        }
    }
    @Override
    public int compareTo(Experience o) {
        int res;
        if(endYear == null) {
            res = -this.startYear.compareTo((o).startYear);
        }
        else {
            res = this.endYear.compareTo((o).endYear);
        }
        if(res != 0)
            return -res;
        else return this.company.compareTo((o).company);
    }
}
