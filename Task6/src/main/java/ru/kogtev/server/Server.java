package ru.kogtev.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.kogtev.common.Message;
import ru.kogtev.common.UserList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    static Set<String> usernames = new HashSet<>();
    static List<ClientHandler> clients = new ArrayList<>();

    private final ObjectMapper objectMapper;

    private final int port;

    private final ExecutorService executorService;
    private static final Lock usernameLock = new ReentrantLock();

    public Server() {
        this.port = ConfigManager.getProperty();
        this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
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
                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            System.out.println("The client has not been accept" + e.getMessage());
        } finally {
            shutdownExecutorService();
        }

    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public void addUsername(String username) {
        usernameLock.lock();
        try {
            usernames.add(username);
        } finally {
            usernameLock.unlock();
        }
    }

    public void removeUsername(String username) {
        usernameLock.lock();
        try {
            usernames.remove(username);
        } finally {
            usernameLock.unlock();
        }
        broadcastUserList();
        broadcastMessage(new Message(username, " has left the chat"));
    }

    public Set<String> getUsernames() {
        return usernames;
    }

    public Lock getUsernameLock() {
        return usernameLock;
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

    private void shutdownExecutorService() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.out.println("Executor service did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}