package ru.kogtev;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);

    static Set<String> usernames = new HashSet<>();
    static List<ClientHandler> clients = new ArrayList<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final int port;

    private final ExecutorService executorService;
    private static final Lock usernameLock = new ReentrantLock();

    public Server() {
        port = ConfigManager.getProperty();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            logger.info("Сервер запущен c портом {}", port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Подключился новый клиент");

                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);

                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            logger.error("Клиент не подключился {}", e.getMessage());
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
        logger.info("Добавляем {} в множество пользователей", username);
    }

    public void removeUsername(String username) {
        usernameLock.lock();
        try {
            usernames.remove(username);
        } finally {
            usernameLock.unlock();
        }

        broadcastMessage(new ChatMessage(username, " has left the chat"));
        broadcastMessage(new UserListMessage(usernames));
        logger.info("Удаляем {} из множество пользователей", username);
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
            logger.warn("Сообщение не было отправлено {}", e.getMessage());
        }
    }

    private void shutdownExecutorService() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    logger.warn("Работа executor service сервера не была прекращена");
                }
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.warn("Работа executor service сервера прекращена");
    }
}