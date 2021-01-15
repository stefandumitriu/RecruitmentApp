import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.CompletionService;

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
        HashMap<Consumer, Integer> distanceMap = new HashMap<>();
        distanceMap.put(this, 0);
        PriorityQueue<Consumer> q = new PriorityQueue<Consumer>((o1, o2) -> {
            if(distanceMap.get(o1) > distanceMap.get(o2)) {
                return 1;
            }
            else if(distanceMap.get(o1).equals(distanceMap.get(o2))) {
                return 0;
            }
            else return -1;
        });
        for(Consumer c : Application.getInstance().consumers) {
            if(c != this) {
                distanceMap.put(c, Integer.MAX_VALUE);
            }
            q.add(c);
        }
        while(!q.isEmpty()) {
            Consumer temp = q.poll();
            for(Consumer f : temp.friends) {
                int alt = distanceMap.get(temp) + 1;
                if(alt < distanceMap.get(f)) {
                    distanceMap.replace(f, alt);
                    q.remove(f);
                    q.add(f);
                }
            }
        }
        return distanceMap.get(consumer);
    }

    public void remove(Consumer consumer) {
        friends.remove(consumer);
    }

    public Integer getGraduationYear() {
        for(Education ed : resume.education) {
            if(ed.level.equals("college")) {
                return ed.gradYear.getYear();
            }
        }
        return null;
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

    static public Consumer searchByName(String name) {
        for(Consumer c : Application.getInstance().consumers) {
            if(c.resume.userInfo.getSurname().equals(name)) {
                return c;
            }
        }
        return null;
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
