package ru.kogtev.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kogtev.common.Message;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private ServerMain serverMain;

    private String username;
    private PrintWriter outMessage;
    private BufferedReader reader;
    private ObjectMapper objectMapper;


    public ClientHandler(Socket clientSocket, ServerMain serverMain) {
        this.clientSocket = clientSocket;
        this.serverMain = serverMain;
        this.objectMapper = new ObjectMapper(); // Создаем объект ObjectMapper для сериализации объектов в JSON
        serverMain.broadcastUserList();
    }


//    public void sendMessage(String message){
//        System.out.println(message + "sendMessage");
//        try{
//            outMessage.println(message);
//        }catch (Exception e){
//
//        }
//    }

    public void sendMessage(String jsonMessage) {
        try {
            // Десериализуем JSON в объект Message
            Message message = objectMapper.readValue(jsonMessage, Message.class);
            // Преобразуем объект Message обратно в JSON-строку
            String messageString = objectMapper.writeValueAsString(message);
            // Выводим JSON-строку на консоль
            outMessage.println(messageString);
            System.out.println(messageString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendUser(String message){
        try{
            outMessage.println(message);
        }catch (Exception e){

        }
    }

    public void closeConnection(){
        serverMain.removeClient(this);
        serverMain.broadcast(new Message(username,"Server has left the chat"));
    }

    public boolean isUsernameAvailable(String username) {
        return serverMain.getUsernames().contains(username);
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
            //   printWriter.println("Enter your username: ");
            username = reader.readLine();

            // Проверяем доступность имени пользователя

            while (isUsernameAvailable(username)) {
                outMessage.println("This username is already taken. Please enter a different username: ");
                username = reader.readLine();
            }

            // Добавляем имя клиента в множество имен
            serverMain.addUsername(username);

            // Приветствуем клиента в чате
           // outMessage.println(new Message("Server: " , "Welcome to the chat!"));

            // Добавляем поток вывода клиента в список активных клиентов

            // Рассылаем всем клиентам сообщение о присоединении нового участника
           // outMessage.println(username + "Server has joined the chat");

            serverMain.broadcastUserList();

            String inputLine;
            // Читаем сообщения от клиента и рассылаем их всем остальным клиентам
            while ((inputLine = reader.readLine()) != null) {
                serverMain.broadcast(new Message(username, inputLine));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // При отключении клиента удаляем его имя из множества имен, закрываем потоки и уведомляем остальных клиентов
            if (username != null) {
                serverMain.removeUsername(username);
                serverMain.broadcast(new Message(username,"Server has left the chat"));
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
}
