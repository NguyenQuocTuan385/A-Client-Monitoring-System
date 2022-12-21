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
    public ClientReceive(Socket socket, JButton jButtonConnect)
    {
        this.socket = socket;
        this.jButtonConnect = jButtonConnect;
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

                if (info.equals("1")) {

                }
                else if (info.equals("2")) {
                    JOptionPane.showMessageDialog(null, "Kết nối thành công","Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    jButtonConnect.setText("Đóng kết nối");
                }
                else if (info.equals("4")) {
                    System.out.println("Đã có người sử dụng tên này!");
                    socket.close();
                    bufferedReader.close();
                    JOptionPane.showMessageDialog(null, "Đã có người sử dụng tên này!!!","Thông báo", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
