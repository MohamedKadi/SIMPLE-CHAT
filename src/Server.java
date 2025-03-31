import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private ServerSocket ss;
    protected static ArrayList<Client> clients = new ArrayList<>();

    public static void main(String[] args) {
        try{
            ServerSocket ss = new ServerSocket(3000);
            System.out.println("waiting for a client to connect...");
            while(true){
                Socket s = ss.accept();
                new Thread(new ClientHandeler(s)).start();
                System.out.println("new client connected to the server");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
class ClientHandeler implements Runnable{
    Socket s;
    public ClientHandeler(Socket s){
        this.s = s;
    }
    @Override
    public void run() {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(),true);
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

            //waiting for the current name of the client
            String name = in.readLine();
            //adding him to the array
            Client C1 = new Client(name,s);
            synchronized (Server.class){
                Server.clients.add(C1);
            }
            //sending the array of clients
            oos.writeObject( Server.clients);
            oos.flush();
            //waiting for the client to chose who wanna chat with we peretend that the name's are unique
            int num;
            Client C;
            do{
                num = Integer.parseInt(in.readLine())-1;
                C =  Server.clients.get(num);
                if (num < 1 || num >  Server.clients.size()) continue;
            }while(C.getName() == name);
            //sending msgs between the clients
            Socket cs1= C.getS();
            BufferedReader cin = new BufferedReader(new InputStreamReader(cs1.getInputStream()));
            PrintWriter cout = new PrintWriter(cs1.getOutputStream());
            String msgs;
            System.out.println("listening to msgs...");
            do{
                if((msgs = in.readLine()) != null){
                    cout.println(C.getName() + " said :"+ msgs);
                }
                if((msgs = cin.readLine()) != null){
                    out.println(C1.getName() + " said :"+ msgs);
                }
            }while(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
