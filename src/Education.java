import java.time.LocalDate;

public class Education implements Comparable<Education> {
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
    public int compareTo(Education o) {
        int res;
        if(gradYear == null) {
            res = -this.startYear.compareTo((o).startYear);
        }
        else {
            res = this.gradYear.compareTo((o).gradYear);
        }
        if(res != 0)
            return -res;
        else return -this.gradGPA.compareTo((o).gradGPA);
    }
}
class InvalidDatesException extends Exception {
    InvalidDatesException() {
        System.out.println("The dates are not in chronological order");
    }
}