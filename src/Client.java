import java.io.Serializable;
import java.net.Socket;

public class Client implements Serializable {
    private String name;
    private transient Socket s;

    public Client(String name, Socket s){
        this.name = name;
        this.s = s;
    }

    public String getName() {
        return name;
    }

    public Socket getS() {
        return s;
    }


}
