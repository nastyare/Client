package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 1517;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your nickname: ");
        String nickname = scanner.nextLine().trim();

        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(nickname);

            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading messages from the server: " + e.getMessage());
                }
            }).start();

            guide();

            while (true) {
                String message = scanner.nextLine().trim();

                if (message.equalsIgnoreCase("/w")) {
                    handlePrivateMessage(scanner, out);
                } else {
                    out.println(message);
                }
            }
        } catch (IOException e) {
            System.err.println("Error connecting to the server: " + e.getMessage());
        }
    }

    private static void guide() {
        System.out.println("commands:");
        System.out.println("/w for dm, /users for list of users.");
    }

    private static void handlePrivateMessage(Scanner scanner, PrintWriter out) {
        out.println("/users");
        System.out.println("Enter the recipient's nickname:");
        String recipient = scanner.nextLine().trim();
        System.out.println("Enter your message:");
        String privateMessage = scanner.nextLine().trim();
        out.println("/w " + recipient + " " + privateMessage);
    }
}
