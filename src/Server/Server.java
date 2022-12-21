package Server;

import Client.ClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;

public class Server implements Runnable {
    private int port;
    private ServerSocket serverSocket;
    private ArrayList<Socket> listClient;
    private ArrayList<String> listNameClient;
    private TreeMap<String, Socket> mapClient;

    public Server(int port) throws IOException {
        this.port = port;
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(1234);
        new Thread(server).start();
    }

    @Override
    public void run() {
        listClient =  new ArrayList<>();
        listNameClient = new ArrayList<>();
        mapClient = new TreeMap<>();
        Socket s = new Socket();
        System.out.println("Bắt đầu khởi động máy chủ");
        try {
            serverSocket = new ServerSocket(port);
            System.out.println(serverSocket.getLocalSocketAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                s = serverSocket.accept();
                listClient.add(s);
                new Thread(new ServerReceive(s, listClient, listNameClient, mapClient)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
