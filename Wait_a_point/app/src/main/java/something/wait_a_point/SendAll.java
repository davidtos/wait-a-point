package something.wait_a_point;

/**
 * Created by Fatih on 2-11-2015.
 */
public class SendAll {
    String type;
    String from;
    String message;

    public SendAll(String type, String from, String message) {
        this.type = type;
        this.from = from;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) { this.type = type; }

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
