package ru.kogtev.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kogtev.common.ChatMessage;
import ru.kogtev.common.Message;
import ru.kogtev.common.UserListMessage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientHandler implements Runnable {
    private static final String SERVER_NAME = "Server";

    private final Socket clientSocket;
    private final Server server;

    private String username;
    private PrintWriter writer;
    private final ObjectMapper objectMapper;


    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void run() {
        try (OutputStreamWriter outputStreamWriter =
                     new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8);
             InputStreamReader inputStreamReader =
                     new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            writer = new PrintWriter(outputStreamWriter, true);

            // Выполнение процесса подключения пользователя
            connectUser(reader);

            // Чтение и обработка обычных сообщений
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                server.broadcastMessage(new ChatMessage(username, inputLine));
            }
        } catch (IOException e) {
            System.out.println("The message was not sent: " + e.getMessage());
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
                    System.out.println("Заблокирован");
                    try {
                        if (isUsernameAvailable(username)) {
                            server.addUsername(username);
                            System.out.println("Добавляем юзера");
                            break;
                        }
                    } finally {
                        System.out.println("Разблокирован");
                        usernameLock.unlock();
                    }
                }

                System.out.println("Вводим еще раз пользователя");
                writer.println(objectMapper.writeValueAsString(new ChatMessage(SERVER_NAME,
                        "This username is already taken. Please enter a different username: ")));
                username = reader.readLine();
            }

            writer.println(objectMapper.writeValueAsString(new ChatMessage(SERVER_NAME,
                    "Welcome to the chat, " + username + "!")));

            server.broadcastMessage(new ChatMessage(SERVER_NAME, username + " has joined the chat"));
            server.broadcastMessage(new UserListMessage(server.getUsernames()));
        } catch (IOException | InterruptedException e) {
            System.out.println("Error while setting username: " + e.getMessage());
        }
    }


    public void sendMessage(String jsonMessage) {
        writer.println(jsonMessage);
    }


    public boolean isUsernameAvailable(String username) {
        return !server.getUsernames().contains(username);
    }

    public void closeConnection() {
        if (username != null) {
            server.removeUsername(username);
        }
        if (writer != null) {
            writer.close();
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("The client socket could not be closed: " + e.getMessage());
        }
        server.removeClient(this);
    }
}
