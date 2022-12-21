package Client;

import java.util.Scanner;

public class Test {
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        String username = sc.nextLine();
        new ClientHandler("127.0.0.1", 1234, username);
    }
}
