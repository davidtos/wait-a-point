package something.wait_a_point;

/**
 * Created by David on 23-10-2015.
 */
public class SendTo {
    String type;
    String to;
    String from;
    String message;

    public SendTo(String type, String to, String from, String message) {
        this.type = type;
        this.to = to;
        this.from = from;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
