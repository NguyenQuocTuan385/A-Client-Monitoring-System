package Client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientSend {
    ClientSend(Socket socket, String infoMessage, String name, String path) throws IOException {
        String messages = name + "`" + infoMessage + "`" + path;
        BufferedWriter bufferedWriter;
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write(messages);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }
    ClientSend(Socket socket, String infoMessage, String name, String path, String fileName) throws IOException {
        String messages = name + "`" + infoMessage + "`" + path + "`" + fileName;
        BufferedWriter bufferedWriter;
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write(messages);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }
}
