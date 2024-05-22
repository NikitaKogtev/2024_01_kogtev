package ru.kogtev.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class Client {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String SEPARATOR = ": ";

    private final ChatWindow chatWindow;

    private String username;

    private PrintWriter writer;

    public Client() {
        chatWindow = new ChatWindow(this);
    }


    public void execute() {
        String serverAddress = chatWindow.initializeServerAddress();
        int port = chatWindow.initializePort();
        username = chatWindow.initializeUsername();


        try {
            Socket socket = new Socket(serverAddress, port);
            writer = new PrintWriter(socket.getOutputStream(), true);

            chatWindow.initialize(this::sendMessageToServer, username);

            appendMessageToChat("Connected to the server");

            new Thread(new ServerListener(socket, this)).start();

            writer.println(username);
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
        String timestamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        chatWindow.appendMessage(timestamp + SEPARATOR + message);
    }

    public void updateUserList(Set<String> users) {
        chatWindow.updateUserList(users);
    }
}
