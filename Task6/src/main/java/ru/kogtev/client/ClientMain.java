package ru.kogtev.client;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ClientMain {
    public static final int PORT = 8899;

    private static Socket socket;
    private static PrintWriter out;

    private static ChatWindow chatWindow;

    protected static final Set<String> usernames = new HashSet<>();

    public static void main(String[] args) {
        chatWindow = new ChatWindow();
        connectToServer();
        chatWindow.display();
    }

    private static void connectToServer() {
        try {
            System.out.println(usernames);
            socket = new Socket(chatWindow.getServerAddress(), PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(chatWindow.getUsername());
            appendMessage("Connected to the server");
            new Thread(new ServerListener(socket)).start(); // Запуск потока для приема сообщений от сервера
        } catch (IOException e) {
            appendMessage("Error connecting to the server: " + e.getMessage());
        }
    }

    public static void sendMessage(String message) {
        if (out != null) {
            out.println(message); // Отправляем сообщение на сервер
        } else {
            appendMessage("Error: Not connected to the server");
        }
    }

    public static void appendMessage(String message) {
        // Добавляем новое сообщение в окно чата
        chatWindow.appendMessage(message);
    }

    public static void appendUsername(String message) {
        // Разбиваем строку сообщения на список пользователей
        String[] userList = message.split(",");

        // Очищаем текущий список пользователей
        usernames.clear();

        // Добавляем всех пользователей из сообщения в множество
        for (String username : userList) {
            usernames.add(username);
        }

        chatWindow.updateUserList(usernames);

        // Выводим список пользователей в консоль (это может быть удалено в финальной версии)
        System.out.println(usernames + "appendUsername");
    }
}
