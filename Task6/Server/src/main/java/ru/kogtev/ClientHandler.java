package ru.kogtev;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class ClientHandler implements Runnable {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);
    private static final String SERVER_NAME = "Server";

    private final Socket clientSocket;
    private final Server server;

    private String username;
    private PrintWriter writer;
    private final ObjectMapper objectMapper;


    public ClientHandler(Socket clientSocket, Server server, ObjectMapper objectMapper) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run() {
        try (OutputStreamWriter outputStreamWriter =
                     new OutputStreamWriter(clientSocket.getOutputStream());
             InputStreamReader inputStreamReader =
                     new InputStreamReader(clientSocket.getInputStream());
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            writer = new PrintWriter(outputStreamWriter, true);

            connectUser(reader);

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                server.broadcastMessage(new ChatMessage(username, inputLine));
            }
        } catch (IOException e) {
            logger.warn("Сообщение не было отправлено {}", e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void connectUser(BufferedReader reader) throws IOException {
        try {
            username = reader.readLine();

            Lock usernameLock = server.getUsernameLock();

            while (true) {
                if (usernameLock.tryLock(5, TimeUnit.SECONDS)) {
                    logger.info("Поток заблокирован");
                    try {
                        if (isUsernameAvailable(username)) {
                            server.addUsername(username);
                            logger.info("Добавляем юзера - {}", username);
                            break;
                        }
                    } finally {
                        logger.info("Поток разблокирован");
                        usernameLock.unlock();
                    }
                }

                logger.info("Вводим еще раз пользователя");
                writer.println(objectMapper.writeValueAsString(new ChatMessage(SERVER_NAME,
                        "This username is already taken. Please enter a different username: ")));
                username = reader.readLine();
            }


            writer.println(objectMapper.writeValueAsString(new ChatMessage(SERVER_NAME,
                    "Welcome to the chat, " + username + "!")));

            server.broadcastMessage(new ChatMessage(SERVER_NAME, username + " has joined the chat"));
            server.broadcastMessage(new UserListMessage(server.getUsernames()));
        } catch (IOException | InterruptedException e) {
            logger.info("Ошибка при установке имени пользователя {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }


    public void sendMessage(String jsonMessage) {
        writer.println(jsonMessage);
    }


    private boolean isUsernameAvailable(String username) {
        logger.info("Доступно ли имя пользователя");
        return !server.getUsernames().contains(username);
    }

    private void closeConnection() {
        if (username != null) {
            server.removeUsername(username);
        }
        if (writer != null) {
            writer.close();
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            logger.warn("Сокет клиента не был закрыт {}", e.getMessage());
        }
        server.removeClient(this);
        logger.warn("Коннект закрыт");
    }
}
