import java.util.ArrayList;

public class Resume {
    Information userInfo;
    ArrayList<Education> education;
    ArrayList<Experience> experience;
    private Resume(ResumeBuilder builder) {
        this.userInfo = builder.userInfo;
        this.education = builder.education;
        this.experience = builder.experience;
    }

    public static class ResumeBuilder {
        Information userInfo;
        ArrayList<Education> education;
        ArrayList<Experience> experience;
        public ResumeBuilder(Information userInfo, ArrayList<Education> education) throws ResumeIncompleteException {
            if(userInfo == null || education.size() == 0)
                throw new ResumeIncompleteException();
            this.userInfo = userInfo;
            this.education = new ArrayList<>(education);
        }
        public ResumeBuilder experience(ArrayList<Experience> experience) {
            this.experience = new ArrayList<>(experience);
            return this;
        }
        public Resume build() {
            return new Resume(this);
        }
    }
}

class ResumeIncompleteException extends Exception {
    ResumeIncompleteException() {
        System.out.println("Resume data is incomplete");
    }
}