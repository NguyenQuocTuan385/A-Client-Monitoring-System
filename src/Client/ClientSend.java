package Client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientSend {
    ClientSend(Socket socket, Object message, String info, String name, String path) throws IOException {
        String messages = name + "`" + message + "`" + info + "`" + path;
        BufferedWriter bufferedWriter;
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write(messages);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }
}
