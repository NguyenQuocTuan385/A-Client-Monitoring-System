package Client;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.*;
import java.util.Vector;

public class MonitoringFolder implements Runnable {
    private WatchService watchService;
    private Socket socket;
    private Path dir;
    private DefaultTableModel dtmClient;
    private String username;
    private String pathCurrent;
    public MonitoringFolder(Socket socket, DefaultTableModel dtmClient, String username, String pathCurrent) {
        this.socket = socket;
        dir = Paths.get(pathCurrent);
        this.pathCurrent = pathCurrent;
        this.dtmClient = dtmClient;
        this.username = username;
    }

    @Override
    public void run()  {
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            WatchKey key = null;
            while (true) {
                try {
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
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        Vector<String> vec = new Vector<>();
                        vec.add(username);
                        vec.add("Created");
                        vec.add("A new file " + fileName.getFileName() + " was created");
                        dtmClient.addRow(vec);
                        new ClientSend(socket, "Created", username, pathCurrent, fileName.getFileName().toString());
                    } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        Vector<String> vec = new Vector<>();
                        vec.add(username);
                        vec.add("Modified");
                        vec.add("A new file " + fileName.getFileName() + " was modified");
                        dtmClient.addRow(vec);
                        new ClientSend(socket, "Modified", username, pathCurrent, fileName.getFileName().toString());
                    } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        Vector<String> vec = new Vector<>();
                        vec.add(username);
                        vec.add("Deleted");
                        vec.add("A new file " + fileName.getFileName() + " was deleted");
                        dtmClient.addRow(vec);
                        new ClientSend(socket, "Deleted", username, pathCurrent, fileName.getFileName().toString());
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
