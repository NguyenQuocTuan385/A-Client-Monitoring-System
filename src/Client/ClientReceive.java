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
                String lineTemp[] = line.split("`");
                String nameClient = lineTemp[0];
                String message = lineTemp[1];
                String info = lineTemp[2];

                if (info.equals("2")) {
                    JOptionPane.showMessageDialog(null, "Kết nối thành công","Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    jButtonConnect.setText("Đóng kết nối");
                }
                else if(info.equals("3")) {
                    JOptionPane.showMessageDialog(null, "Đóng kết nối thành công","Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    socket.close();
                    bufferedReader.close();
                    jButtonConnect.setText("Kết nối");
                    break;
                }
                else if (info.equals("4")) {
                    socket.close();
                    bufferedReader.close();
                    JOptionPane.showMessageDialog(null, "Đã có người sử dụng tên này!!!","Thông báo", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                else if (info.equals("5")) {
                    path = lineTemp[3];
                    jTextPath.setText(path);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
