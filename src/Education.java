import java.time.LocalDate;

public class Education implements Comparable {
    LocalDate startYear;
    LocalDate gradYear;
    String institution;
    String level;
    Double gradGPA;
    Education(LocalDate startYear, LocalDate gradYear, String institution, String level, Double gradGPA) throws InvalidDatesException {
        if(gradYear != null && startYear.compareTo(gradYear) > 0) {
            throw new InvalidDatesException();
        }
        else {
            this.startYear = startYear;
            this.gradYear = gradYear;
            this.institution = institution;
            this.level = level;
            this.gradGPA = gradGPA;
        }
    }

    @Override
    public int compareTo(Object o) {
        int res;
        if(gradYear == null) {
            res = -this.startYear.compareTo(((Education) o).startYear);
        }
        else {
            res = this.gradYear.compareTo(((Education) o).gradYear);
        }
        if(res != 0)
            return -res;
        else return -this.gradGPA.compareTo(((Education) o).gradGPA);
    }
}
class InvalidDatesException extends Exception {
    InvalidDatesException() {
        System.out.println("The dates are not in chronological order");
    }
}