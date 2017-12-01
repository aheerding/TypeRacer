import java.io.Serializable;

public class Message implements Serializable{
    private String sender;
    private String message;
    private static final long serialVersionUID = -1348943183576093148L;

    public Message(String _sender, String _message){
        sender = _sender;
        message = _message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    //toString method to test
    public String toString(){
        return (sender + ": " + message);
    }
}
