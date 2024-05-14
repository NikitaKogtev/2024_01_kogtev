package ru.kogtev.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.kogtev.common.Message;
import ru.kogtev.common.UserList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    public static Set<String> usernames = new HashSet<>(); // Множество для хранения имен пользователей
    public static List<ClientHandler> clients = new ArrayList<>(); // Список для хранения идентификаторов (Writer'ов) подключенных клиентов

    private ObjectMapper objectMapper;

    private int port;

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
            System.out.println("Client ne polychen");
        }

    }


    //Метод удаления клиента
    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    //Метод добавления имя пользователя
    public void addUsername(String username) {
        usernames.add(username);
    }

    //Метод удаления имя пользователя
    public void removeUsername(String username) {
        usernames.remove(username);
        broadcastUserList();
    }

    //Метод получения множества имен пользователей
    public Set<String> getUsernames() {
        return usernames;
    }

    // Метод для рассылки сообщения всем подключенным клиентам
    public void broadcastMessage(Message message) {
        String jsonMessage;
        try {
            jsonMessage = objectMapper.writeValueAsString(message);
            for (ClientHandler client : clients) {
                client.sendMessage(jsonMessage);       // Сообщение для всех клиентов
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для рассылки множества имен пользователей всем подключенным клиентам
    public void broadcastUserList() {
        UserList userList = new UserList(usernames);
        String jsonUserList;
        try {
            jsonUserList = objectMapper.writeValueAsString(userList);
            for (ClientHandler client : clients) {
                client.sendUser(jsonUserList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}