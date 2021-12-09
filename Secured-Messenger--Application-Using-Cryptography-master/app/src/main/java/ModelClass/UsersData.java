package ModelClass;

public class UsersData {

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    private String Email;
    private String Name;
    private String Phone;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private String userid;

    public String getSenderMessage() {
        return SenderMessage;
    }

    public void setSenderMessage(String senderMessage) {
        SenderMessage = senderMessage;
    }

    public String getReceiverMessage() {
        return ReceiverMessage;
    }

    public void setReceiverMessage(String receiverMessage) {
        ReceiverMessage = receiverMessage;
    }

    private String SenderMessage;
    private String ReceiverMessage;

    public void getUserid(String key) {
    }
}
