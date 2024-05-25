package ru.kogtev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class Client {
    private static final Logger logger = LogManager.getLogger(Client.class);

    private static final AtomicReference<SimpleDateFormat> SIMPLE_DATE_FORMAT =
            new AtomicReference<>(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    private static final String SEPARATOR = ": ";

    private final ChatWindow chatWindow;

    private PrintWriter writer;

    private final ExecutorService executorService;

    public Client() {
        chatWindow = new ChatWindow();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void execute() {
        String serverAddress = chatWindow.initializeServerAddress();
        int port = chatWindow.initializePort();
        String username = chatWindow.initializeUsername();
        chatWindow.initializeChat(this::sendMessageToServer, username);

        try {
            Socket socket = new Socket(serverAddress, port);
            writer = new PrintWriter(socket.getOutputStream(), true);

            executorService.submit(new ServerListener(socket, this));
            appendMessageToChat("Connected to the server");
            logger.info("Подключение к серверу");

            writer.println(username);
        } catch (IOException e) {
            appendMessageToChat("Error connecting to the server: " + e.getMessage());
            logger.error("Ошибка подключения к серверу {}", e.getMessage());
        } finally {
            shutdownExecutorService();
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
        String timestamp = SIMPLE_DATE_FORMAT.get().format(new Date());
        chatWindow.appendMessage(timestamp + SEPARATOR + message);
    }

    public void updateUserList(Set<String> users) {
        chatWindow.updateUserList(users);
    }


    private void shutdownExecutorService() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    logger.warn("Работа executor service клиента не была прекращена");
                }
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.warn("Работа executor service клиента прекращена");
    }
}
