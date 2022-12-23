package Client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReceive implements Runnable{
    private Socket socket;
    private BufferedReader bufferedReader;
    private JButton jButtonConnect;
   private String clientUsername;
    private String path;
    private  JTextField jTextPath;
    public ClientReceive(Socket socket, JButton jButtonConnect, String path, String clientUsername, JTextField jTextPath)
    {
        this.path = path;
        this.socket = socket;
        this.jButtonConnect = jButtonConnect;
        this.clientUsername = clientUsername;
        this.jTextPath = jTextPath;
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

                    if (infoMessage.equals("Connect success")) {
                        JOptionPane.showMessageDialog(null, "Kết nối thành công","Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        jButtonConnect.setText("Đóng kết nối");
                        System.out.println("Kết nối server thành công");
                    }
                    else if(infoMessage.equals("Disconnect success")) {
                        if (nameClient.equals("Server stop")) {
                            JOptionPane.showMessageDialog(null, "Kết nối không thành công!!!","Thông báo", JOptionPane.ERROR_MESSAGE);
                        }
                        else if (nameClient.equals("Server stop all"))
                        {
                            JOptionPane.showMessageDialog(null, "Server đã đóng kết nối!!!","Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            new ClientSend(socket, "Disconnect",clientUsername, "Server stop all");
                            System.out.println("Đóng kết nối thành công");
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Đóng kết nối thành công","Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            System.out.println("Đóng kết nối thành công");
                        }
                        socket.close();
                        bufferedReader.close();
                        jButtonConnect.setText("Kết nối");
                        path = "";
                        jTextPath.setText("");
                        break;
                    }
                    else if (infoMessage.equals("Connect fail")) {
                        socket.close();
                        bufferedReader.close();
                        JOptionPane.showMessageDialog(null, "Đã có người sử dụng tên này!!!","Thông báo", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    else if (infoMessage.equals("Change Folder Monitoring")) {
                        path = lineTemp[2];
                        jTextPath.setText(path);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
