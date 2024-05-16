package ru.kogtev.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kogtev.common.Message;
import ru.kogtev.common.UserList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerListener implements Runnable {
    private final Socket socket;
    private final ObjectMapper objectMapper;
    private final Client client;

    public ServerListener(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void run() {
        try (InputStreamReader inputStreamReader =
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            String jsonMessage;

            while ((jsonMessage = reader.readLine()) != null) {
                processJsonMessage(jsonMessage);
            }

        } catch (IOException e) {
            System.out.println("The socket has not been received " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("The socket could not be closed " + e.getMessage());
            }
        }
    }

    private void processJsonMessage(String jsonMessage) {
        try {
            Message message = objectMapper.readValue(jsonMessage, Message.class);
            handleMessage(message);
        } catch (IOException e) {
            try {
                UserList userList = objectMapper.readValue(jsonMessage, UserList.class);
                handleUserList(userList);
            } catch (IOException ex) {
                System.out.println("The message cannot be converted" + ex.getMessage());
            }
        }
    }

    private void handleMessage(Message message) {
        client.appendMessageToChat(message.getSender() + message.getContent());
    }

    private void handleUserList(UserList userList) {
        client.updateUserList(userList.getUsers());
    }
}
