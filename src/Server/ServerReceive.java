package Server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
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
    private DefaultTableModel dtmListActionClient;
    private boolean isStart;

    public ServerReceive(Socket s, ArrayList<Socket> listClient, ArrayList<String> listNameClient,
                         TreeMap<String, Socket> map, DefaultTableModel dtmListClient, TreeMap<String, String> mapPathClient,
                        DefaultTableModel dtmListActionClient, boolean isStart) {
        this.mapPathClient = mapPathClient;
        this.dtmListClient = dtmListClient;
        this.socket = s;
        this.listClient = listClient;
        this.listNameClient = listNameClient;
        this.mapNameClient = map;
        this.isStart = isStart;
        this.dtmListActionClient = dtmListActionClient;
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

                     if (infoMessage.equals("Connect")) { //Nếu client gửi gói tin connect tới server
                        if (!listNameClient.contains(nameClient)) {
                            listNameClient.add(nameClient);
                            mapNameClient.put(nameClient, socket);
                            mapPathClient.put(nameClient, path);
                            Vector<String>vec = new Vector<>();
                            vec.add(nameClient);
                            vec.add("Start");
                            vec.add(path);
                            SwingUtilities.invokeAndWait(new Runnable() {
                                @Override
                                public void run() {
                                    dtmListClient.addRow(vec);
                                }
                            });
                            new ServerSend(socket, "Connect success", "server");
                        } else {
                            listClient.remove(socket);
                            new ServerSend(socket, "Connect fail", "server");
                        }
                    }
                     else if (infoMessage.equals("Disconnect")) { //Nếu client gửi gói tin disconnect tới server
                         if (listNameClient.contains(nameClient)) {
                             SwingUtilities.invokeAndWait(new Runnable() {
                                 @Override
                                 public void run() {
                                     dtmListClient.removeRow(listNameClient.indexOf(nameClient));
                                 }
                             });
                             listNameClient.remove(nameClient);
                             mapNameClient.remove(nameClient);
                             mapPathClient.remove(nameClient);
                             listClient.remove(socket);
                             if (!path.equals("Server stop all")) {
                                 new ServerSend(socket, "Disconnect success", "server");
                             }
                             System.out.println("Username:" + nameClient + " was disconnected");
                         }
                     }
                     else if (infoMessage.equals("Created")) { //Nếu client gửi gói tin dữ liệu giám sát
                         String descriptionAction = lineTemp[3];
                         Vector<String>vec = new Vector<>();
                         vec.add(nameClient);
                         vec.add("Created");
                         vec.add(descriptionAction);
                         SwingUtilities.invokeAndWait(new Runnable() {
                             @Override
                             public void run() {
                                 dtmListActionClient.addRow(vec);
                             }
                         });
                     }
                     else if (infoMessage.equals("Deleted")) { //Nếu client gửi gói tin dữ liệu giám sát
                         String descriptionAction = lineTemp[3];
                         Vector<String>vec = new Vector<>();
                         vec.add(nameClient);
                         vec.add("Deleted");
                         vec.add(descriptionAction);
                         SwingUtilities.invokeAndWait(new Runnable() {
                             @Override
                             public void run() {
                                 dtmListActionClient.addRow(vec);
                             }
                         });
                     }
                     else if (infoMessage.equals("Modified")) { //Nếu client gửi gói tin dữ liệu giám sát
                         String descriptionAction = lineTemp[3];
                         Vector<String>vec = new Vector<>();
                         vec.add(nameClient);
                         vec.add("Modified");
                         vec.add(descriptionAction);
                         SwingUtilities.invokeAndWait(new Runnable() {
                             @Override
                             public void run() {
                                 dtmListActionClient.addRow(vec);
                             }
                         });
                     }

                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
