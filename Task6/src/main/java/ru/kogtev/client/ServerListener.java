package ru.kogtev.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class ServerListener implements Runnable {
    private Socket socket;
    private BufferedReader bufferedReader; // Поток ввода для чтения сообщений от сервера


    public ServerListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);

            bufferedReader = new BufferedReader(inputStreamReader); // Создаем поток ввода для чтения сообщений от сервера

            String message;
            // Читаем сообщения от сервера и выводим их в окно чата

            while ((message = bufferedReader.readLine()) != null) {
                // Проверяем, является ли сообщение списком пользователей
                if (message.startsWith("USERS:")) {
                    handleUserList(message.substring(6).trim()); // Обрабатываем список пользователей
                } else {
                    // Обрабатываем сообщение в чате
                    handleMessage(message);
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

    private void handleMessage(String message) {
        ClientMain.appendMessage(message);
    }

    private void handleUserList(String userListMessage) {
        ClientMain.appendUsername(userListMessage);
    }

}


