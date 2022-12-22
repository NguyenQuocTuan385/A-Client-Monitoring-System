package Server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerSend {
    ServerSend(ArrayList<Socket> listClient, String infoMessage, String name) throws IOException {
        String messages = name + "`" + infoMessage;
        BufferedWriter bufferedWriter;

        for (Socket socket : listClient) { // gửi tin nhắn cho từng client cần thiết
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(messages);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
    }

    ServerSend(Socket socket, String infoMessage, String name) throws IOException {
        String messages = name +  "`" + infoMessage;
        BufferedWriter bufferedWriter;
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write(messages);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }
    ServerSend(Socket socket, String infoMessage, String name, String path) throws IOException {
        String messages = name + "`" +  infoMessage + "`" + path;
        BufferedWriter bufferedWriter;
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write(messages);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }
}
