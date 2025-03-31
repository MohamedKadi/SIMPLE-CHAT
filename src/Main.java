import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Thread {
    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost",3000);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
        PrintWriter out = new PrintWriter(s.getOutputStream(),true);

        Scanner scan = new Scanner(System.in);
        System.out.println("Name : ");
        String name = scan.nextLine();
        out.println(name);
        ArrayList<Client> clients;
        if((clients=(ArrayList<Client>)ois.readObject())!=null){
            int count=0;

            for(Client c : clients){
                count++;
                System.out.println(count + "- "+c.getName());
            }
        }

        System.out.println("chose 1 client with a number:");
        int num = scan.nextInt();
        out.println(num);

        listen listen = new listen(in);
        write write = new write(scan,out);

        listen.start();
        write.start();

    }
}

class listen extends Thread {
    String msg;
    BufferedReader in;
    public listen(BufferedReader in){
        this.in = in;
    }
    public void run() {
        while(true){
            try {
                if((msg=in.readLine())!=null){
                    System.out.println(msg);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class write extends Thread {
    Scanner scan;
    PrintWriter out;
    public write(Scanner scan,PrintWriter out){
        this.scan=scan;
        this.out = out;
    }
    public void run() {
        while(true){
            try {
                String msg = scan.nextLine();
                out.println(msg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}