package Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class ClientReceive implements Runnable{
    private Socket socket;
    private BufferedReader bufferedReader;
    private JButton jButtonConnect;
   private String clientUsername;
    private String path;
    private  JTextField jTextPath;
    private DefaultTableModel dtmClient;
    private  JTextField jTextStatus;
    private Thread threadMonitor;
    public ClientReceive(Socket socket, JButton jButtonConnect, String path, String clientUsername, JTextField jTextPath
                        ,DefaultTableModel dtmClient, JTextField jTextStatus)
    {
        this.path = path;
        this.socket = socket;
        this.jButtonConnect = jButtonConnect;
        this.clientUsername = clientUsername;
        this.jTextPath = jTextPath;
        this.dtmClient = dtmClient;
        this.jTextStatus = jTextStatus;
    }

    @Override
    public void run() {
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    String lineTemp[] = line.split("`");
                    String nameClient = lineTemp[0];
                    String infoMessage = lineTemp[1];

                    if (infoMessage.equals("Connect success")) { //Nếu server gửi gói tin kết nối thành công
                        JOptionPane.showMessageDialog(null, "Kết nối thành công","Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        jButtonConnect.setText("Đóng kết nối");
                        System.out.println("Kết nối server thành công");
                        threadMonitor =  new Thread(new MonitoringFolder(socket,dtmClient, clientUsername, path, jTextPath, jButtonConnect, jTextStatus));
                        threadMonitor.start();
                    }
                    else if(infoMessage.equals("Disconnect success")) {  //Nếu server gửi gói tin đóng kết nối thành công
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                jButtonConnect.setText("Kết nối");
                                path = "";
                                jTextPath.setText("");
                                jTextStatus.setText("");
                                dtmClient.setRowCount(0);
                            }
                        });
                        if (nameClient.equals("Server stop")) {
                            JOptionPane.showMessageDialog(null, "Kết nối không thành công!!!","Thông báo", JOptionPane.ERROR_MESSAGE);
                        }
                        else if (nameClient.equals("Server stop all"))  //Nếu server đóng kết nối với tất cả client
                        {
                            JOptionPane.showMessageDialog(null, "Server đã đóng kết nối!!!","Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            new ClientSend(socket, "Disconnect",clientUsername, "Server stop all");
                            System.out.println("Đóng kết nối thành công");
                        }
                        //Nếu server đóng kết nối với tất cả client khi click button exit
                        else if (nameClient.equals("Server stop all exit window") || nameClient.equals("Server disconnect")) {
                            JOptionPane.showMessageDialog(null, "Server đã đóng kết nối!!!","Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            System.out.println("Đóng kết nối thành công");
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Đóng kết nối thành công","Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            System.out.println("Đóng kết nối thành công");
                        }
                        if (threadMonitor.isAlive()) {
                            threadMonitor.interrupt();
                        }
                        socket.close();
                        bufferedReader.close();
                        break;
                    }
                    else if (infoMessage.equals("Connect fail")) { //Nếu server gửi gói tin kết nối thất bại
                        socket.close();
                        bufferedReader.close();
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                jTextStatus.setText("");
                                jTextPath.setText("");
                            }
                        });
                        JOptionPane.showMessageDialog(null, "Đã có người sử dụng tên này!!!","Thông báo", JOptionPane.ERROR_MESSAGE);

                        break;
                    }
                    else if (infoMessage.equals("Change Folder Monitoring")) { //Nếu server gửi gói tin thay đổi thư mục giám sát
                        path = lineTemp[2];
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                jTextPath.setText(path);
                            }
                        });
                        if (threadMonitor.isAlive()) {
                            threadMonitor.interrupt();
                        }
                        threadMonitor = new Thread(new MonitoringFolder(socket,dtmClient, clientUsername, path, jTextPath, jButtonConnect, jTextStatus));
                        threadMonitor.start();
                    }
                    else if (infoMessage.equals("Stop monitor")) {  //Nếu server gửi gói tin dừng giám sát thư mục
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                jTextStatus.setText("Is not being monitored");
                            }
                        });

                    }
                    else if (infoMessage.equals("Start monitor")) { //Nếu server gửi gói tin mở giám sát thư mục
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                jTextStatus.setText("Being monitored");
                            }
                        });
                        if (threadMonitor.isAlive()) {
                            threadMonitor.interrupt();
                        }
                        threadMonitor = new Thread(new MonitoringFolder(socket,dtmClient, clientUsername, path, jTextPath, jButtonConnect, jTextStatus));
                        threadMonitor.start();
                    }
                }
            }
        } catch (IOException | InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
