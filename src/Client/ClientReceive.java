package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReceive implements Runnable{
    private Socket socket;
    private BufferedReader bufferedReader;
    public ClientReceive(Socket socket)
    {
        this.socket = socket;
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
                    System.out.println(nameClient + " connected to server");
                }
                else if (info.equals("4")) {
                    System.out.println("Đã có người sử dụng tên này!");
                    socket.close();
                    bufferedReader.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
