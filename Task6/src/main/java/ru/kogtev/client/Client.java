package ru.kogtev.client;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class Client {
    public static final int PORT = 8899;

    private PrintWriter writer;

    private final ChatWindow chatWindow;

    public Client() {
        chatWindow = new ChatWindow(this);
    }


    public void execute() {
        Socket socket;
        try {
            socket = new Socket("localhost", PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println(chatWindow.getUsername());
            appendMessageToChat("Connected to the server");

            ServerListener serverListener = new ServerListener(socket, this);
            new Thread(serverListener).start();
        } catch (IOException e) {
            appendMessageToChat("Error connecting to the server: " + e.getMessage());
        }
    }

    public void sendMessageToServer(String message) {
        if (writer != null) {
            writer.println(message);
        } else {
            appendMessageToChat("Error: Not connected to the server");
        }
    }

    public void appendMessageToChat(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        chatWindow.appendMessage(timestamp + ": " + message);
    }

    public void updateUserList(Set<String> users) {
        chatWindow.updateUserList(users);
    }


}
