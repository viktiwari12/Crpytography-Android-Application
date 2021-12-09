package ModelClass;

public class Status {

    private String imageurl;
    private long timestamp;

    public Status() {
    }

    public Status(String imageurl, long timestamp, String time) {
        this.imageurl = imageurl;
        this.timestamp = timestamp;
        this.time= time;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;
}
