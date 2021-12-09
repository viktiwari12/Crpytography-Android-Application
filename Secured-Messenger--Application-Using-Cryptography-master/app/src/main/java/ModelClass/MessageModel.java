package ModelClass;

public class MessageModel {

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }





    private String userid;

    public MessageModel(String userid, String message, String time) {
        this.userid = userid;
        this.message = message;
        this.time=time;

    }

    private String message;
    private String imageurl;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    private Long timestamp;

    public MessageModel(){}

}
