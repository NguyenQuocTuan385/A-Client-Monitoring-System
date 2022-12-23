package Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.*;
import java.util.Vector;

public class MonitoringFolder implements Runnable {
    private Socket socket;
    private Path dir;
    private DefaultTableModel dtmClient;
    private String username;
    private String pathCurrent;
    private JTextField jTextPath;
    private  JButton jButtonConnect;
    public MonitoringFolder(Socket socket, DefaultTableModel dtmClient, String username, String pathCurrent, JTextField jTextPath,
                            JButton jButtonConnect) {
        this.socket = socket;
        this.pathCurrent = pathCurrent;
        this.dtmClient = dtmClient;
        this.username = username;
        this.jTextPath = jTextPath;
        this.jButtonConnect = jButtonConnect;
    }

    @Override
    public void run()  {
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            WatchKey key;
            while (true) {
                try {
                    pathCurrent = jTextPath.getText();
                    dir = Paths.get(pathCurrent);
                    dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
                            StandardWatchEventKinds.ENTRY_MODIFY);
                    key = null;
                    key = watcher.take();
                } catch (InterruptedException ex) {
                    System.out.println("InterruptedException: " + ex.getMessage());
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    // Retrieve the type of event by using the kind() method.
                    WatchEvent.Kind<?> kind = event.kind();
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    String filenamePath = "";
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        if (jButtonConnect.getText().equals("Đóng kết nối") && (socket.isClosed() == false))
                        {
                            Vector<String> vec = new Vector<>();
                            vec.add(username);
                            vec.add("Created");
                            File filePath =  dir.resolve(fileName).toFile();
                            if (filePath.isDirectory()) {
                                filenamePath = "A new folder was created in path " + dir.resolve(fileName);
                                vec.add(filenamePath);
                            } else if(filePath.isFile()) {
                                filenamePath = "A new file was created in path " + dir.resolve(fileName);
                                vec.add(filenamePath);
                            }
                            dtmClient.addRow(vec);
                            new ClientSend(socket, "Created", username, pathCurrent, filenamePath);
                        }
                    } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        if (jButtonConnect.getText().equals("Đóng kết nối") && (socket.isClosed() == false))
                        {
                            Vector<String> vec = new Vector<>();
                            vec.add(username);
                            vec.add("Modified");
                            File filePath =  dir.resolve(fileName).toFile();
                            if (filePath.exists()) {
                                if (filePath.isDirectory()) {
                                    filenamePath = "A folder was modified in path " + dir.resolve(fileName);
                                    vec.add(filenamePath);
                                    dtmClient.addRow(vec);
                                    new ClientSend(socket, "Modified", username, pathCurrent, filenamePath);
                                } else if(filePath.isFile()) {
                                    filenamePath = "A file was modified in path " + dir.resolve(fileName);
                                    vec.add(filenamePath);
                                    dtmClient.addRow(vec);
                                    new ClientSend(socket, "Modified", username, pathCurrent, filenamePath);
                                }
                            }
                        }
                    } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        if (jButtonConnect.getText().equals("Đóng kết nối")&& (socket.isClosed() == false))
                        {
                            Vector<String> vec = new Vector<>();
                            vec.add(username);
                            vec.add("Deleted");
                            File filePath =  dir.resolve(fileName).toFile();
                            if (fileName.toString().indexOf('.') == -1) {
                                filenamePath = "A folder was deleted in path " + dir.resolve(fileName);
                                vec.add(filenamePath);
                            } else {
                                filenamePath = "A file was deleted in path " + dir.resolve(fileName);
                                vec.add(filenamePath);
                            }
                            dtmClient.addRow(vec);
                            new ClientSend(socket, "Deleted", username, pathCurrent, filenamePath);
                        }
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
                if (!jButtonConnect.getText().equals("Đóng kết nối"))
                {
//                    if (!socket.isClosed()) {
//                        socket.close();
//                    }
                    break;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
