package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;

public class ServerReceive implements Runnable{
    private Socket socket;
    private ArrayList<Socket> listClient;
    private ArrayList<String> listNameClient;
    private TreeMap<String , Socket> mapClient;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ServerReceive(Socket s, ArrayList<Socket> listClient, ArrayList<String> nameClient,
                         TreeMap<String, Socket> map) {
        this.socket = s;
        this.listClient = listClient;
        this.listNameClient = nameClient;
        this.mapClient = map;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String s = bufferedReader.readLine();
                System.out.println(s);
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bufferedWriter.write("Connected");
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
