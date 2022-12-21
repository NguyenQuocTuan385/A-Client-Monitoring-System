package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;

public class ServerReceive implements Runnable{
    private Socket socket;
    private ArrayList<Socket> listClient;
    private ArrayList<String> listNameClient;
    private TreeMap<String , Socket> mapNameClient;
    private TreeMap<String, String> mapPathClient;

    public ServerReceive(Socket s, ArrayList<Socket> listClient, ArrayList<String> listNameClient,
                         TreeMap<String, Socket> map) {
        this.socket = s;
        this.listClient = listClient;
        this.listNameClient = listNameClient;
        this.mapNameClient = map;
        mapPathClient = new TreeMap<>();
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    String lineTemp[] = line.split("`");
                    String nameClient = lineTemp[0];
                    String message = lineTemp[1];
                    String info = lineTemp[2];
                    String path = lineTemp[3];

                    if (info.equals("1")) {
                        new ServerSend(listClient, message, info, nameClient);
                    }
                    else if (info.equals("2")) {
                        if (!listNameClient.contains(nameClient)) {
                            System.out.println("name:" + nameClient + " path:" + path);
                            listNameClient.add(nameClient);
                            mapNameClient.put(nameClient, socket);
                            mapPathClient.put(nameClient, path);
                            new ServerSend(listClient, message, "2", nameClient);
                        } else {
                            listClient.remove(socket);
                            new ServerSend(socket, "", "4", "server");
                        }
                    }
                }

            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
