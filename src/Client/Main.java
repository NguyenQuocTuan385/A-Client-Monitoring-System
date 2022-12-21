package Client;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        new ClientHandler();
        // create a file object
        String folderPath = "C:\\Drivers";

        File folder = new File(folderPath);

        File[] files = folder.listFiles();

        //iterate the files array
        for(File file:files) {
            //check if the file
            if(file.isFile()) {
                System.out.println("File - "+file.getName());
            }else
            if(file.isDirectory()) {
                System.out.println("Folder - "+file.getName());
            }
        }
    }
}
