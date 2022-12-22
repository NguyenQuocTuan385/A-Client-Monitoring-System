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
    private boolean isStart;

    public ServerReceive(Socket s, ArrayList<Socket> listClient, ArrayList<String> listNameClient,
                         TreeMap<String, Socket> map, DefaultTableModel dtmListClient, TreeMap<String, String> mapPathClient,
                         boolean isStart) {
        this.mapPathClient = mapPathClient;
        this.dtmListClient = dtmListClient;
        this.socket = s;
        this.listClient = listClient;
        this.listNameClient = listNameClient;
        this.mapNameClient = map;
        this.isStart = isStart;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (isStart) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    String lineTemp[] = line.split("`");
                    String nameClient = lineTemp[0];
                    String infoMessage = lineTemp[1];
                    String path = lineTemp[2];

                     if (infoMessage.equals("Connect")) {
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
                            new ServerSend(socket, "Connect success", "server");
                        } else {
                            listClient.remove(socket);
                            new ServerSend(socket, "Connect fail", "server");
                        }
                    }
                     else if (infoMessage.equals("Disconnect")) {
                         if (listNameClient.contains(nameClient)) {
                             dtmListClient.removeRow(listNameClient.indexOf(nameClient));
                             listNameClient.remove(nameClient);
                             mapNameClient.remove(nameClient);
                             mapPathClient.remove(nameClient);
                             listClient.remove(socket);
                             new ServerSend(socket, "Disconnect success", "server");
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
