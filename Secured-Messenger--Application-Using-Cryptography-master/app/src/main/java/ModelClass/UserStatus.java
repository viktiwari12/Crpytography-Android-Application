package ModelClass;

import java.util.ArrayList;

public class UserStatus {
    private String name,profilepic;
    private long lastupdated;
    private ArrayList<Status> statuses;

    public UserStatus() {
    }

    public UserStatus(String name, String profilepic, long lastupdated, ArrayList<Status> statuses) {
        this.name = name;
        this.profilepic = profilepic;
        this.lastupdated = lastupdated;
        this.statuses = statuses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public long getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(long lastupdated) {
        this.lastupdated = lastupdated;
    }

    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }




}
