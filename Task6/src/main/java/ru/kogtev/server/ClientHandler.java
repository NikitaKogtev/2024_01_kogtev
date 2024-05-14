package ru.kogtev.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kogtev.common.Message;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Server server;

    private String username;
    private PrintWriter outMessage;
    private BufferedReader reader;
    private ObjectMapper objectMapper;


    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.objectMapper = new ObjectMapper(); // Создаем объект ObjectMapper для сериализации объектов в JSON
        server.broadcastUserList();
    }

    @Override
    public void run() {
        try (OutputStreamWriter outputStreamWriter =
                     new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8);
             InputStreamReader inputStreamReader =
                     new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8)) {

            outMessage = new PrintWriter(outputStreamWriter, true);
            reader = new BufferedReader(inputStreamReader);

            // Просим клиента ввести имя
            username = reader.readLine();

            // Проверяем доступность имени пользователя

            while (isUsernameAvailable(username)) {
                outMessage.println(objectMapper.writeValueAsString(new Message("Server: ",
                        "This username is already taken. Please enter a different username: ")));
                username = reader.readLine();
            }

            // Добавляем имя клиента в множество имен
            server.addUsername(username);

            // Приветствуем клиента в чате
            outMessage.println(objectMapper.writeValueAsString(new Message("Server: ",
                    "Welcome to the chat, " + username + "!")));

            // Добавляем поток вывода клиента в список активных клиентов

            // Рассылаем всем клиентам сообщение о присоединении нового участника
            server.broadcastMessage(new Message("Server: ", username + " has joined the chat"));

            server.broadcastUserList();

            String inputLine;
            // Читаем сообщения от клиента и рассылаем их всем остальным клиентам
            while ((inputLine = reader.readLine()) != null) {
                server.broadcastMessage(new Message(username, inputLine));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // При отключении клиента удаляем его имя из множества имен, закрываем потоки и уведомляем остальных клиентов
            if (username != null) {
                server.removeUsername(username);
                server.broadcastMessage(new Message("Server: ", username + " has left the chat"));
            }
            if (outMessage != null) {
                closeConnection();
            }
            try {
                clientSocket.close(); // Закрываем сокет клиента
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String jsonMessage) {
        outMessage.println(jsonMessage);
    }

    public void sendUser(String jsonMessage) {
        outMessage.println(jsonMessage);
    }

    public boolean isUsernameAvailable(String username) {
        return server.getUsernames().contains(username);
    }

    public void closeConnection() {
        server.removeClient(this);
        server.broadcastMessage(new Message("Server: ", username + " has left the chat"));
    }
}
