public interface Subject {
    public void addObserver(User user);
    public void removeObserver(User user);
    public void notifyAllObservers(Notification notification);
}
