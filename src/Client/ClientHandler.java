package Client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler  {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(String ip, int port, String clientUsername) {
        try {
            this.socket = new Socket(ip, port);
            this.clientUsername = clientUsername;

            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String message = clientUsername;
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            new Thread(new ClientReceive(socket)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
