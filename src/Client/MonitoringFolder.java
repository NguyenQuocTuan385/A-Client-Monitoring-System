package Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.*;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class MonitoringFolder implements Runnable {
    private Socket socket;
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
            while (true) {
                WatchService watcher = FileSystems.getDefault().newWatchService();
                WatchKey key;
                pathCurrent = jTextPath.getText();
                Path dir = Paths.get(pathCurrent);
                dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY);
                try {
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
                    String descriptionAction = "";
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        if (jButtonConnect.getText().equals("Đóng kết nối") && (socket.isClosed() == false))
                        {
                            Vector<String> vec = new Vector<>();
                            vec.add(username);
                            vec.add("Created");
                            File filePath =  dir.resolve(fileName).toFile();
                            if (filePath.isDirectory()) {
                                descriptionAction = "A new folder was created in path " + dir.resolve(fileName);
                                vec.add(descriptionAction);

                            } else if(filePath.isFile()) {
                                descriptionAction = "A new file was created in path " + dir.resolve(fileName);
                                vec.add(descriptionAction);
                            }
                            dtmClient.addRow(vec);
                            new ClientSend(socket, "Created", username, pathCurrent, descriptionAction);
                        }
                    } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        if (jButtonConnect.getText().equals("Đóng kết nối") && (socket.isClosed() == false))
                        {
                            Vector<String> vec = new Vector<>();
                            vec.add(username);
                            vec.add("Modified");
                            File filePath =  dir.resolve(fileName).toFile();
                            if (filePath.isDirectory()) {
                                descriptionAction = "A folder was modified in path " + dir.resolve(fileName);
                                vec.add(descriptionAction);
                            } else if(filePath.isFile()) {
                                descriptionAction = "A file was modified in path " + dir.resolve(fileName);
                                vec.add(descriptionAction);
                            }
                            dtmClient.addRow(vec);
                            new ClientSend(socket, "Modified", username, pathCurrent, descriptionAction);
                        }
                    } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        if (jButtonConnect.getText().equals("Đóng kết nối")&& (socket.isClosed() == false))
                        {
                            Vector<String> vec = new Vector<>();
                            vec.add(username);
                            vec.add("Deleted");
                            if (fileName.toString().indexOf('.') == -1) {
                                descriptionAction = "A folder was deleted in path " + dir.resolve(fileName);
                                vec.add(descriptionAction);
                            } else {
                                descriptionAction = "A file was deleted in path " + dir.resolve(fileName);
                                vec.add(descriptionAction);
                            }
                            dtmClient.addRow(vec);
                            new ClientSend(socket, "Deleted", username, pathCurrent, descriptionAction);
                        }
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
                if (!jButtonConnect.getText().equals("Đóng kết nối"))
                {
                    break;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
