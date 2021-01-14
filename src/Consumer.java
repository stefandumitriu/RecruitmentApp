import java.lang.reflect.Array;
import java.util.*;

public abstract class Consumer {
    ArrayList<Consumer> friends;
    Resume resume;
    Consumer() {}
    Consumer(String name, String surname, String email, String phone, String gender) {
        this.resume = new Resume(name, surname, email, phone, gender);
        friends = new ArrayList<>();
    }
    public void add(Education education) {
        resume.education.add(education);
    }

    public void add(Experience experience) {
        resume.experience.add(experience);
    }

    public void add(Consumer consumer) {
        friends.add(consumer);
    }

    public int getDegreeInFriendship(Consumer consumer) {
        int degree = 0;
        HashSet<Consumer> visited = new HashSet<>();
        HashMap<Consumer, Integer> count = new HashMap<>();
        LinkedList<Consumer> q = new LinkedList<>();
        visited.add(friends.get(0));
        q.push(friends.get(0));
        while(!q.isEmpty()) {
            Consumer temp = q.pop();
            degree++;
            for(Consumer c : temp.friends) {
                if(!visited.contains(c)) {
                    visited.add(c);
                    count.put(c, degree);
                    q.push(c);
                    if(c == consumer)
                        return count.get(c);
                }
            }
        }
        return -1;
    }

    public void remove(Consumer consumer) {
        friends.remove(consumer);
    }

    public Integer getGraduationYear() {
        return resume.education.get(0).gradYear.getYear();
    }

    public Double meanGPA() {
        double gpa = 0;
        double i = 0;
        for(Education e : resume.education) {
            i = i + 1;
            gpa += e.gradGPA;
        }
        return gpa / i;
    }

    class Resume {
        Information userInfo;
        ArrayList<Education> education;
        ArrayList<Experience> experience;
        Resume(String name, String surname, String email, String phone, String gender) {
            this.userInfo = new Information(name, surname, email, phone, gender);
            this.education = new ArrayList<>();
            this.experience = new ArrayList<>();
        }
    }
}
