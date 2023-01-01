package Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
    private  JTextField jTextStatus;
    public MonitoringFolder(Socket socket, DefaultTableModel dtmClient, String username, String pathCurrent, JTextField jTextPath,
                            JButton jButtonConnect,JTextField jTextStatus) {
        this.socket = socket;
        this.pathCurrent = pathCurrent;
        this.dtmClient = dtmClient;
        this.username = username;
        this.jTextPath = jTextPath;
        this.jButtonConnect = jButtonConnect;
        this.jTextStatus = jTextStatus;
    }

    @Override
    public void run()  {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                WatchService watcher = FileSystems.getDefault().newWatchService();
                WatchKey key;
                pathCurrent = jTextPath.getText();
                Path dir = Paths.get(pathCurrent);
                dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY);
                try {
                    key = null;
                    key = watcher.take();

                    if (Thread.currentThread().isInterrupted())
                    {
                        break;
                    }
                } catch (InterruptedException ex) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    // Retrieve the type of event by using the kind() method.
                    WatchEvent.Kind<?> kind = event.kind();
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    String descriptionAction = "";
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) { //Tạo mới file hoặc folder
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
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                dtmClient.addRow(vec);
                            }
                        });
                        new ClientSend(socket, "Created", username, pathCurrent, descriptionAction);
                    } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) { //Modify file hoặc folder
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
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                dtmClient.addRow(vec);
                            }
                        });

                        new ClientSend(socket, "Modified", username, pathCurrent, descriptionAction);
                    } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {   //Delete file hoặc folder
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
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                dtmClient.addRow(vec);
                            }
                        });

                        new ClientSend(socket, "Deleted", username, pathCurrent, descriptionAction);
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
