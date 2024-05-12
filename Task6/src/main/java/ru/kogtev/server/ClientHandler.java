package ru.kogtev.server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientHandler implements Runnable {
    public static Set<String> usernames = new HashSet<>(); // Множество для хранения имен пользователей
    public static List<PrintWriter> clients = new ArrayList<>(); // Список для хранения идентификаторов (Writer'ов) подключенных клиентов

    private final Socket clientSocket;
    private String username;
    PrintWriter printWriter;
    BufferedReader bufferedReader;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        broadcastUserList();
    }

    @Override
    public void run() {
        try (OutputStreamWriter outputStreamWriter =
                     new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8);
             InputStreamReader inputStreamReader =
                     new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8)) {

            printWriter = new PrintWriter(outputStreamWriter, true);
            bufferedReader = new BufferedReader(inputStreamReader);

            // Просим клиента ввести имя
            //   printWriter.println("Enter your username: ");
            username = bufferedReader.readLine();

            // Проверяем доступность имени пользователя

            while (isUsernameAvailable(username)) {
                printWriter.println("This username is already taken. Please enter a different username: ");
                username = bufferedReader.readLine();
            }

            // Добавляем имя клиента в множество имен
            addUser(username);

            // Приветствуем клиента в чате
            printWriter.println("Welcome to the chat, " + username + "!");

            // Добавляем поток вывода клиента в список активных клиентов
            clients.add(printWriter);

            // Рассылаем всем клиентам сообщение о присоединении нового участника
            broadcast(username + " has joined the chat");

            broadcastUserList();

            String inputLine;
            // Читаем сообщения от клиента и рассылаем их всем остальным клиентам
            while ((inputLine = bufferedReader.readLine()) != null) {
                broadcast(username + ": " + inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // При отключении клиента удаляем его имя из множества имен, закрываем потоки и уведомляем остальных клиентов
            if (username != null) {
                removeUser(username);
                broadcast(username + " has left the chat");
            }
            if (printWriter != null) {
                clients.remove(printWriter);
            }
            try {
                clientSocket.close(); // Закрываем сокет клиента
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Метод для рассылки сообщения всем подключенным клиентам
    private void broadcast(String message) {
        synchronized (clients) {
            for (PrintWriter client : clients) {
                client.println(message);
            }
        }
    }

    public static synchronized boolean isUsernameAvailable(String username) {
        return usernames.contains(username);
    }

    // Метод для добавления пользователя в список и рассылки обновленного списка всем клиентам
    public synchronized void addUser(String username) {
        usernames.add(username);
        broadcastUserList();
    }

    // Метод для удаления пользователя из списка и рассылки обновленного списка всем клиентам
    public synchronized void removeUser(String username) {
        usernames.remove(username);
        broadcastUserList();
    }

    // Метод для рассылки обновленного списка пользователей всем клиентам
    private void broadcastUserList() {
        for (PrintWriter client : clients) {
            client.println("USERS: " + String.join(",", usernames));
        }
        System.out.println(usernames + "CH");
    }

}

