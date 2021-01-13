import java.time.LocalDate;

public class Experience implements Comparable {
    LocalDate startYear;
    LocalDate endYear;
    String position;
    String company;

    Experience(LocalDate startYear, LocalDate endYear, String position, String company) throws InvalidDatesException {
        if(endYear != null && startYear.isAfter(endYear)) {
            throw new InvalidDatesException();
        }
        else {
            this.startYear = startYear;
            this.endYear = endYear;
            this.position = position;
            this.company = company;
        }
    }
    @Override
    public int compareTo(Object o) {
        int res;
        if(endYear == null) {
            res = -this.startYear.compareTo(((Experience) o).startYear);
        }
        else {
            res = this.endYear.compareTo(((Experience) o).endYear);
        }
        if(res != 0)
            return -res;
        else return this.company.compareTo(((Experience) o).company);
    }
}
