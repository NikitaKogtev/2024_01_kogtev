package ru.kogtev.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ServerMain {
    private static final String PROPERTIES_FILE = "chat.properties";
    private static final String PORT_IN_PROPERTIES_FILE = "PORT";

    public static int PORT;

    public static void main(String[] args) {
        loadConfig();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Принимаем входящее подключение
                System.out.println("New client connected");

                ClientHandler clientHandler = new ClientHandler(clientSocket); // Создаем обработчик клиента
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadConfig() {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream(PROPERTIES_FILE)) {
            prop.load(input);
            PORT = Integer.parseInt(prop.getProperty(PORT_IN_PROPERTIES_FILE));
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error with properties file : " + e.getMessage()); // Исправить формулировку
            // Устанавливаем порт по умолчанию, если возникла ошибка при загрузке конфигурации
            PORT = 8899;
        }
    }
}