package ru.kogtev.client;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client {
    private static final Logger logger = LogManager.getLogger(Client.class);

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String SEPARATOR = ": ";

    private final ChatWindow chatWindow;

    private PrintWriter writer;

    public Client() {
        chatWindow = new ChatWindow();
    }


    public void execute() {
        String serverAddress = chatWindow.initializeServerAddress();
        int port = chatWindow.initializePort();
        String username = chatWindow.initializeUsername();

        try {
            Socket socket = new Socket(serverAddress, port);
            writer = new PrintWriter(socket.getOutputStream(), true);

            chatWindow.initializeChat(this::sendMessageToServer, username);

            new Thread(new ServerListener(socket, this)).start();
            appendMessageToChat("Connected to the server");
            logger.info("Подключение к серверу");

            writer.println(username);
        } catch (IOException e) {
            appendMessageToChat("Error connecting to the server: " + e.getMessage());
            logger.error("Ошибка подключения к серверу {}", e.getMessage());
        }
    }

    private void sendMessageToServer(String message) {
        if (writer != null) {
            writer.println(message);
        } else {
            appendMessageToChat("Error: Not connected to the server");
            logger.error("Ошибка подключения к серверу");
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
