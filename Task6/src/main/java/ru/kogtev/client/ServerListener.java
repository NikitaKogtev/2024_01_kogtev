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
    private Socket socket;
    private BufferedReader bufferedReader; // Поток ввода для чтения сообщений от сервера
    private ObjectMapper objectMapper;

    public ServerListener(Socket socket) {
        this.socket = socket;
        this.objectMapper = new ObjectMapper(); // Создаем объект ObjectMapper для десериализации JSON
    }

    @Override
    public void run() {
        try {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);

            bufferedReader = new BufferedReader(inputStreamReader); // Создаем поток ввода для чтения сообщений от сервера

            String jsonMessage;
            // Читаем JSON-строки от сервера и преобразуем их в объекты Message и UserList

            while ((jsonMessage = bufferedReader.readLine()) != null) {
                System.out.println(jsonMessage);

                try {
                    Message message = objectMapper.readValue(jsonMessage, Message.class);
                    handleMessage(message);
                } catch (IOException e) {
                    // Возможно, это не сообщение, а список пользователей
                    try {
                        UserList userList = objectMapper.readValue(jsonMessage, UserList.class);
                        handleUserList(userList);
                    } catch (IOException ex) {
                        // Обработка ошибок при чтении JSON
                        ex.printStackTrace();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close(); // Закрываем сокет при отключении от сервера
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMessage(Message message) {
        ClientMain.appendMessage(message.getSender() + ": " + message.getContent());
    }

    private void handleUserList(UserList userList) {
        ClientMain.updateUserList(userList.getUsers());
    }
}
