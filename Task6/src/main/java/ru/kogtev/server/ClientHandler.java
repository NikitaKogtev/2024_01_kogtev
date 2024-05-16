package ru.kogtev.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kogtev.common.Message;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {
    private static final String SERVER_NAME = "Server: ";

    private final Socket clientSocket;
    private final Server server;

    private String username;
    private PrintWriter writer;
    private final ObjectMapper objectMapper;


    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.objectMapper = new ObjectMapper();
        server.broadcastUserList();
    }

    @Override
    public void run() {
        try (OutputStreamWriter outputStreamWriter =
                     new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8);
             InputStreamReader inputStreamReader =
                     new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            writer = new PrintWriter(outputStreamWriter, true);

            username = reader.readLine();

            while (isUsernameAvailable(username)) {
                writer.println(objectMapper.writeValueAsString(new Message(SERVER_NAME,
                        "This username is already taken. Please enter a different username: ")));
                username = reader.readLine();
            }

            server.addUsername(username);

            writer.println(objectMapper.writeValueAsString(new Message(SERVER_NAME,
                    "Welcome to the chat, " + username + "!")));

            server.broadcastMessage(new Message(SERVER_NAME, username + " has joined the chat"));
            server.broadcastUserList();

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                server.broadcastMessage(new Message(username, inputLine));
            }
        } catch (IOException e) {
            System.out.println("The message was not sent" + e.getMessage());
        } finally {
            if (username != null) {
                server.removeUsername(username);
                server.broadcastMessage(new Message(SERVER_NAME, username + " has left the chat"));
            }
            if (writer != null) {
                closeConnection();
            }
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("The client socket could not be closed" + e.getMessage());
            }
        }
    }

    public void sendMessage(String jsonMessage) {
        writer.println(jsonMessage);
    }

    public void sendUser(String jsonMessage) {
        writer.println(jsonMessage);
    }

    public boolean isUsernameAvailable(String username) {
        return server.getUsernames().contains(username);
    }

    public void closeConnection() {
        server.removeClient(this);
        server.broadcastMessage(new Message(SERVER_NAME, username + " has left the chat"));
    }
}
