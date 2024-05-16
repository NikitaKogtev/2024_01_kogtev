package ru.kogtev.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.kogtev.common.Message;
import ru.kogtev.common.UserList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    static Set<String> usernames = new HashSet<>();
    static List<ClientHandler> clients = new ArrayList<>();

    private final ObjectMapper objectMapper;

    private final int port;

    public Server() {
        this.port = ConfigManager.getProperty();
        this.objectMapper = new ObjectMapper();
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server is running on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Принимаем входящее подключение
                System.out.println("New client connected");
                ClientHandler clientHandler = new ClientHandler(clientSocket, this); // Создаем обработчик клиента
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.out.println("The client has not been accept" + e.getMessage());
        }

    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public void addUsername(String username) {
        usernames.add(username);
    }

    public void removeUsername(String username) {
        usernames.remove(username);
        broadcastUserList();
    }

    public Set<String> getUsernames() {
        return usernames;
    }

    public void broadcastMessage(Message message) {
        String jsonMessage;
        try {
            jsonMessage = objectMapper.writeValueAsString(message);
            for (ClientHandler client : clients) {
                client.sendMessage(jsonMessage);
            }
        } catch (IOException e) {
            System.out.println("The message was not sent" + e.getMessage());
        }
    }

    public void broadcastUserList() {
        UserList userList = new UserList(usernames);
        String jsonUserList;
        try {
            jsonUserList = objectMapper.writeValueAsString(userList);
            for (ClientHandler client : clients) {
                client.sendUser(jsonUserList);
            }
        } catch (IOException e) {
            System.out.println("The usernames was not sent" + e.getMessage());
        }
    }


}