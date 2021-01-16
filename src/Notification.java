public class Notification {
    String notificationType;
    Company company;
    Job job;
    public Notification(Company company, Job job, String notificationType) {
        this.company = company;
        this.job = job;
        this.notificationType = notificationType;
    }
}
