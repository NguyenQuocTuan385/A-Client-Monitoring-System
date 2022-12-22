package Server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Vector;

public class ServerReceive implements Runnable{
    private Socket socket;
    private ArrayList<Socket> listClient;
    private ArrayList<String> listNameClient;
    private TreeMap<String , Socket> mapNameClient;
    private TreeMap<String, String> mapPathClient;
    private DefaultTableModel dtmListClient;

    public ServerReceive(Socket s, ArrayList<Socket> listClient, ArrayList<String> listNameClient,
                         TreeMap<String, Socket> map, DefaultTableModel dtmListClient, TreeMap<String, String> mapPathClient) {
        this.mapPathClient = mapPathClient;
        this.dtmListClient = dtmListClient;
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

                     if (info.equals("2")) {
                        if (!listNameClient.contains(nameClient)) {
                            listNameClient.add(nameClient);
                            mapNameClient.put(nameClient, socket);
                            mapPathClient.put(nameClient, path);
                            int index = listNameClient.indexOf(nameClient);
                            Vector<String>vec = new Vector<>();
                            vec.add(String.valueOf(index));
                            vec.add(nameClient);
                            vec.add(path);
                            dtmListClient.addRow(vec);
                            new ServerSend(socket, message, "2", nameClient);
                        } else {
                            listClient.remove(socket);
                            new ServerSend(socket, "", "4", "server");
                        }
                    }
                     else if (info.equals("3")) {
                         if (listNameClient.contains(nameClient)) {
                             dtmListClient.removeRow(listNameClient.indexOf(nameClient));
                             listNameClient.remove(nameClient);
                             mapNameClient.remove(nameClient);
                             mapPathClient.remove(nameClient);
                             listClient.remove(socket);
                             new ServerSend(socket, "Disconnect", "3", "server");
                             System.out.println(nameClient + "is disconnected");
                         }
                     }
                }

            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
